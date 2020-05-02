package fudan.se.project.service;

import fudan.se.project.exception.UsernameHasBeenRegisteredException;
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

@Service
public class AuthService {
    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;

    @Autowired
    public AuthService(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    public User register(RegisterRequest request) {
        // TODO: Implement the function.
        return null;
    }

    public String login(String username, String password) {
        // TODO: Implement the function.
        return null;
    }


}
