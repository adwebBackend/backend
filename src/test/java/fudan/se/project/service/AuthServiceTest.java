package fudan.se.project.service;

import fudan.se.project.controller.request.RegisterRequest;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


//系统你执行测试方法时，调用者是SpringRunnerClass类
@RunWith(SpringJUnit4ClassRunner.class)
//测试时如果涉及了数据库的操作，那么测试完成后，该操作会回滚，也就是不会改变数据库内容
//@javax.transaction.Transactional(rollbackOn = Exception.class)
@SpringBootTest
class AuthServiceTest extends AbstractJUnit4SpringContextTests {
    @Autowired
    private AuthService authService;
    @Test
    void register() {
        RegisterRequest request = new RegisterRequest("aaa.qq.com", "123456",1234);
        System.out.println(authService.register(request));
    }

    @Test
    void send_email() {
        Long last=new Date().getTime();
        for (int i=0;i<20;i++){
            while (new Date().getTime()-last<1000){
            }
            last=new Date().getTime();
            System.out.println(authService.send_email("1801859073@1632.com"));
        }
    }

    @Test
    void createStudent() {
        authService.createStudent("vvv","bbb");
    }
}