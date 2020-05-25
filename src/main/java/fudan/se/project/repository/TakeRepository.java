package fudan.se.project.repository;

import fudan.se.project.domain.Take;
import fudan.se.project.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TakeRepository extends CrudRepository<Take,Long> {
    Page<Take> findByUserId(int userId, Pageable pageable);
    List<Take> findAllByUserId(int uerId);
    List<Take> findAllByCourseId(int courseId);
    void deleteAllByCourseId(int courseId);
    void deleteAllByUserId(int userId);
}
