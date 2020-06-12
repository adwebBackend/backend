package fudan.se.project.controller;

import com.alibaba.fastjson.JSONObject;
import fudan.se.project.controller.request.AddUserRequest;
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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

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
//        int userId = 15;
        String message = managerService.addUser(userId,request);
        JSONObject result = new JSONObject();
        result.put("message",message);
        return Tool.getResponseEntity(result);
    }

    @GetMapping("/delete_user")
    @ResponseBody
    public ResponseEntity<?> deleteUser(@Validated @RequestParam(value = "id") int id){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
//        int userId = 15;
        String message = managerService.deleteUser(userId,id);
        JSONObject result = new JSONObject();
        result.put("message",message);
        return Tool.getResponseEntity(result);
    }

    @PostMapping("/modify_user")
    @ResponseBody
    public ResponseEntity<?> modifyUser(@Validated @RequestParam("id") int id,@Validated @RequestParam(value = "avatar") MultipartFile avatar,@Validated @RequestParam("params") String params) throws IOException {
        JSONObject json= JSONObject.parseObject(params);
        String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (json.getString("email")==null ||json.getString("nickname")==null ||json.getString("name")==null ||json.getInteger("gender")==null ||json.getString("signature")==null || json.getDate("birthday")==null || json.getDate("birthday").after(new Date()) || json.getString("email").matches(regex)){
            return Tool.getErrorJson("parameter error");
        }
        JSONObject message = new JSONObject();
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
//        int userId = 15;
        //检查是否是图片
        BufferedImage bi = ImageIO.read(avatar.getInputStream());
        if (bi == null){
            message.put("message","An image is required");
            return new ResponseEntity<>(message.toJSONString(),HttpStatus.BAD_REQUEST);
        }
        //检查图片大小
        float size = Float.parseFloat(String.valueOf(avatar.getSize())) / 1024;
        BigDecimal b = new BigDecimal(size);
        // 2表示2位 ROUND_HALF_UP表明四舍五入，
        size = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        if (size > 200){
            message.put("message","Image is too large");
            return new ResponseEntity<>(message.toJSONString(),HttpStatus.BAD_REQUEST);
        }

        String path = fileService.saveFile(avatar);
        String result = managerService.modifyUser(userId,id,path,json);
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
        return Tool.getResponseEntity(managerService.allUsers(userId));
    }
}
