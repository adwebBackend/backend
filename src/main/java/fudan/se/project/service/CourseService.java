package fudan.se.project.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import fudan.se.project.domain.*;
import fudan.se.project.repository.*;
import fudan.se.project.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("Duplicates")
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
    @Autowired
    private CpInclusionRepository cpInclusionRepository;
    @Autowired
    private AuthService authService;

    private final int NUM_PER_PAGE = 9;

    public String createCourse(int userId, JSONObject params){
        if (authService.checkAuthor("teacher",userId)){
            String courseName = params.getString("course_name");
            String backgroundImage = params.getString("background_image");
            String description = params.getString("description");
            Date startTime = params.getDate("start_time");
            Date endTime = params.getDate("end_time");
            Course course = new Course(courseName,description,startTime,endTime,backgroundImage);
            course.setValid(1);
            courseRepository.save(course);

            int courseId = course.getCourseId();
            Teach teach = new Teach(userId,courseId);
            teachRepository.save(teach);

            return "success";
        }
        return "failure";
    }


    public JSONObject teacherViewCourses(int userId,int page){
        JSONObject result = new JSONObject();
        if (authService.checkAuthor("teacher",userId)){

            List<Teach> teaches = teachRepository.findAllByUserId(userId);
            Iterator<Teach> iterator = teaches.iterator();
            while (iterator.hasNext()){
                Teach teach = iterator.next();
                Course course=courseRepository.findByCourseId(teach.getCourseId());
                if (course.getValid() == 0){
                    iterator.remove();
                }
            }
            int total = teaches.size();
            if (Math.ceil((total + 0.0)/NUM_PER_PAGE) >= page){
                JSONArray courseArray = new JSONArray();
                for (int i = (page - 1)*NUM_PER_PAGE;i < page*NUM_PER_PAGE&&i<total;i ++){
                    Teach teach = teaches.get(i);
                    JSONObject courseJSON = new JSONObject();
                    Course course=courseRepository.findByCourseId(teach.getCourseId());
                    courseJSON.put("course_id",course.getCourseId());
                    courseJSON.put("course_name",course.getCourseName());
                    courseJSON.put("background_image",course.getPicture());
                    courseJSON.put("description",course.getCourseIntroduce());
                    courseJSON.put("start_time",course.getCourseStartTime());
                    courseJSON.put("end_time",course.getCourseEndTime());
                    courseArray.add(courseJSON);
                }
                result.put("courses",courseArray);
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
        if (authService.checkAuthor("student",userId)){

            List<Take> takes = takeRepository.findAllByUserId(userId);
            Iterator<Take> iterator = takes.iterator();
            while (iterator.hasNext()){
                Take take = iterator.next();
                Course course=courseRepository.findByCourseId(take.getCourseId());
                if (course.getValid() == 0){
                    iterator.remove();
                }
            }
            int total = takes.size();
            if (Math.ceil((total + 0.0)/NUM_PER_PAGE) >= page){
                JSONArray courseArray = new JSONArray();
                for (int i = (page - 1)*NUM_PER_PAGE;i < page*NUM_PER_PAGE&&i<total;i ++){
                    Take take = takes.get(i);
                    JSONObject courseJSON = new JSONObject();
                    Course course=courseRepository.findByCourseId(take.getCourseId());
                    Teach teach=teachRepository.findByCourseId(course.getCourseId());
                    User teacher=userRepository.findByUserId(teach.getUserId());
                    courseJSON.put("course_id",course.getCourseId());
                    courseJSON.put("course_name",course.getCourseName());
                    courseJSON.put("background_image",course.getPicture());
                    courseJSON.put("description",course.getCourseIntroduce());
                    courseJSON.put("start_time",course.getCourseStartTime());
                    courseJSON.put("end_time",course.getCourseEndTime());
                    courseJSON.put("teacher_name",teacher.getName());
                    courseArray.add(courseJSON);
                }
                result.put("courses",courseArray);
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
        if (authService.checkAuthor("student",userId)){
            List<Take> takes = takeRepository.findAllByUserId(userId);
            List<Integer> limited = new ArrayList<>();
            List<Course> unselected=new ArrayList<>();
            for (Take take:takes){
                limited.add(take.getCourseId());
            }
            if (limited.size()==0){
                unselected=courseRepository.findAll();
            }else {
                unselected = courseRepository.findCourseByLimited(limited);
            }
            unselected.removeIf(course -> course.getValid() == 0);
            for(Course course:unselected){
                if (course.getValid() == 0){
                    unselected.remove(course);
                }
            }
            int total = unselected.size();
            if (Math.ceil((total + 0.0)/NUM_PER_PAGE) >= page){
                JSONArray courseArray = new JSONArray();
                for (int i = (page - 1)*NUM_PER_PAGE;i < page*NUM_PER_PAGE&&i<total;i ++){
                    JSONObject courseJSON = new JSONObject();
                    Course course = unselected.get(i);
                    Teach teach=teachRepository.findByCourseId(course.getCourseId());
                    User teacher=userRepository.findByUserId(teach.getUserId());
                    courseJSON.put("course_id",course.getCourseId());
                    courseJSON.put("course_name",course.getCourseName());
                    courseJSON.put("background_image",course.getPicture());
                    courseJSON.put("description",course.getCourseIntroduce());
                    courseJSON.put("start_time",course.getCourseStartTime());
                    courseJSON.put("end_time",course.getCourseEndTime());
                    courseJSON.put("teacher_name",teacher.getName());
                    courseArray.add(courseJSON);
                }
                result.put("courses",courseArray);
                result.put("total",total);
                return result;
            }
            result.put("message","failure");
            return result;
        }
        result.put("message","failure");
        return result;
    }

    public JSONObject courseBasicInfo(int courseId){
        JSONObject result = new JSONObject();
        Course course=courseRepository.findByCourseId(courseId);
        if (course == null){
            result.put("message","failure");
            return result;
        }

        Teach teach=teachRepository.findByCourseId(course.getCourseId());
        User teacher=userRepository.findByUserId(teach.getUserId());

        result.put("course_name",course.getCourseName());
        result.put("background_image",course.getPicture());
        result.put("description",course.getCourseIntroduce());
        result.put("start_time",course.getCourseStartTime());
        result.put("end_time",course.getCourseEndTime());
        result.put("teacher_name",teacher.getName());
        return result;
    }

    public List<User> courseStudents(int courseId){
        List<Take> takes = takeRepository.findAllByCourseId(courseId);
        List<User> students = new ArrayList<>();
        for (Take take:takes){
            students.add(userRepository.findByUserId(take.getUserId()));
        }
        return students;
    }

    public List<Project> courseProjects(int userId,int courseId) {
        Teach teach = teachRepository.findByCourseIdAndUserId(courseId, userId);
        if (teach == null) {
            return null;
        }

        Course course = courseRepository.findByCourseId(courseId);
        return course.getProjects();
    }

    public List<Project> selectedProjects(int userId,int courseId) {
        if (takeRepository.findByCourseIdAndUserId(courseId, userId) == null) {
            return null;
        }

        Course course = courseRepository.findByCourseId(courseId);
        List<Project> projects = course.getProjects();
        List<Integer> limited = new ArrayList<>();
        for (Project project : projects) {
            limited.add(project.getProjectId());
        }
        List<Participate> participates = participateRepository.findParticipateByUserIdAndLimited(userId, limited);
        List<Project> result = new ArrayList<>();
        for (Participate participate : participates) {
            result.add(projectRepository.findByProjectId(participate.getProjectId()));
        }
        return result;
    }

    public List<Project> unselectedProjects(int userId,int courseId) {
        if (takeRepository.findByCourseIdAndUserId(courseId, userId) == null) {
            return null;
        }
        Course course = courseRepository.findByCourseId(courseId);
        List<Project> projects = course.getProjects();
        List<Integer> limited = new ArrayList<>();
        for (Project project : projects) {
            limited.add(project.getProjectId());
        }
        List<Participate> participates = participateRepository.findAllByUserId(userId);

        List<Project> result = new ArrayList<>();
        for (Participate participate : participates) {
            int index=limited.indexOf(participate.getProjectId());
            if (index != -1)
                limited.remove(index);
        }

        for (Integer limit: limited){
            result.add(projectRepository.findByProjectId(limit));
        }
        return result;
    }

    @Transactional
    public JSONObject deleteCourse(int userId, int courseId) {
        JSONObject result = new JSONObject();
        Teach teach = teachRepository.findByCourseIdAndUserId(courseId, userId);
        if (teach == null) {
            result.put("message","failure");
            return result;
        }

        teachRepository.deleteAllByCourseId(courseId);
        takeRepository.deleteAllByCourseId(courseId);
        Course course=courseRepository.findByCourseId(courseId);
        course.setValid(0);
        courseRepository.save(course);
        result.put("message","success");
        return result;
    }

    public JSONObject addCourse(int userId, int courseId){
        JSONObject result = new JSONObject();
        if (authService.checkAuthor("student",userId)){
            Course course = courseRepository.findByCourseId(courseId);
            if (course == null||course.getCourseEndTime().getTime()<=new Date().getTime()){
                result.put("message","failure");
                return result;
            }
            if (takeRepository.findByCourseIdAndUserId(courseId,userId)==null){
                Take take = new Take(userId,courseId);
                takeRepository.save(take);
                result.put("message","success");
                return result;
            }
            result.put("message","failure");
            return result;
        }
        result.put("message","failure");
        return result;
    }
}
