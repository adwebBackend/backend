package fudan.se.project.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "task")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})

public class Task implements Serializable {
    private static final long serialVersionUID = -6291727854847739988L;

    @Id
    @Column(name = "taskId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taskId;

    @Column(name = "taskName")
    private String taskName;

    @Column(name = "taskIntroduce")
    private String taskIntroduce;

    @Column(name = "taskStartTime")
    private Date taskStartTime;

    @Column(name = "taskEndTime")
    private Date taskEndTime;

    @Column(name = "importance")
    private int importance;

    @JsonIgnore
    @ManyToOne(cascade= CascadeType.MERGE)
    @JoinTable(name="ptInclusion",joinColumns={@JoinColumn(name="taskId")}
            ,inverseJoinColumns={@JoinColumn(name="projectId")})
    private Project project;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskIntroduce() {
        return taskIntroduce;
    }

    public void setTaskIntroduce(String taskIntroduce) {
        this.taskIntroduce = taskIntroduce;
    }

    public Date getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(Date taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public Date getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(Date taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public Task(String taskName, String taskIntroduce, Date taskStartTime, Date taskEndTime, int importance) {
        this.taskName = taskName;
        this.taskIntroduce = taskIntroduce;
        this.taskStartTime = taskStartTime;
        this.taskEndTime = taskEndTime;
        this.importance = importance;
    }

    public Task() {
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

