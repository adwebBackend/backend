package fudan.se.project.service;

import com.alibaba.fastjson.JSONObject;
import fudan.se.project.controller.request.SetTaskRequest;
import fudan.se.project.domain.*;
import fudan.se.project.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

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

    public String setTask(int userId, SetTaskRequest request){
        int projectId = request.getProject_id();
        Project project = projectRepository.findByProjectId(projectId);
        if (project == null){
            return "failure";
        }
        Course course = project.getCourse();

        Participate participate = participateRepository.findByProjectIdAndUserId(projectId,userId);
        if (teachRepository.findByCourseIdAndUserId(course.getCourseId(),userId) != null || participate.getIsGroupLeader()==1){
            Date startTime = request.getStart_time();
            Date endTime = request.getEnd_time();
            if (startTime.after(endTime)){
                return "start_time should be earlier than end_time";
            }
            Task task = new Task(request.getName(),request.getIntroduce(),request.getStart_time(),request.getEnd_time(),request.getImportance());
            taskRepository.save(task);
            PtInclusion ptInclusion = new PtInclusion(task.getTaskId(),projectId);
            ptInclusionRepository.save(ptInclusion);
            for (int studentId:request.getAssign()){
                SelectTask selectTask = new SelectTask(studentId,task.getTaskId(),0);
                selectTaskRepository.save(selectTask);
            }
            return "success";
        }
        return "failure";
    }

    public JSONObject taskCompletion(int userId, int taskId,int projectId) {
        JSONObject result = new JSONObject();
        Project project = projectRepository.findByProjectId(projectId);
        if (project == null){
            result.put("message","failure");
            return result;
        }
        Course course = project.getCourse();

        Participate participate = participateRepository.findByProjectIdAndUserId(projectId, userId);
        if (teachRepository.findByCourseIdAndUserId(course.getCourseId(),userId) != null || participate.getIsGroupLeader() == 1) {
            PtInclusion ptInclusion = ptInclusionRepository.findByTaskIdAndProjectId(taskId,projectId);
            if (ptInclusion == null){
                result.put("message","task not found in this project");
                return result;
            }
            List<SelectTask> list = selectTaskRepository.findAllByTaskId(taskId);
            if (list.size() == 0){
                result.put("message","no one select this task");
                return result;
            }
            List<JSONObject> objectList = new ArrayList<>();
            for (SelectTask selectTask:list){
                JSONObject object = new JSONObject();
                User student = userRepository.findByUserId(selectTask.getUserId());
                object.put("student_id",student.getUserId());
                object.put("student_name",student.getName());
                object.put("accomplished",selectTask.getIsAccomplished());
                objectList.add(object);
            }
            result.put("students",objectList);
            return result;
        }
        result.put("message","failure");
        return result;
    }

    public String supervise(int userId, int taskId, int projectId, int studentId){

        Participate participate = participateRepository.findByProjectIdAndUserId(projectId, userId);
        Project project = projectRepository.findByProjectId(projectId);
        if (project == null){
            return "failure";
        }
        Course course = project.getCourse();
        if (teachRepository.findByCourseIdAndUserId(course.getCourseId(),userId) != null || participate.getIsGroupLeader() == 1) {
            PtInclusion ptInclusion = ptInclusionRepository.findByTaskIdAndProjectId(taskId, projectId);
            if (ptInclusion == null) {
                return  "task not found in this project";
            }
            SelectTask selectTask = selectTaskRepository.findByTaskIdAndUserId(taskId,studentId);
            if (selectTask == null){
                return "student does not select this task";
            }
            if (selectTask.getIsAccomplished()==1){
                return "student has already finished thia task";
            }
            Supervise supervise = new Supervise(userId,studentId,taskId);
            superviseRepository.save(supervise);
            return "success";
        }
        return "failure";
    }

    public String completeTask(int userId, int taskId){
        if (authService.checkAuthor("student",userId)){
            SelectTask selectTask = selectTaskRepository.findByTaskIdAndUserId(taskId,userId);
            if (selectTask == null){
                return "you don't select this task";
            }
            SelectTask newS =new SelectTask();
            BeanUtils.copyProperties(selectTask,newS);
            selectTaskRepository.delete(selectTask);
            newS.setIsAccomplished(1);
            selectTaskRepository.save(newS);
            return "success";
        }
        return "failure";
    }
}
