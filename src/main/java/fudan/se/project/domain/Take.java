package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "take")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})

public class Take {
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

    public Take(int userId, int courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    public Take() {
    }
}
