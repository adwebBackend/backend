package fudan.se.project.controller.request;

import javax.validation.constraints.Past;
import java.util.Date;

public class ModifyUserRequest {
    private int id;
    private String nickname;
    private int gender;
    private String name;
    private String signature;
    @Past
    private Date birthday;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public ModifyUserRequest(int id, String nickname, int gender, String name, String signature, Date birthday) {
        this.id = id;
        this.nickname = nickname;
        this.gender = gender;
        this.name = name;
        this.signature = signature;
        this.birthday = birthday;
    }

    public ModifyUserRequest() {
    }
}
