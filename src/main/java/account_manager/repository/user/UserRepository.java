package account_manager.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String>,
        CustomizedUserRepository<UserEntity> {
    UserEntity findByEmail(String email);

    UserEntity findByResetToken(String resetToken);
}
