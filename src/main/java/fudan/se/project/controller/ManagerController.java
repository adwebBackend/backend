package fudan.se.project.controller;

import com.alibaba.fastjson.JSONObject;
import fudan.se.project.controller.request.AddUserRequest;
import fudan.se.project.controller.request.ModifyUserRequest;
import fudan.se.project.domain.User;
import fudan.se.project.service.FileService;
import fudan.se.project.service.ManagerService;
import fudan.se.project.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.constraints.Max;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@SuppressWarnings("Duplicates")
@RestController
@Validated
public class ManagerController {
    @Autowired
    private ManagerService managerService;
    @Autowired
    private FileService fileService;

    @PostMapping("/add_user")
    @ResponseBody
    public ResponseEntity<?> addUser(@Validated @RequestBody AddUserRequest request, BindingResult bindingResult){
        JSONObject error = Tool.DealParamError(bindingResult);
        if (error != null){
            return new ResponseEntity<>(error.toJSONString(), HttpStatus.BAD_REQUEST);
        }
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        String message = managerService.addUser(userId,request);
        JSONObject result = new JSONObject();
        result.put("message",message);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/delete_user")
    @ResponseBody
    public ResponseEntity<?> deleteUser(@Validated @RequestParam(value = "id") int id){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));

        String message = managerService.deleteUser(userId,id);
        JSONObject result = new JSONObject();
        result.put("message",message);
        return Tool.getResponseEntity(result);
    }

    @PostMapping("/modify_user")
    @ResponseBody
    public ResponseEntity<?> modifyUser(@Validated @RequestBody ModifyUserRequest request,BindingResult bindingResult){
        JSONObject error = Tool.DealParamError(bindingResult);
        if (error != null){
            return new ResponseEntity<>(error.toJSONString(), HttpStatus.BAD_REQUEST);
        }
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        JSONObject message = new JSONObject();
        String result = managerService.modifyUser(userId,request);
        message.put("message",result);
        return Tool.getResponseEntity(message);
    }

    @GetMapping("/view_user")
    @ResponseBody
    public ResponseEntity<?> viewUser(@Validated @RequestParam(value = "id") int id){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        return Tool.getResponseEntity(managerService.viewUser(userId,id));
    }

    @GetMapping("/all_users")
    @ResponseBody
    public ResponseEntity<?> allUsers(){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        List<JSONObject> objects = managerService.allUsers(userId);
        if (objects == null){
            return Tool.getErrorJson("failure");
        }
        JSONObject result = new JSONObject();
        result.put("users",objects);
        return Tool.getResponseEntity(result);
    }
}
