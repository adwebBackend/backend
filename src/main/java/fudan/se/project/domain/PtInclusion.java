package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ptInclusion")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@IdClass(PtInclusion.class)
public class PtInclusion implements Serializable {

    private static final long serialVersionUID = 7834665936309864180L;

    @Id
    @Column(name = "taskId")
    private int taskId;

    @Id
    @Column(name = "projectId")
    private int projectId;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public PtInclusion(int taskId, int projectId) {
        this.taskId = taskId;
        this.projectId = projectId;
    }

    public PtInclusion() {
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
