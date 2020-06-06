package fudan.se.project.repository;

import fudan.se.project.domain.Supervise;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuperviseRepository extends CrudRepository<Supervise,Long> {
    void deleteAllBySuperviseUserIdOrSupervisedUserId(int superviseUserId,int supervisedUserId);
    Supervise findBySupervisedUserIdAndSuperviseUserIdAndTaskId(int supervisedUserId,int superviseUserId,int taskId);
    List<Supervise> findBySupervisedUserId(int id);
}
