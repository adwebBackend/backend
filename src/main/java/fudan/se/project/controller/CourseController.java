package fudan.se.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fudan.se.project.domain.Course;
import fudan.se.project.domain.Project;
import fudan.se.project.domain.User;
import fudan.se.project.service.CourseService;
import fudan.se.project.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@RestController
@Table(name = "course")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Validated
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private FileService fileService;

    @CrossOrigin(origins = "*")
    @PostMapping("/create_course")
    @ResponseBody
    public ResponseEntity<?> createCourse(@RequestParam("file") MultipartFile file, @RequestBody JSONObject params){
        int userId = Integer.parseInt((((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        String backgroundImage = fileService.saveFile(file);
        params.put("background_image",backgroundImage);
        return ResponseEntity.ok(courseService.createCourse(userId,params));
    }

    @GetMapping("/teacher_view_courses")
    @ResponseBody
    public ResponseEntity<?> teacherViewCourses(int page){
        int userId = Integer.parseInt((((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));

        return ResponseEntity.ok( courseService.teacherViewCourses(userId,page));
    }

    @GetMapping("/student_view_courses")
    @ResponseBody
    public ResponseEntity<?> studentViewCourses(int page){
        int userId = Integer.parseInt((((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));

        return ResponseEntity.ok( courseService.studentViewCourses(userId,page));
    }

    @GetMapping("student_view_unselected_courses")
    @ResponseBody
    public ResponseEntity<?> studentViewUnselectedCourses(int page){
        int userId = Integer.parseInt((((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        return ResponseEntity.ok(courseService.studentViewUnselectedCourses(userId,page));
    }

    @GetMapping("/course_basic_info")
    @ResponseBody
    public ResponseEntity<?> courseBasicInfo(int courseId){
        Course course = courseService.courseBasicInfo(courseId);
        JSONObject result = new JSONObject();
        if (course == null){
            result.put("message","failure");
            return ResponseEntity.ok(result);
        }
        String courseName = course.getCourseName();
        String backgroundImage = course.getPicture();
        String description = course.getCourseIntroduce();
        Date startTime = course.getCourseStartTime();
        Date endTime = course.getCourseEndTime();
        result.put("course_name",courseName);
        result.put("background_image",backgroundImage);
        result.put("description",description);
        result.put("start_time",startTime);
        result.put("end_time",endTime);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/course_students")
    @ResponseBody
    public ResponseEntity<?> courseStudents(int courseId){
        List<User> students = courseService.courseStudents(courseId);
        if (students == null){
            return ResponseEntity.ok("failure");
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/course_projects")
    @ResponseBody
    public ResponseEntity<?> courseProjects(int courseId){
        int userId = Integer.parseInt((((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));

        List<Project> projects = courseService.courseProjects(userId,courseId);
        if (projects == null){
            return ResponseEntity.ok("failure");
        }
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/selected_projects")
    @ResponseBody
    public ResponseEntity<?> selectedProjects(int courseId){
        int userId = Integer.parseInt((((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        List<Project> projects = courseService.selectedProjects(userId,courseId);
        if (projects == null){
            return ResponseEntity.ok("failure");
        }
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/unselected_projects")
    @ResponseBody
    public ResponseEntity<?> unselectedProjects(int courseId){
        int userId = Integer.parseInt((((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        List<Project> projects = courseService.unselectedProjects(userId,courseId);
        if (projects == null){
            return ResponseEntity.ok("failure");
        }
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/delete_course")
    @ResponseBody
    public ResponseEntity<?> deleteCourse(@Validated @RequestParam(value = "course_id")int courseId){
//        int userId = Integer.parseInt((((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        int userId = 7;
        String result = courseService.deleteCourse(userId,courseId);
        if (result.equals("success")){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/add_course")
    @ResponseBody
    public ResponseEntity<?> addCourse(@Validated @RequestParam(value = "course_id")int courseId){
//        int userId = Integer.parseInt((((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        int userId = 1;
        String result = courseService.addCourse(userId,courseId);
        if (result.equals("success")){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
}
