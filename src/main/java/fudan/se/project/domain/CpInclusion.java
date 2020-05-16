package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity                     //实体类的注解，必须注明
@Table(name = "cpInclusion")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class CpInclusion {
    @Id
    @Column(name = "courseId")
    private int courseId;

    @Id
    @Column(name = "projectId")
    private int projectId;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public CpInclusion(int courseId, int projectId) {
        this.courseId = courseId;
        this.projectId = projectId;
    }

    public CpInclusion() {
    }
}
