package sk.silvia.projects.iassesment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.silvia.projects.iassesment.entity.CompletedTask;

public interface CompletedTasksRepository extends JpaRepository<CompletedTask, Long> {
}
