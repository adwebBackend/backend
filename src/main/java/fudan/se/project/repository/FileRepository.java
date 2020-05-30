package fudan.se.project.repository;

import fudan.se.project.domain.File;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends CrudRepository<File,Long> {
    File findByFileId(int fileId);
}
