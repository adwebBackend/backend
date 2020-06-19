package fudan.se.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fudan.se.project.controller.request.SetTaskRequest;
import fudan.se.project.service.TaskService;
import fudan.se.project.tool.Tool;
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
import java.text.ParseException;

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
    public ResponseEntity<?> setTask(@Validated @RequestBody SetTaskRequest request) throws ParseException {
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        JSONObject result = new JSONObject();
        String message = taskService.setTask(userId,request);
        result.put("message",message);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/task_completion")
    @ResponseBody
    public ResponseEntity<?> taskCompletion(@Validated @RequestParam(value = "task_id") int taskId) {
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        JSONObject result = taskService.taskCompletion(userId, taskId);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/supervise")
    @ResponseBody
    public ResponseEntity<?> supervise(@Validated @RequestParam(value = "task_id") int taskId,@Validated@RequestParam(value = "student_id")int studentId) {
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        String message = taskService.supervise(userId, taskId, studentId);
        JSONObject result = new JSONObject();
        result.put("message", message);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/complete_task")
    @ResponseBody
    public ResponseEntity<?> completeTask(@Validated @RequestParam(value = "task_id")int taskId) {
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        String message = taskService.completeTask(userId, taskId);
        JSONObject result = new JSONObject();
        result.put("message", message);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/messages")
    @ResponseBody
    public ResponseEntity<?> messages() {
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        JSONObject result = taskService.messages(userId);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/delete_message")
    @ResponseBody
    public ResponseEntity<?> deleteMessage(@Validated @RequestParam(value = "task_id")int taskId, @Validated @RequestParam(value = "superviseUserId")int superviseUserId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        String message = taskService.deleteMessage(userId,taskId,superviseUserId);
        JSONObject result = new JSONObject();
        result.put("message", message);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/read_message")
    @ResponseBody
    public ResponseEntity<?> readMessage(@Validated @RequestParam(value = "task_id")int taskId, @Validated @RequestParam(value = "superviseUserId")int superviseUserId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        JSONObject result = taskService.readMessage(userId,taskId,superviseUserId);
        return Tool.getResponseEntity(result);
    }
}
