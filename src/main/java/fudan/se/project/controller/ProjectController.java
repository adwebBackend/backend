package fudan.se.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fudan.se.project.controller.request.PostRequest;
import fudan.se.project.controller.request.ProjectRequest;
import fudan.se.project.controller.request.ReplyRequest;
import fudan.se.project.domain.Project;
import fudan.se.project.domain.User;
import fudan.se.project.service.AuthService;
import fudan.se.project.service.FileService;
import fudan.se.project.service.ProjectService;
import fudan.se.project.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("Duplicates")
@RestController
@Table(name = "project")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Validated
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private FileService fileService;

    @PostMapping("/create_project")
    @ResponseBody
    public ResponseEntity<?> createProject(@Validated @RequestBody ProjectRequest request, BindingResult bindingResult){
        JSONObject result = Tool.DealParamError(bindingResult);
        if (result != null){
            return new ResponseEntity<>(result.toJSONString(), HttpStatus.BAD_REQUEST);
        }

        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        String message = projectService.createProject(userId,request);
        JSONObject response = new JSONObject();
        response.put("message",message);
        return Tool.getResponseEntity(response);
    }

    @GetMapping("/delete_project")
    @ResponseBody
    public ResponseEntity<?> deleteProject(@Validated @RequestParam(value = "project_id") int projectId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        String message = projectService.deleteProject(userId,projectId);
        JSONObject response = new JSONObject();
        response.put("message",message);
        return Tool.getResponseEntity(response);
    }

    @GetMapping("/project_basic_info")
    @ResponseBody
    public ResponseEntity<?> projectBasicInfo(@Validated @RequestParam(value = "project_id") int projectId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        JSONObject result = projectService.projectBasicInfo(userId,projectId);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/group_members")
    @ResponseBody
    public ResponseEntity<?> groupMembers(@Validated @RequestParam(value = "project_id") int projectId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        JSONObject result = projectService.groupMembers(userId,projectId);
        return Tool.getResponseEntity(result);

    }

    @GetMapping("/all_tasks")
    @ResponseBody
    public ResponseEntity<?> allTasks(@Validated @RequestParam(value = "project_id") int projectId) {
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        JSONObject result = projectService.allTasks(userId, projectId);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/my_tasks")
    @ResponseBody
    public ResponseEntity<?> myTasks(@Validated @RequestParam(value = "project_id") int projectId) {
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        JSONObject result = projectService.myTasks(userId, projectId);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/view_posts")
    @ResponseBody
    public ResponseEntity<?> viewPosts(@Validated @RequestParam(value = "project_id") int projectId, @Validated @RequestParam(value = "page") int page){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        JSONObject result = projectService.viewPosts(userId,projectId,page);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/view_replies")
    @ResponseBody
    public ResponseEntity<?> viewReplies(@Validated @RequestParam(value = "post_id") int postId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        JSONObject result = projectService.viewReplies(userId,postId);
        return Tool.getResponseEntity(result);
    }

    @PostMapping("/post")
    @ResponseBody
    public ResponseEntity<?> post(@Validated @RequestBody PostRequest request,BindingResult bindingResult){
        JSONObject error = Tool.DealParamError(bindingResult);
        if (error != null){
            return new ResponseEntity<>(error.toJSONString(), HttpStatus.BAD_REQUEST);
        }
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        String message = projectService.post(userId,request);
        JSONObject result = new JSONObject();
        result.put("message",message);
        return Tool.getResponseEntity(result);
    }

    @PostMapping("/reply")
    @ResponseBody
    public ResponseEntity<?> reply(@Validated @RequestBody ReplyRequest replyRequest,BindingResult bindingResult){
        JSONObject error = Tool.DealParamError(bindingResult);
        if (error != null){
            return new ResponseEntity<>(error.toJSONString(), HttpStatus.BAD_REQUEST);
        }
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        String message = projectService.reply(userId,replyRequest);
        JSONObject result = new JSONObject();
        result.put("message",message);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/files")
    @ResponseBody
    public ResponseEntity<?> files(@Validated @RequestParam(value = "project_id") int projectId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        JSONObject result = projectService.files(userId,projectId);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/like")
    @ResponseBody
    public ResponseEntity<?> like(@Validated@RequestParam(value = "post_id")int postId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        String message = projectService.like(userId,postId);
        JSONObject result = new JSONObject();
        result.put("message",message);
        if (message.equals("failure")){
            return new ResponseEntity<>(result.toJSONString(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result.toJSONString(),HttpStatus.OK);
    }

    @GetMapping("/add_project")
    @ResponseBody
    public ResponseEntity<?> addProject(@Validated @RequestParam(value = "project_id") int projectId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        String message = projectService.addProject(userId, projectId);
        JSONObject result = new JSONObject();
        result.put("message",message);
        return Tool.getResponseEntity(result);

    }

    @GetMapping("/teacher_score")
    @ResponseBody
    public ResponseEntity<?> teacherScore(@Validated @RequestParam(value = "project_id") int projectId,@Validated @RequestParam(value = "student_id")int studentId,@Validated @RequestParam(value = "score")int score){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        String message = projectService.teacherScore(userId,projectId,studentId,score);
        JSONObject result = new JSONObject();
        result.put("message",message);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/view_score")
    @ResponseBody
    public ResponseEntity<?> viewScore(@Validated @RequestParam(value = "project_id") int projectId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        JSONObject result = projectService.viewScore(userId,projectId);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/mutual_evaluation")
    @ResponseBody
    public ResponseEntity<?> mutualEvaluation(@Validated @RequestParam(value = "project_id") int projectId,@Validated @RequestParam(value = "student_id")int studentId,@Validated @RequestParam(value = "score")int score){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        String message = projectService.mutualEvaluation(userId,projectId,studentId,score);
        JSONObject result = new JSONObject();
        result.put("message",message);
        return Tool.getResponseEntity(result);
    }


    @GetMapping("/self_evaluation")
    @ResponseBody
    public ResponseEntity<?> selfEvaluation(@Validated @RequestParam(value = "project_id") int projectId,@Validated @RequestParam(value = "score")int score){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        String message = projectService.selfEvaluation(userId,projectId,score);
        JSONObject result = new JSONObject();
        result.put("message",message);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/publish_score")
    @ResponseBody
    public ResponseEntity<?> publish_score(@Validated @RequestParam(value = "project_id") int projectId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        String message = projectService.publish_score(userId,projectId);
        JSONObject result = new JSONObject();
        result.put("message",message);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/view_all_scores")
    @ResponseBody
    public ResponseEntity<?> view_all_scores(@Validated @RequestParam(value = "project_id") int projectId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        JSONObject result = projectService.view_all_scores(userId,projectId);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/choose_leader")
    @ResponseBody
    public ResponseEntity<?> choose_leader(@Validated @RequestParam(value = "student_id") int student_id, @Validated @RequestParam(value = "project_id") int projectId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        JSONObject result = projectService.choose_leader(userId,student_id,projectId);
        return Tool.getResponseEntity(result);
    }
}
