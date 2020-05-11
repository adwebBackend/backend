package fudan.se.project.controller.request;

import java.util.Set;

public class RegisterRequest {
    private String email;
    private String password;
    private Set<String> authorities;

    public RegisterRequest() {}

    public RegisterRequest(String email, String password, Set<String> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

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

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }
}

