package fudan.se.project.repository;

import fudan.se.project.domain.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends CrudRepository<Course,Integer> {
    Course findByCourseId(int courseId);

    @Query(value = "select * from course where courseId not in(:limited)", nativeQuery = true)
    List<Course> findCourseByLimited(List<Integer> limited);
}
