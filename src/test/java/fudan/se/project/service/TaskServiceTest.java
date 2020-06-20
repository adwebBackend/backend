package fudan.se.project.service;

import fudan.se.project.controller.request.SetTaskRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
//测试时如果涉及了数据库的操作，那么测试完成后，该操作会回滚，也就是不会改变数据库内容
//@javax.transaction.Transactional(rollbackOn = Exception.class)
@SpringBootTest
class TaskServiceTest {
    @Autowired
    private TaskService taskService;

    @Test
    void setTask() throws ParseException {
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date start = dateFormat2.parse("2020-06-21 22:36:01");

        Date end = dateFormat2.parse("2020-06-22 20:36:01");

        SetTaskRequest setTaskRequest = new SetTaskRequest(22,"xx",start,end,"xx",1,new ArrayList<>());

        taskService.setTask(10,setTaskRequest);
    }
}