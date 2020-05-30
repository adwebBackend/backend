package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity                     //实体类的注解，必须注明
@Table(name = "userReply")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@IdClass(UserReply.class)
public class UserReply implements Serializable {

    private static final long serialVersionUID = -4457879246542388330L;

    @Id
    @Column(name = "postId")
    private int postId;

    @Id
    @Column(name = "userId")
    private int userId;

    @Id
    @Column(name = "replyId")
    private int replyId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public UserReply(int postId, int userId, int replyId) {
        this.postId = postId;
        this.userId = userId;
        this.replyId = replyId;
    }

    public UserReply() {
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
