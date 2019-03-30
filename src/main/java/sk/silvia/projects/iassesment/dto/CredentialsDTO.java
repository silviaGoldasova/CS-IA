package sk.silvia.projects.iassesment.dto;

public class CredentialsDTO {

    private String oldUsername;
    private String newUsername;
    private String oldPassword;
    private String newPassword;

    public CredentialsDTO(){
    }

    public CredentialsDTO(String oldUsername, String newUsername, String oldPassword, String newPassword) {
        this.oldUsername = oldUsername;
        this.newUsername = newUsername;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldUsername() {
        return oldUsername;
    }

    public void setOldUsername(String oldUsername) {
        this.oldUsername = oldUsername;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
