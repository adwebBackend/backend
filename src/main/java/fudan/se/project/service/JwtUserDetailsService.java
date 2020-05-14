package fudan.se.project.service;

import fudan.se.project.domain.User;
import fudan.se.project.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Implement the function.

        User user = userRepository.findByEmail(username);
        if (user == null){
            throw new UsernameNotFoundException("User: '" + username + "' not found.");
        }
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) user.getAuthorities();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        /*考虑用户权限
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Authority authority = loadUser.getAuthority();
        authorityRepository.add(new SimpleGrantedAuthority(authority.getAuthority()));
        return new User(username, loadUser.getPassword(), authorityRepository);
         */
    }
}
