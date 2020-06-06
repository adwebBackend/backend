package fudan.se.project.repository;

import fudan.se.project.domain.Teach;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeachRepository extends CrudRepository<Teach,Integer> {
    Page<Teach> findByUserId(int userId, Pageable pageable);
    List<Teach> findAllByUserId(int uerId);
    Teach findByCourseIdAndUserId(int courseId,int userId);
    void deleteAllByCourseId(int courseId);
    void deleteByCourseIdAndUserId(int courseId,int userId);
    Teach findByCourseId(int courseId);
    void deleteAllByUserId(int userId);
}
