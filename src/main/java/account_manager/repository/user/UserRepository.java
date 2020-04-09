package account_manager.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String>,
        CustomizedUserRepository<User> {
    User findByEmail(String email);

    User findByResetToken(String resetToken);
}
