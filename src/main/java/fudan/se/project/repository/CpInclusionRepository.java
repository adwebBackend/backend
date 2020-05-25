package fudan.se.project.repository;

import fudan.se.project.domain.CpInclusion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpInclusionRepository extends CrudRepository<CpInclusion,Long> {
    void deleteAllByCourseId(int courseId);
}
