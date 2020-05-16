package fudan.se.project.service;

import fudan.se.project.domain.Role;
import fudan.se.project.domain.UserRole;
import fudan.se.project.repository.RoleRepository;
import fudan.se.project.repository.UserRoleRepository;
import fudan.se.project.security.jwt.JwtTokenUtil;
import fudan.se.project.domain.User;
import fudan.se.project.repository.UserRepository;
import fudan.se.project.controller.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    Random random = new Random(new Date().getTime());

    private ArrayList<String> emails=new ArrayList<>();
    private ArrayList<Integer> codes=new ArrayList<>();
    private ArrayList<Long> timestamps=new ArrayList<>();

    private int expired_time=900000;//15min

    public String send_email(String email){
        return send(email,0);
    }

    public String send(String email,int operate){
        User user=userRepository.findByEmail(email);
        System.out.println(user);
        if (operate==0&&user!=null){
            return "existed account";
        }
        if (operate==1&&user==null){
            return "account does not exist";
        }
        //生成四位验证码
        int code = random.nextInt(9000)+1000;
        int index=emails.indexOf(email);

        if (index!=-1&&new Date().getTime()-timestamps.get(index)<60000){
            return "Operate too frequently";
        }
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", "smtp.qq.com");   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");

        final String smtpPort = "465";
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", smtpPort);
        Session session = Session.getInstance(props);
        session.setDebug(true);
        try {
            MimeMessage message = createSimpleMail(session, email, code);
            Transport transport = session.getTransport();
            transport.connect("3223826042@qq.com", "vbedktrimapkchij");
            transport.sendMessage(message, message.getAllRecipients());

            transport.close();
            int found=0;
            for (int i=0;i<timestamps.size();i++){
                if (emails.get(i).equals(email)){
                    codes.set(i,code);
                    timestamps.set(i,new Date().getTime());
                    found=1;
                }
                if (new Date().getTime()-timestamps.get(i)>expired_time){
                    emails.remove(i);
                    codes.remove(i);
                    timestamps.remove(i);
                }
            }
            if (found==0) {
                emails.add(email);
                codes.add(code);
                timestamps.add(new Date().getTime());
            }
        }catch (MessagingException e){
            e.printStackTrace();
            return "send failed";
        }
        return "send successfuly";
    }


    public String forget_email(String email){
        send(email,1);
        return null;
    }


    public String register(RegisterRequest request) {
        String email = request.getEmail();
        User searchUser = userRepository.findByEmail(email);

        //用户名没有重复
        if(searchUser == null) {
            int index=emails.indexOf(email);
            if (index==-1){
                return "Verification code error";
            }else {
                if (codes.get(index)!=request.getCode()||new Date().getTime()-timestamps.get(index)>expired_time){
                    return "Verification code error";
                }
            }
            CharSequence charSequence = request.getPassword();
            String password  = encode_password(charSequence);
            createStudent(email,password);
            return "success";     //success
        }
        return "existed account";     //用户名已存在
    }

    public String forget_password(RegisterRequest request) {
        String email = request.getEmail();
        User searchUser = userRepository.findByEmail(email);

        if(searchUser != null) {
            int index=emails.indexOf(email);
            if (index==-1){
                return "Verification code error";
            }else {
                if (codes.get(index)!=request.getCode()||new Date().getTime()-timestamps.get(index)>expired_time){
                    return "Verification code error";
                }
            }
            CharSequence charSequence = request.getPassword();
            String password  = encode_password(charSequence);
            searchUser.setPassword(password);
            userRepository.save(searchUser);
            return "change password success";     //success
        }
        return "email not found";     //用户名已存在
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if(user != null) {
            if(user.getPassword().equals(encode_password(password))){
                //签发jwt
                String token = jwtTokenUtil.generateToken(user);
                long role=0;
                Iterator<UserRole> iterator=user.getUserRoles().iterator();
                while (iterator.hasNext()){
                    role=iterator.next().getRole().getId();
                }
                return "success"+role+token;
            }else{
                //       log.warn("log failed");
                return "wrong password";    //wrong password
            }
        }
        else
            return "email not found";
    }

    private String encode_password(CharSequence charSequence) {
        return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
    }

    public void createStudent(String email, String password){
        //新建用户
        User user = new User(email,password);
        userRepository.save(user);
        Role role = roleRepository.findByRoleName("student");
        UserRole userRole = new UserRole(user,role);
        userRoleRepository.save(userRole);
    }

    public static MimeMessage createSimpleMail(Session session,String email,int code)
            throws MessagingException {
        // 创建邮件对象
        MimeMessage message = new MimeMessage(session);
        // 指明发件人
        message.setFrom(new InternetAddress("3223826042@qq.com"));
        // 指明收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        // 邮件的标题
        message.setSubject("验证码");
        // 邮件的文本内容
        message.setContent("【FDPBL】"+code+"(注册验证码)，此验证码只用于注册你的FDPBL账号，有效期15分钟，请勿将验证码泄露给他人。", "text/html;charset=UTF-8");
        return message;
    }
}
