package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "teach")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@IdClass(Teach.class)
public class Teach implements Serializable {
    private static final long serialVersionUID = 4442567925886325510L;

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
