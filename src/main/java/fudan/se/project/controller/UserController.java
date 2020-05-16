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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;

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
    public ResponseEntity<?> modifyNickName(String nickName){
        int userId = Integer.parseInt((((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        return ResponseEntity.ok(userService.modifyNickName(userId,nickName));
    }

    @GetMapping("modify_gender")
    @ResponseBody
    public ResponseEntity<?> modifyGender(int gender){
        int userId = Integer.parseInt((((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        return ResponseEntity.ok(userService.modifyGender(userId,gender));
    }

    @GetMapping("modify_name")
    @ResponseBody
    public ResponseEntity<?> modifyName(String name){
        int userId = Integer.parseInt((((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        return ResponseEntity.ok(userService.modifyName(userId,name));
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
        result.put("message","success");
        return new ResponseEntity<>(result.toJSONString(),HttpStatus.OK);
    }
}
