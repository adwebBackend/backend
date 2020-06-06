package fudan.se.project.controller.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AddUserRequest {
    @NotNull(message = "邮箱不能为空")
    @NotBlank(message = "邮箱不能为空")
    private String email;
    @NotNull(message = "密码不能为空")
    @NotBlank(message = "密码不能为空")
    private String password;

    public AddUserRequest() {}


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AddUserRequest(@NotNull(message = "邮箱不能为空") @NotBlank(message = "邮箱不能为空") String email, @NotNull(message = "密码不能为空") @NotBlank(message = "密码不能为空") String password) {
        this.email = email;
        this.password = password;
    }
}
