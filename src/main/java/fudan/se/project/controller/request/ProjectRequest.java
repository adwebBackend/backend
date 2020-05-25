package fudan.se.project.controller.request;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.*;
import java.util.Date;

public class ProjectRequest {
    @NotNull(message = "courseId is required")
    private int course_id;
    @NotNull(message = "name is required")
    private String name;
    @NotNull(message = "startTime is required")
    @Future
    private Date start_time;
    @NotNull(message = "endTime is required")
    @Future
    private Date end_time;
    @NotNull(message = "description is required")
    private String description;
    @NotNull(message = "teacherProportion is required")
    @Min(value = 0,message = "teacherProportion should be larger than 0")
    @Max(value = 100, message = "teacherProportion should be smaller than 100")
    private int teacher_proportion;
    @NotNull(message = "selfProportion is required")
    @Min(value = 0,message = "selfProportion should be larger than 0")
    @Max(value = 100, message = "selfProportion should be smaller than 100")
    private int self_proportion;
    @NotNull(message = "mutualProportion is required")
    @Min(value = 0,message = "mutualProportion should be larger than 0")
    @Max(value = 100, message = "mutualProportion should be smaller than 100")
    private int mutual_proportion;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getTeacher_proportion() {
        return teacher_proportion;
    }

    public void setTeacher_proportion(int teacher_proportion) {
        this.teacher_proportion = teacher_proportion;
    }

    public int getSelf_proportion() {
        return self_proportion;
    }

    public void setSelf_proportion(int self_proportion) {
        this.self_proportion = self_proportion;
    }

    public int getMutual_proportion() {
        return mutual_proportion;
    }

    public void setMutual_proportion(int mutual_proportion) {
        this.mutual_proportion = mutual_proportion;
    }

    public ProjectRequest(@NotNull(message = "courseId is required") int course_id, @NotNull(message = "name is required") String name, @NotNull(message = "startTime is required") @Future Date start_time, @NotNull(message = "endTime is required") @Future Date end_time, @NotNull(message = "description is required") String description, @NotNull(message = "teacherProportion is required") @Min(value = 0, message = "teacherProportion should be larger than 0") @Max(value = 100, message = "teacherProportion should be smaller than 100") int teacher_proportion, @NotNull(message = "selfProportion is required") @Min(value = 0, message = "selfProportion should be larger than 0") @Max(value = 100, message = "selfProportion should be smaller than 100") int self_proportion, @NotNull(message = "mutualProportion is required") @Min(value = 0, message = "mutualProportion should be larger than 0") @Max(value = 100, message = "mutualProportion should be smaller than 100") int mutual_proportion) {
        this.course_id = course_id;
        this.name = name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.description = description;
        this.teacher_proportion = teacher_proportion;
        this.self_proportion = self_proportion;
        this.mutual_proportion = mutual_proportion;
    }

    public ProjectRequest() {
    }
}
