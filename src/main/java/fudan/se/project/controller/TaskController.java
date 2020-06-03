package fudan.se.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fudan.se.project.controller.request.SetTaskRequest;
import fudan.se.project.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Table;
import javax.swing.*;
import java.io.StringReader;

@SuppressWarnings("Duplicates")
@RestController
@Table(name = "task")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Validated
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping("/set_task")
    @ResponseBody
    public ResponseEntity<?> setTask(@Validated @RequestBody SetTaskRequest request){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
//        int userId = 7;
        JSONObject result = new JSONObject();
        String message = taskService.setTask(userId,request);
        result.put("message",message);
        if (message.equals("success")){
            return new ResponseEntity<>(result.toJSONString(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result.toJSONString(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/task_completion")
    @ResponseBody
    public ResponseEntity<?> taskCompletion(@Validated @RequestParam(value = "task_id") int taskId, @Validated @RequestParam(value = "project_id") int projectId){
                int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
//        int userId = 7;
        JSONObject result = taskService.taskCompletion(userId,taskId,projectId);
        if (result.getString("message") == null){
            return new ResponseEntity<>(result.toJSONString(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result.toJSONString(), HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/supervise")
    @ResponseBody
    public ResponseEntity<?> supervise(@Validated @RequestParam(value = "task_id") int taskId,@Validated@RequestParam(value = "student_id")int studentId, @Validated @RequestParam(value = "project_id") int projectId){
                int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
//        int userId = 7;
        String message = taskService.supervise(userId,taskId,projectId,studentId);
        JSONObject result = new JSONObject();
        result.put("message",message);
        if (message.equals("success")){
            return new ResponseEntity<>(result.toJSONString(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result.toJSONString(), HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/complete_task")
    @ResponseBody
    public ResponseEntity<?> completeTask(@Validated @RequestParam(value = "task_id")int taskId){
                int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
//        int userId = 1;
        String message = taskService.completeTask(userId,taskId);
        JSONObject result = new JSONObject();
        result.put("message",message);
        if (message.equals("success")){
            return new ResponseEntity<>(result.toJSONString(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result.toJSONString(), HttpStatus.BAD_REQUEST);
    }
}
