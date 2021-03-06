package sk.silvia.projects.iassesment.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MyUser {

    @Id
    private String username;
    private String encryptedPassword;
    private String password;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public MyUser(String username, String encryptedPassword, String password) {
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.password = password;
    }

    public MyUser() {
    }

}
