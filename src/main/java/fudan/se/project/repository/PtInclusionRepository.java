package fudan.se.project.repository;

import fudan.se.project.domain.PtInclusion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PtInclusionRepository extends CrudRepository<PtInclusion,Long> {
    List<PtInclusion> findAllByProjectId(int projectId);
}
