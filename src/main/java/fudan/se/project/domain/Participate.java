package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.io.Serializable;

@Entity                     //实体类的注解，必须注明
@Table(name = "participate")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@IdClass(Participate.class)
public class Participate implements Serializable {
    private static final long serialVersionUID = 43956522808459025L;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Id
    @Column(name = "userId")
    private int userId;

    @Id
    @Column(name = "projectId")
    private int projectId;

    @Column(name = "teacherGrade")
    private int teacherGrade;

    @Column(name = "selfGrade")
    private int selfGrade;

    @Column(name = "mutualGrade")
    private int mutualGrade;

    @Column(name = "isGroupLeader")
    private int isGroupLeader;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getTeacherGrade() {
        return teacherGrade;
    }

    public void setTeacherGrade(int teacherGrade) {
        this.teacherGrade = teacherGrade;
    }

    public int getSelfGrade() {
        return selfGrade;
    }

    public void setSelfGrade(int selfGrade) {
        this.selfGrade = selfGrade;
    }

    public int getMutualGrade() {
        return mutualGrade;
    }

    public void setMutualGrade(int mutualGrade) {
        this.mutualGrade = mutualGrade;
    }

    public int getIsGroupLeader() {
        return isGroupLeader;
    }

    public void setIsGroupLeader(int isGroupLeader) {
        this.isGroupLeader = isGroupLeader;
    }

    public Participate(int userId, int projectId) {
        this.userId = userId;
        this.projectId = projectId;
    }

    public Participate() {
    }
}
