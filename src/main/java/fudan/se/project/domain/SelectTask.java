package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "selectTask")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class SelectTask implements Serializable {

    private static final long serialVersionUID = -3592420698232209524L;

    @Id
    @Column(name = "userId")
    private int userId;

    @Column(name = "")
    private int taskId;

    @Column(name = "isAccomplished")
    private int isAccomplished;


}
