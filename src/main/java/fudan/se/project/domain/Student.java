package fudan.se.project.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity                     //实体类的注解，必须注明
@Table(name = "user")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Student implements Serializable {

    private static final long serialVersionUID = -7682111882839138963L;
    @Id
    @Column(name = "studentId")
    private int studentId;

    @Column(name = "gender")
    private int gender;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "studentName")
    private String studentName;

    @Column(name = "studentNickName")
    private String studentNickName;

    public Student(int studentId){
        this.studentId=studentId;
    }

    public int getGender() {
        return gender;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentNickName() {
        return studentNickName;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setStudentNickName(String studentNickName) {
        this.studentNickName = studentNickName;
    }
}
