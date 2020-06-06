package fudan.se.project.controller.request;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class SetTaskRequest {

    @NotNull(message = "projectId is required")
    private int project_id;

    @NotNull(message = "name is required")
    private String name;

    @NotNull(message = "startTime is required")
    @Future
    private Date start_time;

    @NotNull(message = "endTime is required")
    @Future
    private Date end_time;

    @NotNull(message = "introduce is required")
    private String introduce;

    @NotNull(message = "importance is required")
    @Max(5)
    @Min(1)
    private int importance;

    @NotNull(message = "assign is required")
    private List<Integer> assign;

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public List<Integer> getAssign() {
        return assign;
    }

    public void setAssign(List<Integer> assign) {
        this.assign = assign;
    }

    public SetTaskRequest(@NotNull(message = "projectId is required") int project_id, @NotNull(message = "name is required") String name, @NotNull(message = "startTime is required") @Future Date start_time, @NotNull(message = "endTime is required") @Future Date end_time, @NotNull(message = "introduce is required") String introduce, @NotNull(message = "importance is required") int importance, @NotNull(message = "assign is required") List<Integer> assign) {
        this.project_id = project_id;
        this.name = name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.introduce = introduce;
        this.importance = importance;
        this.assign = assign;
    }

    public SetTaskRequest() {
    }
}
