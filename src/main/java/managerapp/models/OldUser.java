package managerapp.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OldUser {

    private int userId;

    @NotNull
    @NotEmpty(message = "OldUser Name Required")
    @Size(min=5, max=15, message = "OldUser name must be between 5 and 15 characters")
    private String userName;

    @NotEmpty(message = "Password Required")
    @Size(min=6, message = "Password must be at least 5 characters")
    private String password;


    @Email(regexp = "^(.+)@(.+)$", message = "Invalid email adress")
    private String email;

    @NotNull(message = "Passwords do not match")
    private String verifyPassword;

    private static int nextId = 1;

    public OldUser() {
        userId = nextId;
        nextId++;
    }

    public OldUser(String userName, String password, String email) {
        this();
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    public int getUserId() { return userId; }

//    public void setUserId(int userId) { this.userId = userId; }

    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int nextId) {
        OldUser.nextId = nextId;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
        checkPassword();
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
        checkPassword();
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    private void checkPassword() {
        if (!password.equals(verifyPassword) && verifyPassword != null) {
            verifyPassword = null;
        }
    }

}
