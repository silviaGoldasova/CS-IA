package sk.silvia.projects.iassesment.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.silvia.projects.iassesment.dao.UserRepository;
import sk.silvia.projects.iassesment.entity.MyUser;

import java.util.LinkedList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    Validation validation = new Validation();

    public boolean changeLoginCredentials(String oldUsername, String newUsername, String oldPassword, String newPassword) {
        String cryptedOldPassword = DigestUtils.sha1Hex(oldPassword);
        MyUser user = userRepository.findUserByUsernameAndEncryptedPassword(oldUsername, cryptedOldPassword);

        if (user == null)
            return false;
        if (!(validation.validateString(newUsername) || validation.validateString(newPassword)))
            return false;

        String cryptedNewPassword = DigestUtils.sha1Hex(newPassword);
        MyUser newUser = new MyUser(newUsername, cryptedNewPassword, newPassword);

        userRepository.delete(user);
        userRepository.save(newUser);

        return true;
    }

    public void registerUser(String username, String password){
        String cryptedPassword = DigestUtils.sha1Hex(password);

        MyUser user = new MyUser();
        user.setUsername(username);
        user.setPassword(cryptedPassword);

        userRepository.save(user);
    }

    public boolean authentificateUser(String userName, String password){
        String cryptedPassword = DigestUtils.sha1Hex(password);

        MyUser user = userRepository.findUserByUsernameAndEncryptedPassword(userName, cryptedPassword);

        return !(user == null);

    }

}
