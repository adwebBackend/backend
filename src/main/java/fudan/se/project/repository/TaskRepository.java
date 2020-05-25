package fudan.se.project.repository;

import fudan.se.project.domain.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<Task,Long> {
    Task findByTaskId(int taskId);
}
