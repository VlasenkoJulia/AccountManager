package account_manager.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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