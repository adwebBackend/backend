package fudan.se.project.repository;

import fudan.se.project.domain.Evaluate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluateRepository extends CrudRepository<Evaluate, Long> {
}
