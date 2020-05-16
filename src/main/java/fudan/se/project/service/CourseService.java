package fudan.se.project.service;

import com.alibaba.fastjson.JSONObject;
import fudan.se.project.domain.*;
import fudan.se.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeachRepository teachRepository;
    @Autowired
    private TakeRepository takeRepository;
    @Autowired
    private ParticipateRepository participateRepository;
    @Autowired
    private  ProjectRepository projectRepository;

    private final int NUM_PER_PAGE = 9;

    public String createCourse(int userId, JSONObject params){
        if (checkAuthor("teacher",userId)){
            String courseName = params.getString("course_name");
            String backgroundImage = params.getString("background_image");
            String description = params.getString("description");
            Date startTime = params.getDate("start_time");
            Date endTime = params.getDate("end_time");
            Course course = new Course(courseName,description,startTime,endTime,backgroundImage);
            courseRepository.save(course);

            int courseId = course.getCourseId();
            Teach teach = new Teach(userId,courseId);
            teachRepository.save(teach);

            return "success";
        }
        return "failure";
    }


    public String saveFile(MultipartFile file){
        //文件后缀名
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //上传文件名
        String filename = UUID.randomUUID() + suffix;
        //服务器端保存的文件对象
        String saveDir = "D:\\Documents\\AD web\\pj\\courseImages";
        File serverFile = new File(saveDir + filename);

        if(!serverFile.exists()) {
            //先得到文件的上级目录，并创建上级目录，在创建文件
            serverFile.getParentFile().mkdir();
            try {
                //创建文件
                serverFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //将上传的文件写入到服务器端文件内
        try {
            file.transferTo(serverFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverFile.getAbsolutePath();
    }

    public boolean checkAuthor(String author,int userId){
        User user = userRepository.findByUserId(userId);
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority grantedAuthority:authorities) {
            if (grantedAuthority.getAuthority().equals(author)) {
                return true;
            }
        }
        return false;
    }

    public JSONObject teacherViewCourses(int userId,int page){
        JSONObject result = new JSONObject();
        if (checkAuthor("teacher",userId)){

            List<Teach> teaches = teachRepository.findAllByUserId(userId);
            int total = teaches.size();
            if (Math.ceil((total + 0.0)/NUM_PER_PAGE) >= page){
                List<Course> courses = new ArrayList<>();
                Page<Teach> teachPage = teachRepository.findByUserId(userId, PageRequest.of(page - 1, NUM_PER_PAGE));
                for (Teach teach:teachPage){
                    courses.add(courseRepository.findByCourseId(teach.getCourseId()));
                }
                result.put("courses",courses);
                result.put("total",total);
                return result;
            }
            result.put("message","failure");
            return result;
        }
        result.put("message","failure");
        return result;
    }

    public JSONObject studentViewCourses(int userId,int page){
        JSONObject result = new JSONObject();
        if (checkAuthor("student",userId)){

            List<Take> takes = takeRepository.findAllByUserId(userId);
            int total = takes.size();
            if (Math.ceil((total + 0.0)/NUM_PER_PAGE) >= page){
                List<Course> courses = new ArrayList<>();
                Page<Take> takePage = takeRepository.findByUserId(userId, PageRequest.of(page - 1, NUM_PER_PAGE));
                for (Take take:takePage){
                    courses.add(courseRepository.findByCourseId(take.getCourseId()));
                }
                result.put("courses",courses);
                result.put("total",total);
                return result;
            }
            result.put("message","failure");
            return result;
        }
        result.put("message","failure");
        return result;
    }

    public JSONObject studentViewUnselectedCourses(int userId,int page){
        JSONObject result = new JSONObject();
        if (checkAuthor("student",userId)){
            List<Take> takes = takeRepository.findAllByUserId(userId);
            List<Integer> limited = new ArrayList<>();
            for (Take take:takes){
                limited.add(take.getCourseId());
            }
            List<Course> unselected = courseRepository.findCourseByLimited(limited);
            int total = unselected.size();
            if (Math.ceil((total + 0.0)/NUM_PER_PAGE) >= page){
                List<Course> courses = new ArrayList<>();
                for (int i = (page - 1)*NUM_PER_PAGE;i < page*NUM_PER_PAGE;i ++){
                    courses.add(unselected.get(i));
                }
                result.put("courses",courses);
                result.put("total",total);
                return result;
            }
            result.put("message","failure");
            return result;
        }
        result.put("message","failure");
        return result;
    }

    public Course courseBasicInfo(int courseId){
        return courseRepository.findByCourseId(courseId);
    }

    public List<User> courseStudents(int courseId){
        List<Take> takes = takeRepository.findAllByCourseId(courseId);
        List<User> students = new ArrayList<>();
        for (Take take:takes){
            students.add(userRepository.findByUserId(take.getUserId()));
        }
        return students;
    }

    public List<Project> courseProjects(int userId,int courseId){
        if (checkAuthor("teacher",userId)){
            Course course = courseRepository.findByCourseId(courseId);
            return course.getProjects();
        }
        return null;
    }

    public List<Project> selectedProjects(int userId,int courseId){
        if (checkAuthor("student",userId)){
            Course course = courseRepository.findByCourseId(courseId);
            List<Project> projects = course.getProjects();
            List<Integer> limited = new ArrayList<>();
            for (Project project:projects){
                limited.add(project.getProjectId());
            }
            List<Participate> participates = participateRepository.findParticipateByUserIdAndLimited(userId,limited);
            List<Project> result = new ArrayList<>();
            for (Participate participate:participates){
                result.add(projectRepository.findByProjectId(participate.getProjectId()));
            }
            return result;
        }
        return null;
    }

    public List<Project> unselectedProjects(int userId,int courseId){
        if (checkAuthor("student",userId)){
            Course course = courseRepository.findByCourseId(courseId);
            List<Project> projects = course.getProjects();
            List<Integer> limited = new ArrayList<>();
            for (Project project:projects){
                limited.add(project.getProjectId());
            }
            List<Participate> participates = participateRepository.findParticipateByUserIdAndNotLimited(userId,limited);
            List<Project> result = new ArrayList<>();
            for (Participate participate:participates){
                result.add(projectRepository.findByProjectId(participate.getProjectId()));
            }
            return result;
        }
        return null;
    }
}
