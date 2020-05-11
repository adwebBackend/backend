package fudan.se.project.service;

import fudan.se.project.domain.Student;
import fudan.se.project.exception.UsernameHasBeenRegisteredException;
import fudan.se.project.repository.StudentRepository;
import fudan.se.project.security.jwt.JwtTokenUtil;
import fudan.se.project.domain.User;
import fudan.se.project.repository.AuthorityRepository;
import fudan.se.project.repository.UserRepository;
import fudan.se.project.controller.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public String register(RegisterRequest request) {
        String email = request.getEmail();
        User searchUser = userRepository.findByEmail(email);
        //用户名没有重复
        if(searchUser == null) {
            CharSequence charSequence = request.getPassword();
            String password  = encode_password(charSequence);
            createStudent(email,password);
            return "success";     //success
        }

        return "existed account";     //用户名已存在
    }

    public String login(String username, String password) {
        // TODO: Implement the function.
        return null;
    }

    private String encode_password(CharSequence charSequence) {
        return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
    }

    public void createStudent(String email, String password){
        //新建用户
        User user = new User(email,password,0);
        userRepository.save(user);
        //新建用户角色
        Student student = new Student(user.getUserId());
        studentRepository.save(student);
    }

}
