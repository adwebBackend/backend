package fudan.se.project.repository;

import fudan.se.project.domain.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends CrudRepository<Course,Integer> {
    Course findByCourseId(int courseId);
    List<Course> findAll();
    @Query(value = "select * from course where courseId not in(:limited)", nativeQuery = true)
    List<Course> findCourseByLimited(@Param("limited")List<Integer> limited);

    void deleteByCourseId(int courseId);
}
