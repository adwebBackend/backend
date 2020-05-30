package fudan.se.project.repository;

import fudan.se.project.domain.Reply;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends CrudRepository<Reply,Long> {
    Reply findByReplyId(int replyId);
}
