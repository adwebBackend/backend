package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity                     //实体类的注解，必须注明
@Table(name = "role")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})

public class Role implements Serializable,GrantedAuthority {

    private static final long serialVersionUID = -4835130585182561640L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_nameZh")
    private String roleNameZh;

    @Column(name = "description")
    private String description;


    /**
     * 和 Authority 多对多
     * 用户只和角色有直接对应，只通过角色添加权限
     * 和User中的authorities不同
     */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "role_authority",joinColumns = @JoinColumn(name="role_id"),inverseJoinColumns=@JoinColumn(name="authority_id"))
    private Set<Authority> roleAuthority = new HashSet<>();

    /**
     * 和 UserRole 一对多
     */
    @JsonIgnore
    @OneToMany(mappedBy = "role",cascade = CascadeType.MERGE, fetch = FetchType.LAZY)   //权限：更新； 懒加载
    private Set<UserRole> userRoles = new HashSet<>();

    public Role(){}


    public Role(String roleName, String roleNameZh, String description) {
        this.roleName = roleName;
        this.roleNameZh = roleNameZh;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleNameZh() {
        return roleNameZh;
    }

    public void setRoleNameZh(String roleNameZh) {
        this.roleNameZh = roleNameZh;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Set<Authority> getRoleAuthority() {
        return roleAuthority;
    }

    public void setRoleAuthority(Set<Authority> roleAuthority) {
        this.roleAuthority = roleAuthority;
    }

    /**
     * from Interface GrantedAuthority
     * @return roleName
     */
    @Override
    public String getAuthority() {
        return roleName;
    }
}