package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity                     //实体类的注解，必须注明
@Table(name = "evaluate")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@IdClass(Evaluate.class)
public class Evaluate implements Serializable {
    private static final long serialVersionUID = -3224831469023874302L;

    @Id
    @Column(name = "evaluateUserId")
    private int evaluateUserId;

    @Id
    @Column(name = "evaluatedUserId")
    private int evaluatedUserId;

    @Id
    @Column(name = "projectId")
    private int projectId;

    @Id
    @Column(name = "score")
    private int score;

    public int getEvaluateUserId() {
        return evaluateUserId;
    }

    public void setEvaluateUserId(int evaluateUserId) {
        this.evaluateUserId = evaluateUserId;
    }

    public int getEvaluatedUserId() {
        return evaluatedUserId;
    }

    public void setEvaluatedUserId(int evaluatedUserId) {
        this.evaluatedUserId = evaluatedUserId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Evaluate(int evaluateUserId, int evaluatedUserId, int projectId,int score) {
        this.evaluateUserId = evaluateUserId;
        this.evaluatedUserId = evaluatedUserId;
        this.projectId = projectId;
        this.score=score;
    }

    public Evaluate() {
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
