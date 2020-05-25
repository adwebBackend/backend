package fudan.se.project.repository;

import fudan.se.project.domain.Course;
import fudan.se.project.domain.Participate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipateRepository extends CrudRepository<Participate,Long> {
    @Query(value = "select * from participate where userId =:userId and projectId in(:limited)", nativeQuery = true)
    List<Participate> findParticipateByUserIdAndLimited(int userId,List<Integer> limited);

    @Query(value = "select * from participate where userId =:userId and projectId not in(:limited)", nativeQuery = true)
    List<Participate> findParticipateByUserIdAndNotLimited(int userId,List<Integer> limited);
    List<Participate> findAllByProjectId(int projectId);
    Participate findByProjectIdAndUserId(int projectId,int userId);
}
