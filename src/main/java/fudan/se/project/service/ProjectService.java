package fudan.se.project.service;

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
        if (startTime.after(endTime)) {
            return "startTime should be earlier than endTime";
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
        return result;
    }

    public JSONObject groupMembers(int userId,int projectId){
        JSONObject result = new JSONObject();

        User user = userRepository.findByUserId(userId);
        if (user != null){
            Project project = projectRepository.findByProjectId(projectId);
            if (project == null){
                result.put("message","project not found");
                return result;
            }
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

        User user = userRepository.findByUserId(userId);
        if (user != null) {
            Project project = projectRepository.findByProjectId(projectId);
            if (project == null){
                result.put("message","project not found");
                return result;
            }
            Participate participate = participateRepository.findByProjectIdAndUserId(projectId, userId);
            Course course = project.getCourse();

            if (teachRepository.findByCourseIdAndUserId(course.getCourseId(),userId)==null && (participate == null || participate.getIsGroupLeader() == 0)) {
                result.put("message", "access deny");
                return result;
            }
            if (teachRepository.findByCourseIdAndUserId(course.getCourseId(),userId)!=null || participate.getIsGroupLeader() == 1) {
                List<Task> list = project.getTasks();
                if (list.size() == 0) {
                    result.put("message", "this project has no task");
                    return result;
                }
                List<JSONObject> tasks = new ArrayList<>();
                for (Task task : list) {
                    JSONObject object = new JSONObject();
                    object.put("task_id", task.getTaskId());
                    object.put("task_name", task.getTaskName());
                    object.put("start_time", task.getTaskStartTime());
                    object.put("end_time", task.getTaskEndTime());
                    object.put("importance", task.getImportance());
                    tasks.add(object);
                }
                result.put("tasks", tasks);
                return result;
            }
        }
        result.put("message","failure");
        return result;
    }

    public JSONObject myTasks(int userId,int projectId){
        JSONObject result = new JSONObject();

        User user = userRepository.findByUserId(userId);
        if (user != null) {
            Project project = projectRepository.findByProjectId(projectId);
            if (project == null){
                result.put("message","project not found");
                return result;
            }
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
            if (list.size() == 0) {
                result.put("message", "you have no task");
                return result;
            }
            List<JSONObject> taskList = new ArrayList<>();
            for (SelectTask selectTask : list) {
                JSONObject object = new JSONObject();
                Task task = taskRepository.findByTaskId(selectTask.getTaskId());
                object.put("task_id", task.getTaskId());
                object.put("task_name", task.getTaskName());
                object.put("start_time", task.getTaskStartTime());
                object.put("end_time", task.getTaskEndTime());
                object.put("importance", task.getImportance());
                object.put("is_accomplished", selectTask.getIsAccomplished());
                taskList.add(object);
            }
            result.put("tasks",taskList);
            return result;
        }
        result.put("message","failure");
        return result;
    }

    public JSONObject viewPosts(int userId,int projectId,int page){
        JSONObject result = new JSONObject();
        User user = userRepository.findByUserId(userId);
        if (user != null){
            Project project = projectRepository.findByProjectId(projectId);
            if (project == null){
                result.put("message","project not found");
                return result;
            }
            List<UserPost> list = userPostRepository.findAllByProjectId(projectId);
            if (list.size()==0){
                result.put("message","this project has no post");
                return result;
            }
            int pagePerNum = 5;
            int total = list.size();
            if((page - 1) * pagePerNum >= total){
                result.put("message","page not exists");
                return result;
            }
            List<JSONObject> posts = new ArrayList<>();
            for (int i = pagePerNum * (page - 1);i < Math.min(pagePerNum * page,total);i ++){
                JSONObject object = new JSONObject();
                UserPost userPost = list.get(i);
                Post post = postRepository.findByPostId(userPost.getPostId());
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
            return result;
        }
        result.put("message","failure");
        return result;
    }

    public JSONObject viewReplies(int userId,int postId){
        JSONObject result = new JSONObject();
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            Post post = postRepository.findByPostId(postId);
            if (post == null) {
                result.put("message", "post not found");
                return result;
            }
            List<UserReply> list = userReplyRepository.findAllByPostId(postId);
            if (list.size()==0){
                result.put("message","this post has no reply");
                return result;
            }
            List<JSONObject> replies = new ArrayList<>();
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
            result.put("replies",replies);
            return result;
        }
        result.put("message","failure");
        return result;
    }

    public String post(int userId, PostRequest request){
        User user = userRepository.findByUserId(userId);
        if (user != null){
            Project project = projectRepository.findByProjectId(request.getProject_id());
            if (project == null){
                return "project not found";
            }
            Post post = new Post(request.getPost_name(),request.getContent(),request.getPost_time());
            postRepository.save(post);
            UserPost userPost = new UserPost(request.getProject_id(),userId,post.getPostId());
            userPostRepository.save(userPost);
            return "success";
        }
        return "failure";
    }

    public String reply(int userId, ReplyRequest replyRequest){
        User user = userRepository.findByUserId(userId);
        if (user != null){
            Post post = postRepository.findByPostId(replyRequest.getPost_id());
            if (post == null){
                return "post not found";
            }
            Reply reply = new Reply(replyRequest.getContent(),replyRequest.getReply_time());
            replyRepository.save(reply);
            UserReply userReply = new UserReply(replyRequest.getPost_id(),userId,reply.getReplyId());
            userReplyRepository.save(userReply);
            return "success";
        }
        return "failure";
    }

    public JSONObject files(int userId,int projectId){
        JSONObject result = new JSONObject();
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            List<Upload> list = uploadRepository.findAllByProjectId(projectId);
            if (list.size()==0){
                result.put("message","this project has no file");
                return result;
            }
            List<JSONObject> files = new ArrayList<>();
            for (Upload upload:list){
                JSONObject object = new JSONObject();
                File file = fileRepository.findByFileId(upload.getFileId());
                User user1 = userRepository.findByUserId(upload.getUserId());
                object.put("file_name",file.getFilename());
                object.put("path",file.getPath());
                object.put("upload_time",file.getUploadTime());
                object.put("upload_username",user1.getName());
                object.put("avatar",user1.getAvatar());
                files.add(object);
            }
            result.put("files",files);
            return result;
        }
        result.put("message","failure");
        return result;
    }

    public String like(int userId,int postId){
        User user = userRepository.findByUserId(userId);
        if (user != null){
            Post post = postRepository.findByPostId(postId);
            if (post == null){
                return "post not found";
            }
            Likes likes = new Likes(userId,postId);
            likesRepository.save(likes);
            post.setLikesCount(post.getLikesCount() + 1);
            postRepository.save(post);
            return "success";
        }
        return "failure";
    }

    public String addProject(int userId,int projectId){
        if (authService.checkAuthor("student",userId)){
            Project project = projectRepository.findByProjectId(projectId);
            if(project == null){
                return "project not found";
            }

            Participate participate = new Participate(userId,projectId);
            participateRepository.save(participate);
            return "success";
        }
        return "failure";
    }

    public String teacherScore(int userId,int projectId ,int studentId,int score){
        Project project = projectRepository.findByProjectId(projectId);
        if (project == null){
            return "param error";
        }
        Course course = project.getCourse();

        if (teachRepository.findByCourseIdAndUserId(course.getCourseId(),userId) != null){
            Participate participate = participateRepository.findByProjectIdAndUserId(projectId,studentId);
            if (participate == null || score < 0 || score > 100){
                return "param error";
            }
            Participate newP = new Participate();
            BeanUtils.copyProperties(participate,newP);
            newP.setTeacherGrade(score);
            participateRepository.delete(participate);
            participateRepository.save(newP);
            Evaluate evaluate = new Evaluate(userId,studentId,projectId);
            evaluateRepository.save(evaluate);
            return "success";
        }
        return "failure";
    }

    public JSONObject viewScore(int userId,int projectId){
        JSONObject result = new JSONObject();
        if (authService.checkAuthor("student",userId)){
            Project project = projectRepository.findByProjectId(projectId);
            if (project == null){
                result.put("message","project not found");
                return result;
            }
            Participate participate = participateRepository.findByProjectIdAndUserId(projectId,userId);
            int grade = (project.getTeacherProportion() * participate.getTeacherGrade() + project.getSelfProportion() * participate.getSelfGrade() + project.getMutualProportion() * participate.getMutualGrade()) / 100;
            result.put("teacher_grade",participate.getTeacherGrade());
            result.put("self_grade",participate.getSelfGrade());
            result.put("mutual_grade",participate.getMutualGrade());
            result.put("grade",grade);
            return result;
        }
        result.put("message","failure");
        return result;
    }

    public String mutualEvaluation(int userId, int projectId, int studentId, int score){
        if (authService.checkAuthor("student",userId)){
            Participate participate = participateRepository.findByProjectIdAndUserId(projectId,studentId);
            if (participate == null || score < 0 || score > 100){
                return "param error";
            }
            Participate newP = new Participate();
            BeanUtils.copyProperties(participate,newP);
            newP.setMutualGrade(score);
            participateRepository.delete(participate);
            participateRepository.save(newP);
            Evaluate evaluate = new Evaluate(userId,studentId,projectId);
            evaluateRepository.save(evaluate);
            return "success";
        }
        return "failure";
    }

    public String selfEvaluation(int userId, int projectId, int score){
        if (authService.checkAuthor("student",userId)){
            Participate participate = participateRepository.findByProjectIdAndUserId(projectId,userId);
            if (participate == null || score < 0 || score > 100){
                return "param error";
            }
            Participate newP = new Participate();
            BeanUtils.copyProperties(participate,newP);
            newP.setSelfGrade(score);
            participateRepository.delete(participate);
            participateRepository.save(newP);
            return "success";
        }
        return "failure";
    }


}
