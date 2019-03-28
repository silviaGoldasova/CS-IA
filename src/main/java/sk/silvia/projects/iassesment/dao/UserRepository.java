package sk.silvia.projects.iassesment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.silvia.projects.iassesment.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsernameAndPassword(String userName, String password);
}
