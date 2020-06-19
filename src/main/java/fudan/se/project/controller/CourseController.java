package fudan.se.project.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fudan.se.project.domain.Course;
import fudan.se.project.domain.Participate;
import fudan.se.project.domain.Project;
import fudan.se.project.domain.User;
import fudan.se.project.repository.ParticipateRepository;
import fudan.se.project.service.CourseService;
import fudan.se.project.service.FileService;
import fudan.se.project.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.Table;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@SuppressWarnings("Duplicates")
@RestController
@Table(name = "course")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Validated
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ParticipateRepository participateRepository;

    @CrossOrigin(origins = "*")
    @PostMapping("/create_course")
    @ResponseBody
    public ResponseEntity<?> createCourse(@Validated @RequestParam("file") MultipartFile file, @Validated @RequestParam("params") String params) throws IOException {
        JSONObject json= JSONObject.parseObject(params);
        if (json.getString("course_name")==null||json.getString("course_name").equals("")||json.getString("description")==null||json.getString("description").equals("")||json.getString("start_time")==null||json.getString("end_time")==null||json.getDate("start_time").getTime()<=new Date().getTime()||json.getDate("start_time").getTime()>json.getDate("end_time").getTime()){
            return Tool.getErrorJson("parameter error");
        }
        //检查是否是图片
        BufferedImage bi = ImageIO.read(file.getInputStream());
        if (bi == null){
            json.put("message","An image is required");
            return new ResponseEntity<>(json.toJSONString(),HttpStatus.BAD_REQUEST);
        }
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        String backgroundImage = fileService.saveFile(file);
        json.put("background_image",backgroundImage);
        return Tool.getResponseEntity(courseService.createCourse(userId,json));
    }

    @GetMapping("/teacher_view_courses")
    @ResponseBody
    public ResponseEntity<?> teacherViewCourses(int page){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));

        return Tool.getResponseEntity( courseService.teacherViewCourses(userId,page));
    }

    @GetMapping("/student_view_courses")
    @ResponseBody
    public ResponseEntity<?> studentViewCourses(int page){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));

        return Tool.getResponseEntity( courseService.studentViewCourses(userId,page));
    }

    @GetMapping("/student_view_unselected_courses")
    @ResponseBody
    public ResponseEntity<?> studentViewUnselectedCourses(int page){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        return Tool.getResponseEntity(courseService.studentViewUnselectedCourses(userId,page));
    }

    @GetMapping("/course_basic_info")
    @ResponseBody
    public ResponseEntity<?> courseBasicInfo(int course_id){
        return Tool.getResponseEntity(courseService.courseBasicInfo(course_id));
    }

    @GetMapping("/course_students")
    @ResponseBody
    public ResponseEntity<?> courseStudents(int course_id){
        List<User> students = courseService.courseStudents(course_id);

        if (students.size() == 0){
            return Tool.getErrorJson("failure");
        }
        JSONObject result = new JSONObject();
        JSONArray studentArray = new JSONArray();
        for (User user:students){
            JSONObject studentJSON = new JSONObject();
            studentJSON.put("student_name",user.getName());
            studentJSON.put("avatar",user.getAvatar());
            studentArray.add(studentJSON);
        }
        result.put("students",studentArray);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/course_projects")
    @ResponseBody
    public ResponseEntity<?> courseProjects(int course_id){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));

        List<Project> projects = courseService.courseProjects(userId,course_id);
        if (projects == null){
            return Tool.getErrorJson("failure");
        }
        JSONObject result = new JSONObject();
        JSONArray projectArray = new JSONArray();
        for (Project project:projects){
            JSONObject projectJSON = new JSONObject();
            projectJSON.put("project_id",project.getProjectId());
            projectJSON.put("name",project.getProjectName());
            projectJSON.put("introduce",project.getProjectIntroduce());
            projectJSON.put("start_time",project.getProjectStartTime());
            projectJSON.put("end_time",project.getProjectEndTime());
            projectArray.add(projectJSON);
        }
        result.put("projects",projectArray);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/selected_projects")
    @ResponseBody
    public ResponseEntity<?> selectedProjects(int course_id){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        List<Project> projects = courseService.selectedProjects(userId,course_id);
        if (projects == null){
            return Tool.getErrorJson("failure");
        }
        JSONObject result = new JSONObject();
        JSONArray projectArray = new JSONArray();
        for (Project project:projects){
            Participate participate=participateRepository.findByProjectIdAndUserId(project.getProjectId(),userId);
            JSONObject projectJSON = new JSONObject();
            projectJSON.put("project_id",project.getProjectId());
            projectJSON.put("name",project.getProjectName());
            projectJSON.put("introduce",project.getProjectIntroduce());
            projectJSON.put("start_time",project.getProjectStartTime());
            projectJSON.put("end_time",project.getProjectEndTime());
            projectJSON.put("is_leader",participate.getIsGroupLeader());
            projectArray.add(projectJSON);
        }
        result.put("projects",projectArray);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/unselected_projects")
    @ResponseBody
    public ResponseEntity<?> unselectedProjects(int course_id){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        List<Project> projects = courseService.unselectedProjects(userId,course_id);
        if (projects == null){
            return Tool.getErrorJson("failure");
        }
        JSONObject result = new JSONObject();
        JSONArray projectArray = new JSONArray();
        for (Project project:projects){
            JSONObject projectJSON = new JSONObject();
            projectJSON.put("project_id",project.getProjectId());
            projectJSON.put("name",project.getProjectName());
            projectJSON.put("introduce",project.getProjectIntroduce());
            projectJSON.put("start_time",project.getProjectStartTime());
            projectJSON.put("end_time",project.getProjectEndTime());
            projectArray.add(projectJSON);
        }
        result.put("projects",projectArray);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/delete_course")
    @ResponseBody
    public ResponseEntity<?> deleteCourse(@Validated @RequestParam(value = "course_id")int course_id){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        JSONObject result = courseService.deleteCourse(userId,course_id);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/add_course")
    @ResponseBody
    public ResponseEntity<?> addCourse(@Validated @RequestParam(value = "course_id") int course_id){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        JSONObject result = courseService.addCourse(userId,course_id);
        return Tool.getResponseEntity(result);
    }
}
