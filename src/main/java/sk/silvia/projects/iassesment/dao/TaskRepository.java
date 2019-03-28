package sk.silvia.projects.iassesment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.silvia.projects.iassesment.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
