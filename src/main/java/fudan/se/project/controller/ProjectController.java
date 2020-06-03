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
//        int userId = 7;
        JSONObject result = projectService.groupMembers(userId,projectId);
        if (result.getString("message") != null){
            return new ResponseEntity<>(result.toJSONString(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result.toJSONString(),HttpStatus.OK);

    }

    @GetMapping("/all_tasks")
    @ResponseBody
    public ResponseEntity<?> allTasks(@Validated @RequestParam(value = "project_id") int projectId){
                int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
//        int userId = 7;
        JSONObject result = projectService.allTasks(userId,projectId);
        if (result.getString("message") != null){
            return new ResponseEntity<>(result.toJSONString(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result.toJSONString(),HttpStatus.OK);
    }

    @GetMapping("/my_tasks")
    @ResponseBody
    public ResponseEntity<?> myTasks(@Validated @RequestParam(value = "project_id") int projectId){
                int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
//        int userId = 6;
        JSONObject result = projectService.myTasks(userId,projectId);
        if (result.getString("message") != null){
            return new ResponseEntity<>(result.toJSONString(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result.toJSONString(),HttpStatus.OK);

    }

    @GetMapping("/view_posts")
    @ResponseBody
    public ResponseEntity<?> viewPosts(@Validated @RequestParam(value = "project_id") int projectId, @Validated @RequestParam(value = "page") int page){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
//        int userId = 7;
        JSONObject result = projectService.viewPosts(userId,projectId,page);
        if (result.getString("message") != null){
            return new ResponseEntity<>(result.toJSONString(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result.toJSONString(),HttpStatus.OK);

    }

    @GetMapping("/view_replies")
    @ResponseBody
    public ResponseEntity<?> viewReplies(@Validated @RequestParam(value = "post_id") int postId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
//        int userId = 7;
        JSONObject result = projectService.viewReplies(userId,postId);
        if (result.getString("message") != null){
            return new ResponseEntity<>(result.toJSONString(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result.toJSONString(),HttpStatus.OK);
    }

    @PostMapping("/post")
    @ResponseBody
    public ResponseEntity<?> post(@Validated @RequestBody PostRequest request,BindingResult bindingResult){
        JSONObject error = Tool.DealParamError(bindingResult);
        if (error != null){
            return new ResponseEntity<>(error.toJSONString(), HttpStatus.BAD_REQUEST);
        }
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
//        int userId = 7;
        String message = projectService.post(userId,request);
        JSONObject result = new JSONObject();
        result.put("message",message);
        if (message.equals("success")){
            return new ResponseEntity<>(result.toJSONString(),HttpStatus.OK);
        }
        return new ResponseEntity<>(result.toJSONString(),HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/reply")
    @ResponseBody
    public ResponseEntity<?> reply(@Validated @RequestBody ReplyRequest replyRequest,BindingResult bindingResult){
        JSONObject error = Tool.DealParamError(bindingResult);
        if (error != null){
            return new ResponseEntity<>(error.toJSONString(), HttpStatus.BAD_REQUEST);
        }
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
//        int userId = 7;
        String message = projectService.reply(userId,replyRequest);
        JSONObject result = new JSONObject();
        result.put("message",message);
        if (message.equals("success")){
            return new ResponseEntity<>(result.toJSONString(),HttpStatus.OK);
        }
        return new ResponseEntity<>(result.toJSONString(),HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/files")
    @ResponseBody
    public ResponseEntity<?> files(@Validated @RequestParam(value = "project_id") int projectId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
//        int userId = 7;
        JSONObject result = projectService.files(userId,projectId);
        if (result.getString("files") != null){
            return new ResponseEntity<>(result.toJSONString(),HttpStatus.OK);
        }
        return new ResponseEntity<>(result.toJSONString(),HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/like")
    @ResponseBody
    public ResponseEntity<?> like(@Validated@RequestParam(value = "post_id")int postId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
//        int userId = 7;
        String message = projectService.like(userId,postId);
        JSONObject result = new JSONObject();
        result.put("message",message);
        if (message.equals("success")){
            return new ResponseEntity<>(result.toJSONString(),HttpStatus.OK);
        }
        return new ResponseEntity<>(result.toJSONString(),HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/add_project")
    @ResponseBody
    public ResponseEntity<?> addProject(@Validated @RequestParam(value = "project_id") int projectId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
//        int userId = 10;
        String message = projectService.addProject(userId, projectId);
        JSONObject result = new JSONObject();
        result.put("message",message);
        if (message.equals("success")){
            return new ResponseEntity<>(result.toJSONString(),HttpStatus.OK);
        }
        return new ResponseEntity<>(result.toJSONString(),HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/upload_file")
    @ResponseBody
    public ResponseEntity<?> uploadFile(@Validated @RequestParam(value = "file")MultipartFile file, @Validated @RequestParam(value = "Project_id") int projectId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
//        int userId = 10;\
        JSONObject message = new JSONObject();
        if (file == null){
            message.put("message","A PDF file is required");
            return new ResponseEntity<>(message.toJSONString(),HttpStatus.BAD_REQUEST);
        }
        float size = Float.parseFloat(String.valueOf(file.getSize())) / 1024;
        BigDecimal b = new BigDecimal(size);
        // 2表示2位 ROUND_HALF_UP表明四舍五入，
        size = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        if (size > 1024){
            message.put("message","file is too large");
            return new ResponseEntity<>(message.toJSONString(),HttpStatus.BAD_REQUEST);
        }
        String result = projectService.uploadFile(userId,file,projectId);
        message.put("message",result);
        if (result.equals("success")){
            return new ResponseEntity<>(message.toJSONString(),HttpStatus.OK);
        }
        return new ResponseEntity<>(message.toJSONString(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/teacher_score")
    @ResponseBody
    public ResponseEntity<?> teacherScore(@Validated @RequestParam(value = "project_id") int projectId,@Validated @RequestParam(value = "student_id")int studentId,@Validated @RequestParam(value = "score")int score){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
//        int userId = 7;
        String message = projectService.teacherScore(userId,projectId,studentId,score);
        JSONObject result = new JSONObject();
        result.put("message",message);
        if (message.equals("success")){
            return new ResponseEntity<>(result.toJSONString(),HttpStatus.OK);
        }
        return new ResponseEntity<>(result.toJSONString(),HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/view_score")
    @ResponseBody
    public ResponseEntity<?> viewScore(@Validated @RequestParam(value = "project_id") int projectId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
//        int userId = 1;
        JSONObject result = projectService.viewScore(userId,projectId);
        if (result.getString("message") != null){
            return new ResponseEntity<>(result.toJSONString(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result.toJSONString(),HttpStatus.OK);
    }

    @GetMapping("/mutual_evaluation")
    @ResponseBody
    public ResponseEntity<?> mutualEvaluation(@Validated @RequestParam(value = "project_id") int projectId,@Validated @RequestParam(value = "student_id")int studentId,@Validated @RequestParam(value = "score")int score){
//        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        int userId = 6;
        String message = projectService.mutualEvaluation(userId,projectId,studentId,score);
        JSONObject result = new JSONObject();
        result.put("message",message);
        if (message.equals("success")){
            return new ResponseEntity<>(result.toJSONString(),HttpStatus.OK);
        }
        return new ResponseEntity<>(result.toJSONString(),HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/self_evaluation")
    @ResponseBody
    public ResponseEntity<?> selfEvaluation(@Validated @RequestParam(value = "project_id") int projectId,@Validated @RequestParam(value = "score")int score){
//        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        int userId = 1;
        String message = projectService.selfEvaluation(userId,projectId,score);
        JSONObject result = new JSONObject();
        result.put("message",message);
        if (message.equals("success")){
            return new ResponseEntity<>(result.toJSONString(),HttpStatus.OK);
        }
        return new ResponseEntity<>(result.toJSONString(),HttpStatus.BAD_REQUEST);
    }
}
