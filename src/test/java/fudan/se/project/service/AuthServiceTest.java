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
        RegisterRequest request = new RegisterRequest("aaa.qq.com", "123456");
        System.out.println(authService.register(request));
    }

    @Test
    void createStudent() {
        authService.createStudent("vvv","bbb");
    }
}