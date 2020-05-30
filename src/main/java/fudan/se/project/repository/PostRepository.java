package fudan.se.project.repository;

import fudan.se.project.domain.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

@Repository
public interface PostRepository extends CrudRepository<Post,Long> {
    Post findByPostId(int postId);
}
