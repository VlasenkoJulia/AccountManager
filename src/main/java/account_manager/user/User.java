package account_manager.user;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class User {
    @Id
    @Column(name = "user_name")
    private String userName;

    private String password;

    private String email;

    @Column(name = "reset_token")
    private String resetToken;

    @Transient
    private String confirmPassword;

    public User() {
    }

    public User(String userName, String password, String email, String resetToken, String confirmPassword) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.resetToken = resetToken;
        this.confirmPassword = confirmPassword;
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
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getUserName(), user.getUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName());
    }
}
