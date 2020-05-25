package fudan.se.project.service;

import com.alibaba.fastjson.JSONObject;
import fudan.se.project.controller.request.ProjectRequest;
import fudan.se.project.domain.*;
import fudan.se.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("Duplicates")
@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private CpInclusionRepository cpInclusionRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeachRepository teachRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PtInclusionRepository ptInclusionRepository;
    @Autowired
    private ParticipateRepository participateRepository;
    @Autowired
    private TaskRepository taskRepository;

    public String createProject(int userId, ProjectRequest request){
        if (authService.checkAuthor("teacher",userId)){
            int courseId = request.getCourse_id();
            String name = request.getName();
            Date startTime = request.getStart_time();
            Date endTime = request.getEnd_time();
            String description = request.getDescription();
            int teacherProportion = request.getTeacher_proportion();
            int selfProportion = request.getSelf_proportion();
            int mutualProportion = request.getMutual_proportion();

            Course course = courseRepository.findByCourseId(courseId);
            if (course == null){
                return "course not found";
            }
            if (startTime.after(endTime)){
                return "startTime should be earlier than endTime";
            }
            if (teacherProportion + selfProportion + mutualProportion != 100){
                return "the sum of teacherProportion and selfProportion and mutualProportion should be 100";
            }
            Project project = new Project(name,description,startTime,endTime,teacherProportion,selfProportion,mutualProportion);
            projectRepository.save(project);
            CpInclusion cpInclusion = new CpInclusion(courseId,project.getProjectId());
            cpInclusionRepository.save(cpInclusion);
            return "success";
        }
        return "failure";
    }

    @Transactional
    public String deleteProject(int userId,int projectId){
        if (authService.checkAuthor("teacher",userId)){
            CpInclusion cpInclusion = cpInclusionRepository.findByProjectId(projectId);
            if (cpInclusion == null){
                return "project not found";
            }
            Teach teach = teachRepository.findByCourseIdAndUserId(cpInclusion.getCourseId(),userId);
            if (teach == null){
                return "this project is not your course's";
            }
            projectRepository.deleteByProjectId(projectId);
            return "success";
        }
        return "failure";
    }

    public JSONObject projectBasicInfo(int userId, int projectId){
        JSONObject result = new JSONObject();
        Project project = projectRepository.findByProjectId(projectId);
        if (project == null){
            result.put("message","project not found");
            return result;
        }
        User user = userRepository.findByUserId(userId);
        if (user != null){

            result.put("project_name",project.getProjectName());
            result.put("introduce",project.getProjectIntroduce());
            result.put("start_time",project.getProjectStartTime());
            result.put("end_time",project.getProjectEndTime());
            result.put("teacher_proportion",project.getTeacherProportion());
            result.put("self_proportion",project.getSelfProportion());
            result.put("mutual_proportion",project.getMutualProportion());
            return result;
        }
        result.put("message","failure");
        return result;
    }

    public JSONObject groupMembers(int userId,int projectId){
        JSONObject result = new JSONObject();
        Project project = projectRepository.findByProjectId(projectId);
        if (project == null){
            result.put("message","project not found");
            return result;
        }
        User user = userRepository.findByUserId(userId);
        if (user != null){
            List<Participate> list = participateRepository.findAllByProjectId(projectId);
            if (list.size()==0){
                result.put("message","this project has no member");
                return result;
            }
            List<JSONObject> others = new ArrayList<>();
            for (Participate participate:list){
                User user1 = userRepository.findByUserId(participate.getUserId());
                if (participate.getIsGroupLeader() == 1){
                    JSONObject leader = new JSONObject();
                    leader.put("student_name",user1.getUsername());
                    leader.put("student_id",user1.getUserId());
                    result.put("group_leader",leader);
                }
                else {
                    JSONObject other = new JSONObject();
                    other.put("student_name",user1.getUsername());
                    other.put("student_id",user1.getUserId());
                    others.add(other);
                }
            }
            result.put("others",others);
            return result;
        }
        result.put("message","failure");
        return result;
    }

    public JSONObject allTasks(int userId, int projectId){
        JSONObject result = new JSONObject();
        Project project = projectRepository.findByProjectId(projectId);
        if (project == null){
            result.put("message","project not found");
            return result;
        }
        Participate participate = participateRepository.findByProjectIdAndUserId(projectId,userId);

        if (!authService.checkAuthor("teacher",userId) && (participate == null || participate.getIsGroupLeader()==0)){
            result.put("message","access deny");
            return result;
        }
        if (authService.checkAuthor("teacher",userId) || participate.getIsGroupLeader() == 1){
            List<PtInclusion> list = ptInclusionRepository.findAllByProjectId(projectId);
            if (list.size()==0){
                result.put("message","this project has no task");
                return result;
            }
            for (PtInclusion ptInclusion:list){
                Task task = taskRepository.findByTaskId(ptInclusion.getTaskId());

            }

        }
        result.put("message","failure");
        return result;
    }
}
