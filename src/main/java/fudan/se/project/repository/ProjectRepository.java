package fudan.se.project.repository;

import fudan.se.project.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project,Long> {
    Project findByProjectId(int projectId);
}
