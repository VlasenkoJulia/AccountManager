package account_manager.repository.user;

import account_manager.repository.AbstractTestRepository;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class UserEntityRepositoryTest extends AbstractTestRepository {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByEmail() {
        UserEntity userEntity = new UserEntity("testUser", "testPass",
                "test.email@gmail.com", null, "testPass");
        userRepository.save(userEntity);
        UserEntity foundUserEntity = userRepository.findByEmail(userEntity.getEmail());
        assertEquals(userEntity, foundUserEntity);
    }

    @Test
    public void findByResetToken() {
        UserEntity userEntity = new UserEntity("testUser", "testPass",
                "test.email@gmail.com", "resetToken", "testPass");
        userRepository.save(userEntity);
        UserEntity foundUserEntity = userRepository.findByResetToken(userEntity.getResetToken());
        assertEquals(userEntity, foundUserEntity);
    }

    @Test
    public void findByName() {
        UserEntity userEntity = new UserEntity("testUser", "testPass",
                "test.email@gmail.com", null, "testPass");
        userRepository.save(userEntity);
        Optional<UserEntity> foundUser = userRepository.findById(userEntity.getUserName());
        assertThat(foundUser).isPresent();
        assertEquals(userEntity, foundUser.get());
    }

    @Test
    public void update() {
        UserEntity userEntity = new UserEntity("testUser", "testPass",
                "test.email@gmail.com", null, "testPass");
        userRepository.save(userEntity);
        userRepository.update(new UserEntity("testUser", "changedTestPass",
                "test.email@gmail.com", null, "changedTestPass"));
        Optional<UserEntity> foundUser = userRepository.findById(userEntity.getUserName());
        assertThat(foundUser).isPresent();
        assertEquals("changedTestPass", foundUser.get().getPassword());
    }

    @Test(expected = InputParameterValidationException.class)
    public void updateInvalidUser_ShouldThrowException() {
        UserEntity userEntity = new UserEntity("testUser", "testPass",
                "test.email@gmail.com", null, "testPass");
        userRepository.save(userEntity);
        userRepository.update(new UserEntity("anotherTestUser", "changedTestPass",
                "test.email@gmail.com", null, "changedTestPass"));
    }
}
