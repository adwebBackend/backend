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
import java.util.HashMap;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONException;


@RestController
@Table(name = "user")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Validated
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @CrossOrigin(origins = "*")
    @PostMapping(value = "/register", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> register(@Validated @RequestBody RegisterRequest request, BindingResult bindingResult) throws JSONException {
        JSONObject result = Tool.DealParamError(bindingResult);
        if (result != null){
            return new ResponseEntity<>(result.toJSONString(), HttpStatus.OK);
        }
        String message = authService.register(request);
        return Tool.getResponseEntity(message);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request.getUsername(), request.getPassword()));
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
    }

}



