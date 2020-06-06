package fudan.se.project.repository;

import fudan.se.project.domain.Upload;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadRepository extends CrudRepository<Upload,Long> {
    List<Upload> findAllByProjectId(int projectId);
    Upload findByFileIdAndUserId(int fileId,int userId);
    void deleteAllByUserId(int userId);
}
