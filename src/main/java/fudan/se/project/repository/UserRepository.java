package fudan.se.project.repository;

import fudan.se.project.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUserId(int userId);
    User findByEmail(String email);
}
