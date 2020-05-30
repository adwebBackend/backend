package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity                     //实体类的注解，必须注明
@Table(name = "project")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Project implements Serializable {
    private static final long serialVersionUID = 9026158182154127983L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projectId")
    private int projectId;

    @Column(name = "projectName")
    private String projectName;

    @Column(name = "projectIntroduce")
    private String projectIntroduce;

    @Column(name = "projectStartTime")
    private Date projectStartTime;

    @Column(name = "projectEndTime")
    private Date projectEndTime;

    @Column(name = "teacherProportion")
    private int teacherProportion;

    @Column(name = "selfProportion")
    private int selfProportion;

    @Column(name = "mutualProportion")
    private int mutualProportion;

    @JsonIgnore
    @ManyToOne(cascade= CascadeType.MERGE)
    @JoinTable(name="cpInclusion",joinColumns={@JoinColumn(name="projectId")}
            ,inverseJoinColumns={@JoinColumn(name="courseId")})
    private Course course;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinTable(name="ptInclusion",joinColumns=@JoinColumn(name="projectId")
            ,inverseJoinColumns=@JoinColumn(name="taskId"))
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinTable(name="userPost",joinColumns=@JoinColumn(name="projectId")
            ,inverseJoinColumns=@JoinColumn(name="postId"))
    private List<Post> posts = new ArrayList<>();

    public List<Post> getPosts() {
        return posts;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectIntroduce() {
        return projectIntroduce;
    }

    public void setProjectIntroduce(String projectIntroduce) {
        this.projectIntroduce = projectIntroduce;
    }

    public Date getProjectStartTime() {
        return projectStartTime;
    }

    public void setProjectStartTime(Date projectStartTime) {
        this.projectStartTime = projectStartTime;
    }

    public Date getProjectEndTime() {
        return projectEndTime;
    }

    public void setProjectEndTime(Date projectEndTime) {
        this.projectEndTime = projectEndTime;
    }

    public int getTeacherProportion() {
        return teacherProportion;
    }

    public void setTeacherProportion(int teacherProportion) {
        this.teacherProportion = teacherProportion;
    }

    public int getSelfProportion() {
        return selfProportion;
    }

    public void setSelfProportion(int selfProportion) {
        this.selfProportion = selfProportion;
    }

    public int getMutualProportion() {
        return mutualProportion;
    }

    public void setMutualProportion(int mutualProportion) {
        this.mutualProportion = mutualProportion;
    }

    public Project(String projectName, String projectIntroduce, Date projectStartTime, Date projectEndTime, int teacherProportion, int selfProportion, int mutualProportion) {
        this.projectName = projectName;
        this.projectIntroduce = projectIntroduce;
        this.projectStartTime = projectStartTime;
        this.projectEndTime = projectEndTime;
        this.teacherProportion = teacherProportion;
        this.selfProportion = selfProportion;
        this.mutualProportion = mutualProportion;
    }

    public Project() {
    }
}
