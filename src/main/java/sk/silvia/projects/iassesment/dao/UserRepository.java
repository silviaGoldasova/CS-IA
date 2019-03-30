package sk.silvia.projects.iassesment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.silvia.projects.iassesment.entity.MyUser;

public interface UserRepository extends JpaRepository<MyUser, String> {
    MyUser findUserByUsernameAndEncryptedPassword(String userName, String password);
    MyUser findUserByUsername(String username);
}



