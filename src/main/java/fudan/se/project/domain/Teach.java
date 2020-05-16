package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "teach")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Teach {
    @Id
    @Column(name = "userId")
    private int userId;

    @Id
    @Column(name = "courseId")
    private int courseId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Teach(int userId, int courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    public Teach() {
    }
}
