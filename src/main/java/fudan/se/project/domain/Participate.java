package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity                     //实体类的注解，必须注明
@Table(name = "participate")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Participate {
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

    public Participate(int userId, int projectId, int teacherGrade, int selfGrade, int mutualGrade, int isGroupLeader) {
        this.userId = userId;
        this.projectId = projectId;
        this.teacherGrade = teacherGrade;
        this.selfGrade = selfGrade;
        this.mutualGrade = mutualGrade;
        this.isGroupLeader = isGroupLeader;
    }

    public Participate() {
    }
}
