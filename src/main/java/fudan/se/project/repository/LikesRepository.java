package fudan.se.project.repository;

import fudan.se.project.domain.Likes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends CrudRepository<Likes,Long> {
    Likes findByUerIdAndPostId(int userId,int postId);
}
