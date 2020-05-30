package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "selectTask")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@IdClass(SelectTask.class)
public class SelectTask implements Serializable {

    private static final long serialVersionUID = -3592420698232209524L;

    @Id
    @Column(name = "userId")
    private int userId;

    @Id
    @Column(name = "taskId")
    private int taskId;

    @Column(name = "isAccomplished")
    private int isAccomplished;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getIsAccomplished() {
        return isAccomplished;
    }

    public void setIsAccomplished(int isAccomplished) {
        this.isAccomplished = isAccomplished;
    }

    public SelectTask() {
    }

    public SelectTask(int userId, int taskId, int isAccomplished) {
        this.userId = userId;
        this.taskId = taskId;
        this.isAccomplished = isAccomplished;
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
