package fudan.se.project.service;

import fudan.se.project.domain.User;
import fudan.se.project.domain.UserRole;
import fudan.se.project.repository.UserRepository;
import fudan.se.project.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public String modifyNickName(int userId,String nickName){
        User user = userRepository.findByUserId(userId);
        if (user != null){
            user.setNickName(nickName);
            userRepository.save(user);
            return "success";
        }
        else{
            return "failure";
        }
    }

    public String modifyGender(int userId,int gender){
        User user = userRepository.findByUserId(userId);
        if (user != null){
            user.setGender(gender);
            userRepository.save(user);
            return "success";
        }
        else{
            return "failure";
        }
    }

    public String modifyName(int userId,String name){
        User user = userRepository.findByUserId(userId);
        if (user != null){
            if (user.getName() != null){
                return "failure";
            }
            user.setName(name);
            userRepository.save(user);
            return "success";
        }
        else{
            return "failure";
        }
    }

    public User viewPersonalInfo(int userId){
        return userRepository.findByUserId(userId);
    }
}
