package fudan.se.project.repository;

import fudan.se.project.domain.Evaluate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluateRepository extends CrudRepository<Evaluate, Long> {
    List<Evaluate> findAllByEvaluatedUserIdAndProjectId(int evaluatedUserId,int projectId);
    Evaluate findByEvaluatedUserIdAndEvaluateUserIdAndProjectId(int evaluatedUserId,int evaluateUserId,int projectId);
}
