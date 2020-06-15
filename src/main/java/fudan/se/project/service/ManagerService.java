package fudan.se.project.service;

import com.alibaba.fastjson.JSONObject;
import fudan.se.project.controller.request.AddUserRequest;
import fudan.se.project.controller.request.ModifyUserRequest;
import fudan.se.project.domain.Role;
import fudan.se.project.domain.User;
import fudan.se.project.domain.UserRole;
import fudan.se.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ManagerService {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private TeachRepository teachRepository;
    @Autowired
    private TakeRepository takeRepository;
    @Autowired
    private EvaluateRepository evaluateRepository;
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private ParticipateRepository participateRepository;
    @Autowired
    private SelectTaskRepository selectTaskRepository;
    @Autowired
    private SuperviseRepository superviseRepository;
    @Autowired
    private UploadRepository uploadRepository;
    @Autowired
    private UserPostRepository userPostRepository;
    @Autowired
    private UserReplyRepository userReplyRepository;

    public String addUser(int userId, AddUserRequest request){
        if (authService.checkAuthor("admin",userId)){
            User existUser = userRepository.findByEmail(request.getEmail());
            if (existUser != null){
                return "existed account";
            }
            String password = DigestUtils.md5DigestAsHex(((CharSequence) request.getPassword()).toString().getBytes());
            User user = new User(request.getEmail(),password);
            userRepository.save(user);
            Role role = roleRepository.findByRoleName("teacher");
            UserRole userRole = new UserRole(user,role);
            userRoleRepository.save(userRole);
            return "success";
        }
        return "failure";
    }

    @Transactional
    public String deleteUser(int userId, int id){
        if (authService.checkAuthor("admin",userId)){
            User user = userRepository.findByUserId(id);
            if (user == null){
                return "user not found";
            }
            userRoleRepository.deleteAllByUser(user);
            evaluateRepository.deleteAllByEvaluateUserIdOrEvaluatedUserId(id,id);
            likesRepository.deleteAllByUerId(id);
            participateRepository.deleteAllByUserId(id);
            selectTaskRepository.deleteAllByUserId(id);
            superviseRepository.deleteAllBySuperviseUserIdOrSupervisedUserId(id,id);
            teachRepository.deleteAllByUserId(id);
            takeRepository.deleteAllByUserId(id);
            uploadRepository.deleteAllByUserId(id);
            userPostRepository.deleteAllByUserId(id);
            userReplyRepository.deleteAllByUserId(id);
            userRepository.delete(user);
            return "success";
        }
        return "failure";
    }

//    public String modifyUser(int userId, int id, String avatarPath, JSONObject params){
//        if (authService.checkAuthor("admin",userId)){
//            User user = userRepository.findByUserId(id);
//            if (user == null){
//                return "user not found";
//            }
//            user.setAvatar(avatarPath);
//            user.setEmail(params.getString("email"));
//            user.setName(params.getString("name"));
//            user.setGender(params.getInteger("gender"));
//            user.setNickName(params.getString("nickname"));
//            user.setSignature(params.getString("signature"));
//            user.setBirthday(params.getDate("birthday"));
//            userRepository.save(user);
//            return "success";
//        }
//        return "failure";
//    }
    public String modifyUser(int userId, ModifyUserRequest request){
        if (authService.checkAuthor("admin",userId)){
            User user = userRepository.findByUserId(request.getId());
            if (user == null){
                return "user not found";
            }
            user.setName(request.getName());
            user.setGender(request.getGender());
            user.setNickName(request.getNickname());
            user.setSignature(request.getSignature());
            user.setBirthday(request.getBirthday());
            userRepository.save(user);
            return "success";
        }
        return "failure";
    }

    public JSONObject viewUser(int userId, int id){
        JSONObject result = new JSONObject();
        if (authService.checkAuthor("admin",userId)){
            User user = userRepository.findByUserId(id);
            if (user == null){
                result.put("message","user not found");
                return result;
            }
            result.put("name",user.getName());
            result.put("email",user.getEmail());
            result.put("gender",user.getGender());
            result.put("birthday",user.getBirthday());
            result.put("nickname",user.getNickName());
            result.put("signature",user.getSignature());
            result.put("avatar",user.getAvatar());
            return result;
        }
        result.put("message","failure");
        return result;
    }

    public List<JSONObject> allUsers(int userId){

        List<JSONObject> objects = new ArrayList<>();
        if (authService.checkAuthor("admin",userId)){
            List<User> users = (List<User>) userRepository.findAll();
            for (User user:users){
                JSONObject result = new JSONObject();
                result.put("name",user.getName());
                result.put("email",user.getEmail());
                result.put("gender",user.getGender());
                result.put("nickname",user.getNickName());
                result.put("userId",user.getUserId());
                objects.add(result);
            }
        }
        return objects;
    }
}
