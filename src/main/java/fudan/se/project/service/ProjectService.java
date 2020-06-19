package fudan.se.project.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import fudan.se.project.controller.request.PostRequest;
import fudan.se.project.controller.request.ProjectRequest;
import fudan.se.project.controller.request.ReplyRequest;
import fudan.se.project.domain.*;
import fudan.se.project.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;

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
    @Autowired
    private SelectTaskRepository selectTaskRepository;
    @Autowired
    private UserPostRepository userPostRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private UserReplyRepository userReplyRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private UploadRepository uploadRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private EvaluateRepository evaluateRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private TakeRepository takeRepository;


    public String createProject(int userId, ProjectRequest request) {
        if (teachRepository.findByCourseIdAndUserId(request.getCourse_id(), userId) == null) {
            return "failure";
        }

        int courseId = request.getCourse_id();
        String name = request.getName();
        Date startTime = request.getStart_time();
        Date endTime = request.getEnd_time();
        String description = request.getDescription();
        int teacherProportion = request.getTeacher_proportion();
        int selfProportion = request.getSelf_proportion();
        int mutualProportion = request.getMutual_proportion();

        Course course = courseRepository.findByCourseId(courseId);
        if (course == null) {
            return "course not found";
        }
        if (startTime.before(course.getCourseStartTime()) || endTime.after(course.getCourseEndTime()) || startTime.after(endTime)){
            return "Time parameter error";
        }
        if (teacherProportion + selfProportion + mutualProportion != 100) {
            return "the sum of teacherProportion and selfProportion and mutualProportion should be 100";
        }
        Project project = new Project(name, description, startTime, endTime, teacherProportion, selfProportion, mutualProportion);
        projectRepository.save(project);
        CpInclusion cpInclusion = new CpInclusion(courseId, project.getProjectId());
        cpInclusionRepository.save(cpInclusion);
        return "success";
    }

    @Transactional
    public String deleteProject(int userId,int projectId) {
        CpInclusion cpInclusion = cpInclusionRepository.findByProjectId(projectId);
        if (cpInclusion == null) {
            return "project not found";
        }

        if (teachRepository.findByCourseIdAndUserId(cpInclusion.getCourseId(), userId) == null) {
            return "failure";
        }

        cpInclusionRepository.deleteAllByProjectId(projectId);
        return "success";
    }

    public JSONObject projectBasicInfo(int userId, int projectId) {
        JSONObject result = new JSONObject();
        CpInclusion cpInclusion = cpInclusionRepository.findByProjectId(projectId);
        if (cpInclusion == null) {
            result.put("message", "failure");
            return result;
        }
        if (takeRepository.findByCourseIdAndUserId(cpInclusion.getCourseId(), userId) == null&&teachRepository.findByCourseIdAndUserId(cpInclusion.getCourseId(), userId)==null) {
            result.put("message", "failure");
            return result;
        }

        Project project=projectRepository.findByProjectId(projectId);
        result.put("project_name", project.getProjectName());
        result.put("introduce", project.getProjectIntroduce());
        result.put("start_time", project.getProjectStartTime());
        result.put("end_time", project.getProjectEndTime());
        result.put("teacher_proportion", project.getTeacherProportion());
        result.put("self_proportion", project.getSelfProportion());
        result.put("mutual_proportion", project.getMutualProportion());
        result.put("status", project.getStatus());
        return result;
    }

    public JSONObject groupMembers(int userId,int projectId){
        JSONObject result = new JSONObject();

        CpInclusion cpInclusion=cpInclusionRepository.findByProjectId(projectId);
        if (cpInclusion!=null&&(teachRepository.findByCourseIdAndUserId(cpInclusion.getCourseId(),userId)!=null||takeRepository.findByCourseIdAndUserId(cpInclusion.getCourseId(),userId)!=null)){
            List<Participate> list = participateRepository.findAllByProjectId(projectId);
            JSONArray others = new JSONArray();
            JSONArray leaders = new JSONArray();
            for (Participate participate:list){
                User user = userRepository.findByUserId(participate.getUserId());
                if (participate.getIsGroupLeader() == 1){
                    JSONObject leader = new JSONObject();
                    leader.put("student_name",user.getName());
                    leader.put("student_id",user.getUserId());
                    leaders.add(leader);
                }
                else {
                    JSONObject other = new JSONObject();
                    other.put("student_name",user.getName());
                    other.put("student_id",user.getUserId());
                    others.add(other);
                }
            }
            result.put("group_leaders",leaders);
            result.put("others",others);
            return result;
        }

        result.put("message","failure");
        return result;
    }

    public JSONObject allTasks(int userId, int projectId){
        JSONObject result = new JSONObject();
        CpInclusion cpInclusion=cpInclusionRepository.findByProjectId(projectId);
        if (cpInclusion!=null){
            Project project = projectRepository.findByProjectId(projectId);
            Course course=courseRepository.findByCourseId(cpInclusion.getCourseId());
            Participate participate = participateRepository.findByProjectIdAndUserId(projectId, userId);
            if (teachRepository.findByCourseIdAndUserId(course.getCourseId(),userId)!=null || (participate!=null&&participate.getIsGroupLeader() == 1)) {
                List<Task> list = project.getTasks();
                JSONArray tasks = new JSONArray();
                for (Task task : list) {
                    JSONObject object = new JSONObject();
                    object.put("task_id", task.getTaskId());
                    object.put("task_name", task.getTaskName());
                    object.put("start_time", task.getTaskStartTime());
                    object.put("end_time", task.getTaskEndTime());
                    object.put("importance", task.getImportance());
                    object.put("introduce",task.getTaskIntroduce());
                    tasks.add(object);
                }
                result.put("tasks", tasks);
                return result;
            }
            result.put("message", "access deny");
            return result;
        }
        result.put("message","failure");
        return result;
    }

    public JSONObject myTasks(int userId,int projectId){
        JSONObject result = new JSONObject();

        CpInclusion cpInclusion=cpInclusionRepository.findByProjectId(projectId);
        if (cpInclusion!=null){
            Project project = projectRepository.findByProjectId(projectId);
            Participate participate = participateRepository.findByProjectIdAndUserId(projectId, userId);

            if (participate == null) {
                result.put("message", "access deny");
                return result;
            }

            List<Task> tasks = project.getTasks();
            List<Integer> limited = new ArrayList<>();
            for (Task task : tasks) {
                limited.add(task.getTaskId());
            }
            List<SelectTask> list = selectTaskRepository.findAllByUserIdAndLimited(userId, limited);

            JSONArray taskList = new JSONArray();
            for (SelectTask selectTask : list) {
                JSONObject object = new JSONObject();
                Task task = taskRepository.findByTaskId(selectTask.getTaskId());
                object.put("task_id", task.getTaskId());
                object.put("task_name", task.getTaskName());
                object.put("start_time", task.getTaskStartTime());
                object.put("end_time", task.getTaskEndTime());
                object.put("importance", task.getImportance());
                object.put("introduce",task.getTaskIntroduce());
                object.put("is_accomplished", selectTask.getIsAccomplished());
                taskList.add(object);
            }
            result.put("tasks",taskList);
            return result;
        }
        result.put("message","failure");
        return result;
    }

    public JSONObject seeTask(int userId,int taskId){
        JSONObject result = new JSONObject();
        PtInclusion ptInclusion=ptInclusionRepository.findByTaskId(taskId);
        if (ptInclusion==null){
            result.put("message", "failure");
            return result;
        }
        SelectTask selectTask = selectTaskRepository.findByTaskIdAndUserId(taskId,userId);
        CpInclusion cpInclusion=cpInclusionRepository.findByProjectId(ptInclusion.getProjectId());
        Teach teach=teachRepository.findByCourseId(cpInclusion.getCourseId());

        if (selectTask == null&&teach==null){
            result.put("message", "failure");
            return result;
        }
        Task task = taskRepository.findByTaskId(taskId);
        result.put("task_name", task.getTaskName());
        result.put("start_time", task.getTaskStartTime());
        result.put("end_time", task.getTaskEndTime());
        result.put("importance", task.getImportance());
        if (selectTask!=null)
            result.put("is_accomplished", selectTask.getIsAccomplished());
        result.put("introduce",task.getTaskIntroduce());
        return result;
    }

    public JSONObject viewPosts(int userId,int projectId,int page){
        JSONObject result = new JSONObject();
        CpInclusion cpInclusion=cpInclusionRepository.findByProjectId(projectId);
        if (cpInclusion!=null&&(teachRepository.findByCourseIdAndUserId(cpInclusion.getCourseId(),userId)!=null||takeRepository.findByCourseIdAndUserId(cpInclusion.getCourseId(),userId)!=null)){
            List<UserPost> list = userPostRepository.findAllByProjectId(projectId);
            List<Post> postList=new ArrayList<>();
            for (int i=0;i<list.size();i++){
                postList.add(postRepository.findByPostId(list.get(i).getPostId()));
            }
            Collections.sort(postList);

            int pagePerNum = 5;
            int total = list.size();
            if((page - 1) * pagePerNum >= total){
                result.put("message","page not exists");
                return result;
            }
            JSONArray posts = new JSONArray();
            for (int i = pagePerNum * (page - 1);i < Math.min(pagePerNum * page,total);i ++){
                JSONObject object = new JSONObject();
                Post post = postList.get(i);
                UserPost userPost=userPostRepository.findByPostId(post.getPostId());
                User student = userRepository.findByUserId(userPost.getUserId());
                Likes likes = likesRepository.findByUerIdAndPostId(userId,userPost.getPostId());
                object.put("post_id",post.getPostId());
                object.put("post_name",post.getPostName());
                object.put("content",post.getPostContent());
                object.put("likes",post.getLikesCount());
                object.put("post_time",post.getPostTime());
                object.put("student_name",student.getName());
                object.put("avatar",student.getAvatar());
                if (likes != null){
                    object.put("liked",true);
                }
                else {
                    object.put("liked",false);
                }
                posts.add(object);
            }

            result.put("posts",posts);
            result.put("total",total);
            return result;
        }
        result.put("message","failure");
        return result;
    }

    public JSONObject viewReplies(int userId,int postId){
        JSONObject result = new JSONObject();
        UserPost userPost=userPostRepository.findByPostId(postId);
        if (userPost!=null){
            CpInclusion cpInclusion=cpInclusionRepository.findByProjectId(userPost.getProjectId());
            Teach teach=teachRepository.findByCourseIdAndUserId(cpInclusion.getCourseId(),userId);
            Participate participate=participateRepository.findByProjectIdAndUserId(cpInclusion.getProjectId(),userId);
            if (teach!=null||participate!=null){
                List<UserReply> list = userReplyRepository.findAllByPostId(postId);
                JSONArray replies = new JSONArray();
                for (UserReply userReply:list){
                    JSONObject object = new JSONObject();
                    Reply reply = replyRepository.findByReplyId(userReply.getReplyId());
                    User student = userRepository.findByUserId(userReply.getUserId());
                    object.put("reply_content",reply.getReplyContent());
                    object.put("reply_time",reply.getReplyTime());
                    object.put("student_name",student.getName());
                    object.put("avatar",student.getAvatar());
                    replies.add(object);
                }
                JSONArray repliesReverse = new JSONArray();
                for (int i=replies.size()-1;i>=0;i--){
                    repliesReverse.set(replies.size()-i-1,replies.get(i));
                }
                result.put("replies",repliesReverse);
                return result;
            }
            result.put("message","failure");
            return result;
        }
        result.put("message","failure");
        return result;
    }

    public String post(int userId, PostRequest request){
        CpInclusion cpInclusion=cpInclusionRepository.findByProjectId(request.getProject_id());
        if (cpInclusion!=null&&(teachRepository.findByCourseIdAndUserId(cpInclusion.getCourseId(),userId)!=null||participateRepository.findByProjectIdAndUserId(request.getProject_id(),userId)!=null)) {
            Post post = new Post(request.getPost_name(),request.getContent(),new Date());
            postRepository.save(post);
            UserPost userPost = new UserPost(request.getProject_id(),userId,post.getPostId());
            userPostRepository.save(userPost);
            return "success";
        }
        return "failure";
    }

    public String reply(int userId, ReplyRequest replyRequest){
        UserPost userPost=userPostRepository.findByPostId(replyRequest.getPost_id());
        if (userPost!=null){
            CpInclusion cpInclusion=cpInclusionRepository.findByProjectId(userPost.getProjectId());
            if (cpInclusion!=null&&(teachRepository.findByCourseIdAndUserId(cpInclusion.getCourseId(),userId)!=null||participateRepository.findByProjectIdAndUserId(userPost.getProjectId(),userId)!=null)) {
                Reply reply = new Reply(replyRequest.getContent(),new Date());
                replyRepository.save(reply);
                UserReply userReply = new UserReply(replyRequest.getPost_id(),userId,reply.getReplyId());
                userReplyRepository.save(userReply);
                return "success";
            }
            return "failure";
        }
        return "failure";
    }

    public JSONObject files(int userId,int projectId){
        JSONObject result = new JSONObject();
        CpInclusion cpInclusion=cpInclusionRepository.findByProjectId(projectId);
        if (cpInclusion!=null&&(teachRepository.findByCourseIdAndUserId(cpInclusion.getCourseId(),userId)!=null||participateRepository.findByProjectIdAndUserId(projectId,userId)!=null)) {
            List<Upload> list = uploadRepository.findAllByProjectId(projectId);
            JSONArray files = new JSONArray();
            for (Upload upload:list){
                JSONObject object = new JSONObject();
                File file = fileRepository.findByFileId(upload.getFileId());
                User user = userRepository.findByUserId(upload.getUserId());
                object.put("file_id",file.getFileId());
                object.put("file_name",file.getFilename());
                object.put("upload_time",file.getUploadTime());
                object.put("upload_username",user.getName());
                object.put("upload_userId",user.getUserId());
                object.put("avatar",user.getAvatar());
                files.add(object);
            }
            result.put("files",files);
            return result;
        }
        result.put("message","failure");
        return result;
    }

    @Transactional
    public String like(int userId,int postId){
        UserPost userPost=userPostRepository.findByPostId(postId);
        if (userPost!=null){
            CpInclusion cpInclusion=cpInclusionRepository.findByProjectId(userPost.getProjectId());
            Teach teach=teachRepository.findByCourseIdAndUserId(cpInclusion.getCourseId(),userId);
            Participate participate=participateRepository.findByProjectIdAndUserId(cpInclusion.getProjectId(),userId);
            if (teach!=null||participate!=null){
                Post post = postRepository.findByPostId(postId);
                if (likesRepository.findByUerIdAndPostId(userId,postId)==null){
                    Likes likes = new Likes(userId,postId);
                    likesRepository.save(likes);
                    post.setLikesCount(post.getLikesCount() + 1);
                    postRepository.save(post);
                    return "success";
                }
                else {
                    Likes likes = likesRepository.findByUerIdAndPostId(userId,postId);
                    likesRepository.delete(likes);
                    post.setLikesCount(post.getLikesCount() - 1);
                    postRepository.save(post);
                    return "likes canceled";
                }
            }
            return "failure";
        }
        return "failure";

    }

    public String addProject(int userId,int projectId){
        CpInclusion cpInclusion=cpInclusionRepository.findByProjectId(projectId);
        if (cpInclusion!=null&&takeRepository.findByCourseIdAndUserId(cpInclusion.getCourseId(),userId)!=null){
            if (participateRepository.findByProjectIdAndUserId(projectId,userId)==null){
                Participate participate = new Participate(userId,projectId);
                participateRepository.save(participate);
                return "success";
            }
            return "failure";
        }
        return "failure";
    }

    public String teacherScore(int userId,int projectId,int studentId,int score){
        Project project = projectRepository.findByProjectId(projectId);
        if (project == null||project.getStatus()==1){
            return "failure";
        }
        Course course = project.getCourse();

        if (teachRepository.findByCourseIdAndUserId(course.getCourseId(),userId) != null){
            Participate participate = participateRepository.findByProjectIdAndUserId(projectId,studentId);
            if (participate == null || score < 0 || score > 100){
                return "failure";
            }
            Participate newP = new Participate();
            BeanUtils.copyProperties(participate,newP);
            newP.setTeacherGrade(score);
            participateRepository.delete(participate);
            participateRepository.save(newP);
            return "success";
        }
        return "failure";
    }

    public JSONObject viewScore(int userId,int projectId) {
        JSONObject result = new JSONObject();
        Project project = projectRepository.findByProjectId(projectId);
        Participate participate = participateRepository.findByProjectIdAndUserId(projectId, userId);
        if (project == null || project.getStatus() == 0 || participate==null) {
            result.put("message", "failure");
            return result;
        }
        int teacherGrade = participate.getTeacherGrade()>0?project.getTeacherProportion() * participate.getTeacherGrade():0;
        int selfGrade = participate.getSelfGrade()>0?project.getSelfProportion()*participate.getSelfGrade():0;
        int mutualGrade = participate.getMutualGrade()>0?project.getMutualProportion()*participate.getMutualGrade():0;
        int grade = (teacherGrade+selfGrade+mutualGrade) / 100;
        result.put("teacher_grade", participate.getTeacherGrade());
        result.put("self_grade", participate.getSelfGrade());
        result.put("mutual_grade", participate.getMutualGrade());
        result.put("grade", grade);
        return result;
    }

    public String mutualEvaluation(int userId, int projectId, int studentId, int score) {
        Participate participate = participateRepository.findByProjectIdAndUserId(projectId, studentId);
        Participate participate1 = participateRepository.findByProjectIdAndUserId(projectId, userId);
        Project project=projectRepository.findByProjectId(projectId);
        if (participate == null || participate1 == null || score < 0 || score > 100 || project.getStatus()==1) {
            return "failure";
        }
        Participate newP = new Participate();
        BeanUtils.copyProperties(participate, newP);

        Evaluate evaluated=evaluateRepository.findByEvaluatedUserIdAndEvaluateUserIdAndProjectId(studentId,userId,projectId);
        if (evaluated!=null){
            return "you have evaluated";
        }

        int total=0;
        List<Evaluate> evaluates=evaluateRepository.findAllByEvaluatedUserIdAndProjectId(studentId,projectId);
        for (Evaluate evaluate:evaluates){
            total+=evaluate.getScore();
        }

        newP.setMutualGrade((total+score)/(evaluates.size()+1));
        participateRepository.delete(participate);
        participateRepository.save(newP);
        Evaluate evaluate = new Evaluate(userId, studentId, projectId,score);
        evaluateRepository.save(evaluate);
        return "success";
    }

    public String selfEvaluation(int userId, int projectId, int score) {
        Participate participate = participateRepository.findByProjectIdAndUserId(projectId, userId);
        Project project = projectRepository.findByProjectId(projectId);
        if (participate == null || score < 0 || score > 100 || project.getStatus()==1) {
            return "failure";
        }
        Participate newP = new Participate();
        BeanUtils.copyProperties(participate, newP);
        newP.setSelfGrade(score);
        participateRepository.delete(participate);
        participateRepository.save(newP);
        return "success";
    }

    public String publish_score(int userId, int projectId) {
        Project project=projectRepository.findByProjectId(projectId);
        if (project==null||teachRepository.findByCourseIdAndUserId(project.getCourse().getCourseId(),userId)==null){
            return "failure";
        }
        if (project.getStatus()==1) {
            return "published already";
        }
        project.setStatus(1);
        projectRepository.save(project);
        return "success";
    }

    public JSONObject view_all_scores(int userId, int projectId) {
        JSONObject result = new JSONObject();
        Project project=projectRepository.findByProjectId(projectId);
        if (project==null||teachRepository.findByCourseIdAndUserId(project.getCourse().getCourseId(),userId)==null){
            result.put("message","failure");
            return result;
        }

        List<Participate> participateList=participateRepository.findAllByProjectId(projectId);
        JSONArray scores = new JSONArray();
        for (Participate participate:participateList){
            JSONObject object = new JSONObject();
            User user=userRepository.findByUserId(participate.getUserId());
            int teacherGrade = participate.getTeacherGrade()>0?project.getTeacherProportion() * participate.getTeacherGrade():0;
            int selfGrade = participate.getSelfGrade()>0?project.getSelfProportion()*participate.getSelfGrade():0;
            int mutualGrade = participate.getMutualGrade()>0?project.getMutualProportion()*participate.getMutualGrade():0;
            int grade = (teacherGrade+selfGrade+mutualGrade) / 100;
            object.put("username",user.getName());
            object.put("user_id",user.getUserId());
            object.put("avatar",user.getAvatar());
            object.put("teacher_grade",participate.getTeacherGrade());
            object.put("self_grade",participate.getSelfGrade());
            object.put("mutual_grade",participate.getMutualGrade());
            object.put("grade",grade);
            scores.add(object);
        }

        result.put("scores",scores);
        return result;
    }

    public JSONObject choose_leader(int userId, int student_id, int projectId) {
        JSONObject result = new JSONObject();
        Project project=projectRepository.findByProjectId(projectId);
        Participate participate=participateRepository.findByProjectIdAndUserId(projectId,student_id);
        if (project==null||teachRepository.findByCourseIdAndUserId(project.getCourse().getCourseId(),userId)==null||participate==null ||participate.getIsGroupLeader()==1){
            result.put("message","failure");
            return result;
        }

        Participate newP = new Participate();
        BeanUtils.copyProperties(participate, newP);
        newP.setIsGroupLeader(1);
        participateRepository.delete(participate);
        participateRepository.save(newP);

        result.put("message","success");
        return result;
    }
}
