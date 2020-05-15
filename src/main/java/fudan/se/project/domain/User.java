package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity                     //实体类的注解，必须注明
@Table(name = "user")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class User implements UserDetails {

    private static final long serialVersionUID = 1723631884331388023L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private int userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "gender")
    private int gender;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "name")
    private String name;

    @Column(name = "nickName")
    private String nickName;

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    @OneToMany(mappedBy ="user",cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<UserRole> userRoles = new HashSet<>();

    public User() {}
    public User(String email, String password, int gender, String avatar, String name, String nickName) {
        this.email = email;
        this.password= password;
        this.gender=gender;
        this.avatar=avatar;
        this.name=name;
        this.nickName=nickName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Set<GrantedAuthority> authoritySet = new HashSet<>();   //去重
        for (UserRole role: userRoles) {
            authoritySet.add(new SimpleGrantedAuthority(role.getRole().getRoleName()));
        }
        authorities.addAll(authoritySet);
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId+"";
    }

    public String getEmail() {
        return email;
    }

    public int getGender(){
        return gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public String getNickName() {
        return nickName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
