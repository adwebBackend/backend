package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity                     //实体类的注解，必须注明
@Table(name = "supervise")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@IdClass(Supervise.class)
public class Supervise implements Serializable {
    private static final long serialVersionUID = 78311376933444692L;

    @Id
    @Column(name = "superviseUserId")
    private int superviseUserId;

    @Id
    @Column(name = "supervisedUserId")
    private int supervisedUserId;

    @Id
    @Column(name = "taskId")
    private int taskId;

    @Column(name = "isRead")
    private int isRead;

    public int getSuperviseUserId() {
        return superviseUserId;
    }

    public void setSuperviseUserId(int superviseUserId) {
        this.superviseUserId = superviseUserId;
    }

    public int getSupervisedUserId() {
        return supervisedUserId;
    }

    public void setSupervisedUserId(int supervisedUserId) {
        this.supervisedUserId = supervisedUserId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public Supervise(int superviseUserId, int supervisedUserId, int taskId) {
        this.superviseUserId = superviseUserId;
        this.supervisedUserId = supervisedUserId;
        this.taskId = taskId;
    }

    public Supervise() {
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
