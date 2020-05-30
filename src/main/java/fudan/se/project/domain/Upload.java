package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity                     //实体类的注解，必须注明
@Table(name = "upload")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@IdClass(Upload.class)
public class Upload implements Serializable {
    private static final long serialVersionUID = 50338011123555761L;

    @Id
    @Column(name = "projectId")
    private int projectId;

    @Id
    @Column(name = "userId")
    private int userId;

    @Id
    @Column(name = "fileId")
    private int fileId;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public Upload(int projectId, int userId, int fileId) {
        this.projectId = projectId;
        this.userId = userId;
        this.fileId = fileId;
    }

    public Upload() {
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
