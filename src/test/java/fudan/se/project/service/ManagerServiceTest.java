package fudan.se.project.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringJUnit4ClassRunner.class)
//测试时如果涉及了数据库的操作，那么测试完成后，该操作会回滚，也就是不会改变数据库内容
//@javax.transaction.Transactional(rollbackOn = Exception.class)
@SpringBootTest
class ManagerServiceTest {

    @Autowired
    private ManagerService managerService;

    @Test
    void deleteUser() {
        managerService.deleteUser(30,6);
    }
}