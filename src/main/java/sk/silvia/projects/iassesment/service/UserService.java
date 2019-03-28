package sk.silvia.projects.iassesment.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.silvia.projects.iassesment.dao.UserRepository;
import sk.silvia.projects.iassesment.entity.User;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void registerUser(String username, String password){
        String cryptedPassword = DigestUtils.sha1Hex(password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(cryptedPassword);

        userRepository.save(user);

    }

    public boolean authentificateUser(String userName, String password){
        String cryptedPassword = DigestUtils.sha1Hex(password);

        User user = userRepository.findUserByUsernameAndPassword(userName, cryptedPassword);

        return !(user == null);

    }

}
