package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Generated;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity                     //实体类的注解，必须注明
@Table(name = "post")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Post implements Serializable,Comparable<Post> {

    private static final long serialVersionUID = 4501633417701052945L;

    @Id
    @Column(name = "postId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;

    @Column(name = "postName")
    private String postName;

    @Column(name = "postContent")
    private String postContent;

    @Column(name = "postTime")
    private Date postTime;

    @Column(name = "likesCount")
    private int likesCount;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public Post(String postName, String postContent, Date postTime) {

        this.postName = postName;
        this.postContent = postContent;
        this.postTime = postTime;
    }

    public Post() {
    }

    @Override
    public int compareTo(Post post){
        if (this.postTime.getTime()<post.postTime.getTime()){
            return 1;
        }else if (this.postTime.getTime()==post.postTime.getTime()){
            return 0;
        }else {
            return -1;
        }
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
