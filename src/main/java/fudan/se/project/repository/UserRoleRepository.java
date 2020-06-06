package fudan.se.project.repository;

import fudan.se.project.domain.User;
import fudan.se.project.domain.UserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
    void deleteAllByUser(User user);

}
