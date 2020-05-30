package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity                     //实体类的注解，必须注明
@Table(name = "reply")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})

public class Reply implements Serializable {

    private static final long serialVersionUID = 5836818645759524712L;

    @Id
    @Column(name = "replyId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int replyId;

    @Column(name = "replyContent")
    private String replyContent;

    @Column(name = "replyTime")
    private Date replyTime;

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public Reply(String replyContent, Date replyTime) {
        this.replyContent = replyContent;
        this.replyTime = replyTime;
    }

    public Reply() {
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
