package fudan.se.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fudan.se.project.controller.request.ProjectRequest;
import fudan.se.project.domain.Project;
import fudan.se.project.domain.User;
import fudan.se.project.service.AuthService;
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

import javax.persistence.Table;
import java.util.Date;

@RestController
@Table(name = "project")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Validated
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping("/create_project")
    @ResponseBody
    public ResponseEntity<?> createProject(@Validated @RequestBody ProjectRequest request, BindingResult bindingResult){
        JSONObject result = Tool.DealParamError(bindingResult);
        if (result != null){
            return new ResponseEntity<>(result.toJSONString(), HttpStatus.BAD_REQUEST);
        }
//        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        int userId = 7;
        String message = projectService.createProject(userId,request);
        if (message.equals("success")){
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/delete_project")
    @ResponseBody
    public ResponseEntity<?> deleteProject(@Validated @RequestParam(value = "project_id") int projectId){
//        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        int userId = 7;
        String message = projectService.deleteProject(userId,projectId);
        if (message.equals("success")){
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/project_basic_info")
    @ResponseBody
    public ResponseEntity<?> projectBasicInfo(@Validated @RequestParam(value = "project_id") int projectId){
//        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        int userId = 7;
        JSONObject result = projectService.projectBasicInfo(userId,projectId);
        if (result.getString("message") != null){
            return new ResponseEntity<>(result.toJSONString(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result.toJSONString(),HttpStatus.OK);
    }

    @GetMapping("/group_members")
    @ResponseBody
    public ResponseEntity<?> groupMembers(@Validated @RequestParam(value = "project_id") int projectId){
//        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        int userId = 7;
        JSONObject result = projectService.groupMembers(userId,projectId);
        if (result.getString("message") != null){
            return new ResponseEntity<>(result.toJSONString(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result.toJSONString(),HttpStatus.OK);

    }
}
