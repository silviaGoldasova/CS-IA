package sk.silvia.projects.IAssesment1.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.silvia.projects.IAssesment1.model.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
