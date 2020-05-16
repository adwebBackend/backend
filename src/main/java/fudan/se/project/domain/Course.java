package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity                     //实体类的注解，必须注明
@Table(name = "course")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "courseId")
    private int courseId;

    @Column(name = "courseName")
    private String courseName;

    @Column(name = "courseIntroduce")
    private String courseIntroduce;

    @Column(name = "courseStartTime")
    private Date courseStartTime;

    @Column(name = "courseEndTime")
    private Date courseEndTime;

    @Column(name = "picture")
    private String picture;

    public List<Project> getProjects() {
        return projects;
    }

    @OneToMany(mappedBy ="course",cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name="cpInclusion",joinColumns={@JoinColumn(name="courseId")}
            ,inverseJoinColumns={@JoinColumn(name="projectId")})
    private List<Project> projects = new ArrayList<>();

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseIntroduce() {
        return courseIntroduce;
    }

    public void setCourseIntroduce(String courseIntroduce) {
        this.courseIntroduce = courseIntroduce;
    }

    public Date getCourseStartTime() {
        return courseStartTime;
    }

    public void setCourseStartTime(Date courseStartTime) {
        this.courseStartTime = courseStartTime;
    }

    public Date getCourseEndTime() {
        return courseEndTime;
    }

    public void setCourseEndTime(Date courseEndTime) {
        this.courseEndTime = courseEndTime;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Course(String courseName, String courseIntroduce, Date courseStartTime, Date courseEndTime, String picture) {
        this.courseName = courseName;
        this.courseIntroduce = courseIntroduce;
        this.courseStartTime = courseStartTime;
        this.courseEndTime = courseEndTime;
        this.picture = picture;
    }

    public Course() {
    }
}
