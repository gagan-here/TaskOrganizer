package interview.taskorganizer.repositories;

import interview.taskorganizer.entities.TaskEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
  List<TaskEntity> findAllByAssignee(String assignee);
}
