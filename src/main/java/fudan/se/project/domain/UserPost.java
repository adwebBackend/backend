package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity                     //实体类的注解，必须注明
@Table(name = "userPost")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@IdClass(UserPost.class)
public class UserPost implements Serializable {

    private static final long serialVersionUID = 9169388080345624891L;

    @Id
    @Column(name = "projectId")
    private int projectId;

    @Id
    @Column(name = "userId")
    private int userId;

    @Id
    @Column(name = "postId")
    private int postId;

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

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public UserPost() {
    }

    public UserPost(int projectId, int userId, int postId) {
        this.projectId = projectId;
        this.userId = userId;
        this.postId = postId;
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
