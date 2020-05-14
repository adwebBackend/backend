package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="authority")
public class Authority implements GrantedAuthority {

    private static final long serialVersionUID = 5269344455501285844L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auth_name",unique = true)
    private String authName;

    @Column(name = "auth_nameZh")
    private String authNameZh;

    //role_authority
    @JsonIgnore
    @ManyToMany(mappedBy = "roleAuthority")  //,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Role> roles;

    public Authority(){}

    public Authority(String authName, String authNameZh, Set<Role> roles) {
        this.authName = authName;
        this.authNameZh = authNameZh;
        this.roles = roles;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getAuthNameZh() {
        return authNameZh;
    }

    public void setAuthNameZh(String authNameZh) {
        this.authNameZh = authNameZh;
    }

    public Long getId() {
        return id;
    }

    /**
     * from Interface GrantedAuthority
     * @return authName
     */
    @Override
    public String getAuthority() {
        return authName;
    }

}