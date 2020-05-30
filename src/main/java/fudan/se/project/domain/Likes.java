package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity                     //实体类的注解，必须注明
@Table(name = "likes")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@IdClass(Likes.class)
public class Likes implements Serializable {
    private static final long serialVersionUID = -5552547080949128757L;

    @Id
    @Column(name = "userId")
    private int uerId;

    @Id
    @Column(name = "postId")
    private int postId;

    public int getUerId() {
        return uerId;
    }

    public void setUerId(int uerId) {
        this.uerId = uerId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public Likes(int uerId, int postId) {
        this.uerId = uerId;
        this.postId = postId;
    }

    public Likes() {
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
