package fudan.se.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fudan.se.project.domain.User;
import fudan.se.project.service.CourseService;
import fudan.se.project.service.FileService;
import fudan.se.project.service.UserService;
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
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@RestController
@Table(name = "user")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Validated
public class UserController {

    private UserService userService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private FileService fileService;

    @Autowired
    public UserController(UserService userService){this.userService = userService;}

    @GetMapping("modify_nickname")
    @ResponseBody
    public ResponseEntity<?> modifyNickName(@Validated @RequestParam(value = "nickname") String nickName){
        if (nickName==null||nickName.equals("")||nickName.length()>30){
            return Tool.getErrorJson("parameter error");
        }
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        return ResponseEntity.ok(userService.modifyNickName(userId,nickName));
    }

    @GetMapping("modify_gender")
    @ResponseBody
    public ResponseEntity<?> modifyGender(@Validated @RequestParam(value = "gender") int gender){
        if (gender!=0&&gender!=1){
            return Tool.getErrorJson("parameter error");
        }
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        return ResponseEntity.ok(userService.modifyGender(userId,gender));
    }

    @GetMapping("modify_name")
    @ResponseBody
    public ResponseEntity<?> modifyName(@Validated @RequestParam(value = "name") String name){
        if (name==null||name.equals("")||name.length()>30){
            return Tool.getErrorJson("parameter error");
        }
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        return ResponseEntity.ok(userService.modifyName(userId,name));
    }

    @GetMapping("modify_signature")
    @ResponseBody
    public ResponseEntity<?> modifySignature(@Validated @RequestParam(value = "signature") String signature){
        if (signature==null||signature.equals("")||signature.length()==0||signature.length()>60){
            return Tool.getErrorJson("parameter error");
        }
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        return ResponseEntity.ok(userService.modifySignature(userId,signature));
    }

    @GetMapping("modify_birthday")
    @ResponseBody
    public ResponseEntity<?> modifyBirthday(@Validated @RequestParam(value = "birthday") String birthday) {
        birthday = birthday.replace("GMT", "").replaceAll("\\(.*\\)", "");
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss", Locale.ENGLISH);
        Date bir;
        try {
            bir = format.parse(birthday);
            if (bir.getTime()>=new Date().getTime()){
                return Tool.getErrorJson("parameter error");
            }
            int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
            return ResponseEntity.ok(userService.modifyBirthday(userId,bir));
        }catch (ParseException e){
            return Tool.getErrorJson("parameter error");
        }
    }

    @GetMapping("view_personal_info")
    @ResponseBody
    public ResponseEntity<?> viewPersonalInfo(){
        int userId = Integer.parseInt((((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        User user = userService.viewPersonalInfo(userId);
        JSONObject result = new JSONObject();
        if (user == null){
            result.put("message","failure");
            return new ResponseEntity<>(result.toJSONString(), HttpStatus.BAD_REQUEST);
        }
        result.put("email",user.getEmail());
        result.put("nickname",user.getNickName());
        result.put("name",user.getUsername());
        result.put("gender",user.getGender());
        result.put("signature",user.getSignature());
        result.put("birthday",user.getBirthday());
        result.put("message","success");
        return new ResponseEntity<>(result.toJSONString(),HttpStatus.OK);
    }

    @PostMapping("/modify_avatar")
    @ResponseBody
    public ResponseEntity<?> modifyAvatar(@Validated @RequestParam(value = "avatar") MultipartFile avatar) throws IOException {
        JSONObject message = new JSONObject();
        //检查是否是图片
        BufferedImage bi = ImageIO.read(avatar.getInputStream());
        if (bi == null){
            message.put("message","An image is required");
            return new ResponseEntity<>(message.toJSONString(),HttpStatus.BAD_REQUEST);
        }
        //检查图片大小
        if (avatar.getSize() > 100){
            message.put("message","Image is too large");
            return new ResponseEntity<>(message.toJSONString(),HttpStatus.BAD_REQUEST);
        }
        int userId = Integer.parseInt((((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        String path = fileService.saveFile(avatar);
        String result = userService.modifyAvatar(userId,path);
        if (result.equals("success")){
            return new ResponseEntity<>(result,HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/view_avatar")
    @ResponseBody
    public ResponseEntity<?> viewAvatar(){
        int userId = Integer.parseInt((((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        String result = userService.viewAvatar(userId);
        if (result.equals("failure"))
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
