package account_manager.repository.user;

import account_manager.repository.AbstractTestRepository;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class UserRepositoryTest extends AbstractTestRepository {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByEmail() {
        User user = new User("testUser", "testPass",
                "test.email@gmail.com", null, "testPass");
        userRepository.save(user);
        User foundUser = userRepository.findByEmail(user.getEmail());
        assertEquals(user, foundUser);
    }

    @Test
    public void findByResetToken() {
        User user = new User("testUser", "testPass",
                "test.email@gmail.com", "resetToken", "testPass");
        userRepository.save(user);
        User foundUser = userRepository.findByResetToken(user.getResetToken());
        assertEquals(user, foundUser);
    }

    @Test
    public void findByName() {
        User user = new User("testUser", "testPass",
                "test.email@gmail.com", null, "testPass");
        userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(user.getUserName());
        assertThat(foundUser).isPresent();
        assertEquals(user, foundUser.get());
    }

    @Test
    public void update() {
        User user = new User("testUser", "testPass",
                "test.email@gmail.com", null, "testPass");
        userRepository.save(user);
        userRepository.update(new User("testUser", "changedTestPass",
                "test.email@gmail.com", null, "changedTestPass"));
        Optional<User> foundUser = userRepository.findById(user.getUserName());
        assertThat(foundUser).isPresent();
        assertEquals("changedTestPass", foundUser.get().getPassword());
    }

    @Test(expected = InputParameterValidationException.class)
    public void updateInvalidUser_ShouldThrowException() {
        User user = new User("testUser", "testPass",
                "test.email@gmail.com", null, "testPass");
        userRepository.save(user);
        userRepository.update(new User("anotherTestUser", "changedTestPass",
                "test.email@gmail.com", null, "changedTestPass"));
    }
}
