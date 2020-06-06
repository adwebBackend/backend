package fudan.se.project.repository;

import fudan.se.project.domain.UserPost;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPostRepository extends CrudRepository<UserPost,Long> {
    List<UserPost> findAllByProjectId(int projectId);
    void deleteAllByUserId(int userId);
    UserPost findByPostId(int postId);
}
