package fudan.se.project.repository;

import fudan.se.project.domain.SelectTask;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelectTaskRepository extends CrudRepository<SelectTask,Long> {
    @Query(value = "select * from selectTask where userId=:userId and taskId in(:limited)", nativeQuery = true)
    List<SelectTask> findAllByUserIdAndLimited(@Param("userId") int userId,@Param("limited") List<Integer> limited);

    List<SelectTask> findAllByTaskId(int taskId);
    SelectTask findByTaskIdAndUserId(int taskId, int userId);
}
