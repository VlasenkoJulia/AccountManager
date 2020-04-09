package account_manager.user;

import account_manager.repository.user.User;
import account_manager.repository.user.UserRepository;
import account_manager.service.UserService;
import account_manager.service.validator.UserValidator;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    UserValidator validator;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = InputParameterValidationException.class)
    public void create_UserNotValid_ShouldThrowException() {
        User user = new User();
        doThrow(InputParameterValidationException.class).when(validator).validateCreate(user);
        userService.create(user);
    }

    @Test(expected = InputParameterValidationException.class)
    public void create_UserWithUserNameExist_ShouldThrowException() {
        User user = createUser("username", "password", "email", null );
        doNothing().when(validator).validateCreate(user);
        when(userRepository.findById("username")).thenReturn(Optional.of(user));
        userService.create(user);
    }

    @Test(expected = InputParameterValidationException.class)
    public void create_UserWithEmailExist_ShouldThrowException() {
        User user = createUser("username", "password", "email", null );
        doNothing().when(validator).validateCreate(user);
        when(userRepository.findById("username")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("email")).thenReturn(user);
        userService.create(user);
    }

    @Test
    public void create_UserIsValid_ShouldReturnSuccessMessage() {
        User user = new User();
        user.setUserName("username");
        user.setEmail("email");
        doNothing().when(validator).validateCreate(user);
        when(userRepository.findById("username")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("email")).thenReturn(null);
        String message = userService.create(user);
        assertEquals("User created successfully! You can return to login page and log in!", message);
    }

    @Test
    public void create_UserIsValid_CheckIfPasswordEncoded() {
        User user = createUser("username", "password", "email", null );
        doNothing().when(validator).validateCreate(user);
        when(userRepository.findById("username")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("email")).thenReturn(null);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        userService.create(user);
        verify(userRepository).save(refEq(createUser("username", encodedPassword, "email", null )));
    }

    @Test(expected = InputParameterValidationException.class)
    public void getByEmail_UserWithEmailDoNotExist_ShouldThrowException() {
        when(userRepository.findByEmail("email")).thenReturn(null);
        doThrow(InputParameterValidationException.class).when(validator).validateGetByEmail(null);
        userService.getByEmail("email");
    }

    @Test
    public void getByEmail_UserFound_ShouldReturnUser() {
        User user = createUser("username", "password", "email", null );
        when(userRepository.findByEmail("email")).thenReturn(user);
        doNothing().when(validator).validateGetByEmail(user);
        User userFound = userService.getByEmail("email");
        assertEquals(user, userFound);
    }

    @Test(expected = InputParameterValidationException.class)
    public void getByResetToken_UserWithTokenDoNotExist_ShouldThrowException() {
        when(userRepository.findByResetToken("token")).thenReturn(null);
        doThrow(InputParameterValidationException.class).when(validator).validateGetByResetToken(null);
        userService.getByResetToken("token");
    }

    @Test
    public void getByResetToken_UserFound_ShouldReturnUser() {
        User user = createUser("username", "password", "email", "token" );
        when(userRepository.findByResetToken("token")).thenReturn(user);
        doNothing().when(validator).validateGetByResetToken(user);
        User userFound = userService.getByResetToken("token");
        assertEquals(user, userFound);
    }

    @Test(expected = InputParameterValidationException.class)
    public void resetPassword_NewPasswordNotValid_ShouldThrowException() {
        User user = createUser("username", "password", "email", "token" );
        doThrow(InputParameterValidationException.class).when(validator).validateReset(user);
        userService.resetPassword(user);
    }

    @Test(expected = InputParameterValidationException.class)
    public void resetPassword_UserWithoutResetToken_ShouldThrowException() {
        User user = createUser("username", "password", "email", null );
        doThrow(InputParameterValidationException.class).when(validator).validateReset(user);
        userService.resetPassword(user);
    }

    @Test(expected = InputParameterValidationException.class)
    public void resetPassword_NoUserWithPassedResetToken_ShouldThrowException() {
        User user = createUser("username", "password", "email", "token");
        doNothing().when(validator).validateReset(user);
        when(userRepository.findByResetToken("token")).thenReturn(null);
        doThrow(InputParameterValidationException.class).when(validator).validateGetByResetToken(null);
        userService.resetPassword(user);
    }

    @Test
    public void resetPassword_UserIsValid_ShouldReturnSuccessMessage() {
        User user = createUser("username", "password", "email", "token" );
        doNothing().when(validator).validateReset(user);
        when(userRepository.findByResetToken("token")).thenReturn(user);
        doNothing().when(validator).validateGetByResetToken(user);
        String message = userService.resetPassword(user);
        assertEquals("Password changed successfully", message);
    }

    @Test
    public void resetPassword_UserIsValid_CheckIfPasswordEncodedAndTokenSetNull() {
        User user = createUser("username", "password", "email", "token" );
        doNothing().when(validator).validateReset(user);
        when(userRepository.findByResetToken("token")).thenReturn(user);
        doNothing().when(validator).validateGetByResetToken(user);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        userService.resetPassword(user);
        verify(userRepository).update(refEq(createUser("username", encodedPassword, "email", null )));
    }


    @Test(expected = InputParameterValidationException.class)
    public void setResetToken_UserWithoutResetToken_ShouldThrowException() {
        User user = createUser("username", "password", "email", null );
        doThrow(InputParameterValidationException.class).when(validator).validateSetResetToken(user);
        userService.setResetToken(user);
    }

    @Test
    public void setResetToken_UserIsValid_ShouldReturnSuccessMessage() {
        User user = createUser("username", "password", "email", "token");
        doNothing().when(validator).validateSetResetToken(user);
        String message = userService.setResetToken(user);
        assertEquals("Set token to user username", message);
    }

    private User createUser(String userName,
                            String password,
                            String email,
                            String resetToken) {
        User user = new User();
        user.setUserName(userName);
        user.setEmail(email);
        user.setPassword(password);
        user.setPassword(resetToken);
        return user;
    }
}
