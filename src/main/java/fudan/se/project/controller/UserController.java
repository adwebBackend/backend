package fudan.se.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fudan.se.project.domain.User;
import fudan.se.project.service.UserService;
import fudan.se.project.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
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
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
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
}
