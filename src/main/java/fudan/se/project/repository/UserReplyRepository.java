package fudan.se.project.repository;

import fudan.se.project.domain.UserReply;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReplyRepository extends CrudRepository<UserReply,Long> {
    List<UserReply> findAllByPostId(int postId);
    void deleteAllByUserId(int userId);
}
