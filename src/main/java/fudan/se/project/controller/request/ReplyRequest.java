package fudan.se.project.controller.request;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class ReplyRequest {
    @NotNull(message = "postId is required")
    private int post_id;

    @NotNull(message = "content is required")
    private String content;

    @NotNull(message = "replTime is required")
    private Date reply_time;

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getReply_time() {
        return reply_time;
    }

    public void setReply_time(Date reply_time) {
        this.reply_time = reply_time;
    }

    public ReplyRequest(@NotNull(message = "postId is required") int post_id, @NotNull(message = "content is required") String content, @NotNull(message = "replTime is required") Date reply_time) {
        this.post_id = post_id;
        this.content = content;
        this.reply_time = reply_time;
    }

    public ReplyRequest() {
    }
}
