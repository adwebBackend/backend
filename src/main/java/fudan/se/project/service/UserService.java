package fudan.se.project.service;

import com.alibaba.fastjson.JSONObject;
import fudan.se.project.domain.User;
import fudan.se.project.domain.UserRole;
import fudan.se.project.repository.UserRepository;
import fudan.se.project.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
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

    public String modifySignature(int userId,String signature){
        User user = userRepository.findByUserId(userId);
        if (user != null){
            user.setSignature(signature);
            userRepository.save(user);
            return "success";
        }
        else{
            return "failure";
        }
    }

    public String modifyBirthday(int userId,Date birthday){
        User user = userRepository.findByUserId(userId);
        if (user != null){
            user.setBirthday(birthday);
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

    public String modifyAvatar(int userId, String avatarPath){
        User user = userRepository.findByUserId(userId);
        if (user != null){
            user.setAvatar(avatarPath);
            userRepository.save(user);
            return "success";
        }
        return "failure";
    }

    public String viewAvatar(int userId){

        User user = userRepository.findByUserId(userId);
        if (user != null){
//            String origin_filepath = user.getAvatar();
//            String filepath = origin_filepath.replace("\\", "/");
//            File file = new File(filepath);
//
//            byte[] buff = new byte[1024];
//            BufferedInputStream bis = null;
//            ServletOutputStream os;
//            try {
//                os = response.getOutputStream();
//                bis = new BufferedInputStream(new FileInputStream(file));
//                int i = bis.read(buff);
//                while (i != -1) {
//                    os.write(buff, 0, buff.length);
//                    os.flush();
//                    i = bis.read(buff);
//                }
//                os.close();
//            }
//            catch (IOException e) {
//                return "failure";
//            } finally {
//                if (bis != null) {
//                    try {
//                        bis.close();
//                    } catch (IOException e) {
//                        System.out.println(e.getMessage());
//                    }
//                }
//            }
            return user.getAvatar();
        }
        return "failure";
    }

    public String modifyPassword(int userId, JSONObject passObj){
        User user = userRepository.findByUserId(userId);
        if (user != null){
            String newPass = passObj.getString("newPass");
            String oldPass = passObj.getString("oldPass");
            if (newPass == null || oldPass == null){
                return "param error";
            }
            String pass = user.getPassword();
            String checkPass = DigestUtils.md5DigestAsHex(((CharSequence) oldPass).toString().getBytes());
            if (pass.equals(checkPass)){
                String password = DigestUtils.md5DigestAsHex(((CharSequence) newPass).toString().getBytes());
                user.setPassword(password);
                userRepository.save(user);
                return "success";
            }
            return "incorrect old password";
        }
        return "failure";
    }
}
