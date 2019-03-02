package sk.silvia.projects.IAssesment1.controller;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.silvia.projects.IAssesment1.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
