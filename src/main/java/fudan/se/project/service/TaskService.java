package fudan.se.project.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import fudan.se.project.controller.request.SetTaskRequest;
import fudan.se.project.domain.*;
import fudan.se.project.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private ParticipateRepository participateRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private PtInclusionRepository ptInclusionRepository;
    @Autowired
    private SelectTaskRepository selectTaskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SuperviseRepository superviseRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TeachRepository teachRepository;
    @Autowired
    private CourseRepository courseRepository;

    public String setTask(int userId, SetTaskRequest request){
        int projectId = request.getProject_id();
        Project project = projectRepository.findByProjectId(projectId);
        if (project == null){
            return "failure";
        }
        Course course = project.getCourse();

        Participate participate = participateRepository.findByProjectIdAndUserId(projectId,userId);
        if (teachRepository.findByCourseIdAndUserId(course.getCourseId(),userId) != null || (participate!=null && participate.getIsGroupLeader()==1)){
            Date startTime = request.getStart_time();
            Date endTime = request.getEnd_time();
            if (startTime.getTime()<new Date().getTime()||startTime.after(endTime)){
                return "failure";
            }
            Task task = new Task(request.getName(),request.getIntroduce(),request.getStart_time(),request.getEnd_time(),request.getImportance());
            taskRepository.save(task);
            PtInclusion ptInclusion = new PtInclusion(task.getTaskId(),projectId);
            ptInclusionRepository.save(ptInclusion);
            for (int studentId:request.getAssign()){
                if (participateRepository.findByProjectIdAndUserId(projectId,studentId)!=null) {
                    SelectTask selectTask = new SelectTask(studentId, task.getTaskId(), 0);
                    selectTaskRepository.save(selectTask);
                }
            }
            return "success";
        }
        return "failure";
    }

    public JSONObject taskCompletion(int userId, int taskId) {
        JSONObject result = new JSONObject();
        PtInclusion ptInclusion = ptInclusionRepository.findByTaskId(taskId);
        if (ptInclusion == null) {
            result.put("message", "failure");
            return result;
        }
        Project project = projectRepository.findByProjectId(ptInclusion.getProjectId());
        Teach teach = teachRepository.findByCourseId(project.getCourse().getCourseId());
        Participate participate = participateRepository.findByProjectIdAndUserId(project.getProjectId(), userId);
        if (teach.getUserId() !=userId && (participate == null || participate.getIsGroupLeader() == 0)) {
            result.put("message", "failure");
            return result;
        }

        List<SelectTask> list = selectTaskRepository.findAllByTaskId(taskId);
        JSONArray objectList = new JSONArray();
        for (SelectTask selectTask : list) {
            JSONObject object = new JSONObject();
            User student = userRepository.findByUserId(selectTask.getUserId());
            object.put("student_id", student.getUserId());
            object.put("student_name", student.getName());
            object.put("accomplished", selectTask.getIsAccomplished());
            objectList.add(object);
        }
        result.put("students", objectList);
        return result;
    }

    public String supervise(int userId, int taskId, int studentId) {
        PtInclusion ptInclusion = ptInclusionRepository.findByTaskId(taskId);
        if (ptInclusion == null) {
            return "failure";
        }
        Project project = projectRepository.findByProjectId(ptInclusion.getProjectId());
        Teach teach = teachRepository.findByCourseId(project.getCourse().getCourseId());
        Participate participate = participateRepository.findByProjectIdAndUserId(project.getProjectId(), userId);
        if (teach.getUserId() != userId && (participate == null || participate.getIsGroupLeader() == 0)) {
            return "failure";
        }

        SelectTask selectTask = selectTaskRepository.findByTaskIdAndUserId(taskId, studentId);
        if (selectTask == null || selectTask.getIsAccomplished() == 1) {
            return "failure";
        }

        Supervise supervise=superviseRepository.findBySupervisedUserIdAndSuperviseUserIdAndTaskId(studentId,userId,taskId);
        if (supervise==null){
            supervise = new Supervise(userId, studentId, taskId);
            superviseRepository.save(supervise);
        }else {
            Supervise newS = new Supervise();
            BeanUtils.copyProperties(supervise, newS);
            newS.setIsRead(0);
            superviseRepository.delete(supervise);
            superviseRepository.save(newS);
        }
        return "success";
    }

    public String completeTask(int userId, int taskId) {
        SelectTask selectTask = selectTaskRepository.findByTaskIdAndUserId(taskId, userId);
        if (selectTask == null||selectTask.getIsAccomplished()==1) {
            return "failure";
        }

        SelectTask newS = new SelectTask();
        BeanUtils.copyProperties(selectTask, newS);
        selectTaskRepository.delete(selectTask);
        newS.setIsAccomplished(1);
        selectTaskRepository.save(newS);
        return "success";
    }

    public JSONObject messages(int userId) {
        JSONObject result = new JSONObject();
        List<Supervise> supervises=superviseRepository.findBySupervisedUserId(userId);
        JSONArray array=new JSONArray();
        for (Supervise supervise:supervises){

            JSONObject object = new JSONObject();
            User superviseUser = userRepository.findByUserId(supervise.getSuperviseUserId());
            Task task=taskRepository.findByTaskId(supervise.getTaskId());
            Project project=projectRepository.findByProjectId(task.getProject().getProjectId());
            Course course=courseRepository.findByCourseId(project.getCourse().getCourseId());
            object.put("superviseUserId", superviseUser.getUserId());
            object.put("superviseUserName", superviseUser.getName());
            object.put("superviseUserAvatar", superviseUser.getAvatar());
            object.put("isRead",supervise.getIsRead());
            object.put("taskName", task.getTaskName());
            object.put("taskIntroduce", task.getTaskIntroduce());
            object.put("projectName", project.getProjectName());
            object.put("projectIntroduce", project.getProjectIntroduce());
            object.put("courseName", course.getCourseName());
            object.put("courseIntroduce", course.getCourseIntroduce());
            array.add(object);
            if (supervise.getIsRead()==0) {
                Supervise newS = new Supervise();
                BeanUtils.copyProperties(supervise, newS);
                newS.setIsRead(1);
                superviseRepository.delete(supervise);
                superviseRepository.save(newS);
            }
        }
        result.put("supervises",array);
        return result;
    }

    @Transactional
    public String deleteMessage(int userId,int taskId,int superviseUserId){
        Supervise supervise = superviseRepository.findBySupervisedUserIdAndSuperviseUserIdAndTaskId(userId,superviseUserId,taskId);
        if (supervise != null){
            superviseRepository.delete(supervise);
            return "success";
        }
        return "failure";
    }

}
