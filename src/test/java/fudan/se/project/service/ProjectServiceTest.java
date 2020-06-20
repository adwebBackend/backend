package fudan.se.project.service;

import fudan.se.project.controller.request.PostRequest;
import fudan.se.project.controller.request.ProjectRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
//测试时如果涉及了数据库的操作，那么测试完成后，该操作会回滚，也就是不会改变数据库内容
//@javax.transaction.Transactional(rollbackOn = Exception.class)
@SpringBootTest
class ProjectServiceTest {
    @Autowired
    private ProjectService projectService;

    @Test
    void createProject() throws ParseException {
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date start = dateFormat2.parse("2020-06-19 22:36:01");

        Date end = dateFormat2.parse("2020-06-30 20:36:01");
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        start = sdf.format(start)
//        end = sdf.parse(String.valueOf(end));


        ProjectRequest projectRequest = new ProjectRequest(27,"xx",start,end,"xx",50,20,30);
        projectService.createProject(31,projectRequest);
    }
}