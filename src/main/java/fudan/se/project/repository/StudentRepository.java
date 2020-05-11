package fudan.se.project.repository;

import fudan.se.project.domain.Student;
import fudan.se.project.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {
    Student findByStudentId(int studentId);
}