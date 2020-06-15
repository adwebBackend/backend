package fudan.se.project.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fudan.se.project.service.AuthService;
import fudan.se.project.service.JwtUserDetailsService;
import fudan.se.project.controller.request.LoginRequest;
import fudan.se.project.controller.request.RegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import fudan.se.project.tool.Tool;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONException;


@RestController
@Table(name = "authority")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Validated
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/send_email", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> send_email(@Validated @RequestParam(value = "email") String email) throws JSONException {
        String message = authService.send_email(email);
        return Tool.getResponseEntity(message);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/forget_email", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> forget_email(@Validated @RequestParam(value = "email") String email) throws JSONException {
        String message = authService.forget_email(email);
        return Tool.getResponseEntity(message);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/register", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> register(@Validated @RequestBody RegisterRequest request, BindingResult bindingResult) throws JSONException {
        JSONObject result = Tool.DealParamError(bindingResult);
        if (result != null){
            return new ResponseEntity<>(result.toJSONString(), HttpStatus.BAD_REQUEST);
        }
        String message = authService.register(request);
        return Tool.getResponseEntity(message);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/forget_password", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> forget_password(@Validated @RequestBody RegisterRequest request, BindingResult bindingResult) throws JSONException {
        JSONObject result = Tool.DealParamError(bindingResult);
        if (result != null){
            return new ResponseEntity<>(result.toJSONString(), HttpStatus.BAD_REQUEST);
        }
        String message = authService.forget_password(request);
        return Tool.getResponseEntity(message);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest request, BindingResult bindingResult) throws JSONException {
        JSONObject result = Tool.DealParamError(bindingResult);
        if (result != null) {
            return new ResponseEntity<>(result.toJSONString(), HttpStatus.BAD_REQUEST);
        }

        JSONObject message = authService.login(request.getEmail(), request.getPassword());

        return new ResponseEntity<>(message.toJSONString(), HttpStatus.OK); //200

    }

    /**
     * This is a function to test your connectivity. (健康测试时，可能会用到它）.
     */
    @GetMapping("/welcome")
    public ResponseEntity<?> welcome() {
        Map<String, String> response = new HashMap<>();
        String message = "Welcome to 2020 AD web project. ";
        response.put("message", message);
        return ResponseEntity.ok(response);
//        RegisterRequest request = new RegisterRequest("17302010033@fudan.edu.cn","zdz");
////        System.out.println(authService.register(request));
//        return ResponseEntity.ok(authService.register(request));
    }

}



