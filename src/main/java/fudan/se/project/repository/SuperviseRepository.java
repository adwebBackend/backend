package fudan.se.project.repository;

import fudan.se.project.domain.Supervise;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperviseRepository extends CrudRepository<Supervise,Long> {
}
