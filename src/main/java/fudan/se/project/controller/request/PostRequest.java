package fudan.se.project.controller.request;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class PostRequest {
    @NotNull(message = "projectId is required")
    private int project_id;

    @NotNull(message = "postName is required")
    private String post_name;

    @NotNull(message = "content is required")
    private String content;

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getPost_name() {
        return post_name;
    }

    public void setPost_name(String post_name) {
        this.post_name = post_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PostRequest(@NotNull(message = "projectId is required") int project_id, @NotNull(message = "postName is required") String post_name, @NotNull(message = "content is required") String content, @NotNull(message = "post_time is required") Date post_time) {
        this.project_id = project_id;
        this.post_name = post_name;
        this.content = content;
    }

    public PostRequest() {
    }
}
