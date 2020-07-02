package account_manager.user;

import account_manager.repository.user.UserEntity;
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

public class UserEntityServiceTest {
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
        UserEntity userEntity = new UserEntity();
        doThrow(InputParameterValidationException.class).when(validator).validateCreate(userEntity);
        userService.create(userEntity);
    }

    @Test(expected = InputParameterValidationException.class)
    public void create_UserWithUserNameExist_ShouldThrowException() {
        UserEntity userEntity = createUser("username", "password", "email", null );
        doNothing().when(validator).validateCreate(userEntity);
        when(userRepository.findById("username")).thenReturn(Optional.of(userEntity));
        userService.create(userEntity);
    }

    @Test(expected = InputParameterValidationException.class)
    public void create_UserWithEmailExist_ShouldThrowException() {
        UserEntity userEntity = createUser("username", "password", "email", null );
        doNothing().when(validator).validateCreate(userEntity);
        when(userRepository.findById("username")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("email")).thenReturn(userEntity);
        userService.create(userEntity);
    }

    @Test
    public void create_UserIsValid_ShouldReturnSuccessMessage() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("username");
        userEntity.setEmail("email");
        doNothing().when(validator).validateCreate(userEntity);
        when(userRepository.findById("username")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("email")).thenReturn(null);
        String message = userService.create(userEntity);
        assertEquals("User created successfully! You can return to login page and log in!", message);
    }

    @Test
    public void create_UserIsValid_CheckIfPasswordEncoded() {
        UserEntity userEntity = createUser("username", "password", "email", null );
        doNothing().when(validator).validateCreate(userEntity);
        when(userRepository.findById("username")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("email")).thenReturn(null);
        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        userService.create(userEntity);
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
        UserEntity userEntity = createUser("username", "password", "email", null );
        when(userRepository.findByEmail("email")).thenReturn(userEntity);
        doNothing().when(validator).validateGetByEmail(userEntity);
        UserEntity userEntityFound = userService.getByEmail("email");
        assertEquals(userEntity, userEntityFound);
    }

    @Test(expected = InputParameterValidationException.class)
    public void getByResetToken_UserWithTokenDoNotExist_ShouldThrowException() {
        when(userRepository.findByResetToken("token")).thenReturn(null);
        doThrow(InputParameterValidationException.class).when(validator).validateGetByResetToken(null);
        userService.getByResetToken("token");
    }

    @Test
    public void getByResetToken_UserFound_ShouldReturnUser() {
        UserEntity userEntity = createUser("username", "password", "email", "token" );
        when(userRepository.findByResetToken("token")).thenReturn(userEntity);
        doNothing().when(validator).validateGetByResetToken(userEntity);
        UserEntity userEntityFound = userService.getByResetToken("token");
        assertEquals(userEntity, userEntityFound);
    }

    @Test(expected = InputParameterValidationException.class)
    public void resetPassword_NewPasswordNotValid_ShouldThrowException() {
        UserEntity userEntity = createUser("username", "password", "email", "token" );
        doThrow(InputParameterValidationException.class).when(validator).validateReset(userEntity);
        userService.resetPassword(userEntity);
    }

    @Test(expected = InputParameterValidationException.class)
    public void resetPassword_UserWithoutResetToken_ShouldThrowException() {
        UserEntity userEntity = createUser("username", "password", "email", null );
        doThrow(InputParameterValidationException.class).when(validator).validateReset(userEntity);
        userService.resetPassword(userEntity);
    }

    @Test(expected = InputParameterValidationException.class)
    public void resetPassword_NoUserWithPassedResetToken_ShouldThrowException() {
        UserEntity userEntity = createUser("username", "password", "email", "token");
        doNothing().when(validator).validateReset(userEntity);
        when(userRepository.findByResetToken("token")).thenReturn(null);
        doThrow(InputParameterValidationException.class).when(validator).validateGetByResetToken(null);
        userService.resetPassword(userEntity);
    }

    @Test
    public void resetPassword_UserIsValid_ShouldReturnSuccessMessage() {
        UserEntity userEntity = createUser("username", "password", "email", "token" );
        doNothing().when(validator).validateReset(userEntity);
        when(userRepository.findByResetToken("token")).thenReturn(userEntity);
        doNothing().when(validator).validateGetByResetToken(userEntity);
        String message = userService.resetPassword(userEntity);
        assertEquals("Password changed successfully", message);
    }

    @Test
    public void resetPassword_UserIsValid_CheckIfPasswordEncodedAndTokenSetNull() {
        UserEntity userEntity = createUser("username", "password", "email", "token" );
        doNothing().when(validator).validateReset(userEntity);
        when(userRepository.findByResetToken("token")).thenReturn(userEntity);
        doNothing().when(validator).validateGetByResetToken(userEntity);
        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        userService.resetPassword(userEntity);
        verify(userRepository).update(refEq(createUser("username", encodedPassword, "email", null )));
    }


    @Test(expected = InputParameterValidationException.class)
    public void setResetToken_UserWithoutResetToken_ShouldThrowException() {
        UserEntity userEntity = createUser("username", "password", "email", null );
        doThrow(InputParameterValidationException.class).when(validator).validateSetResetToken(userEntity);
        userService.setResetToken(userEntity);
    }

    @Test
    public void setResetToken_UserIsValid_ShouldReturnSuccessMessage() {
        UserEntity userEntity = createUser("username", "password", "email", "token");
        doNothing().when(validator).validateSetResetToken(userEntity);
        String message = userService.setResetToken(userEntity);
        assertEquals("Set token to user username", message);
    }

    private UserEntity createUser(String userName,
                                  String password,
                                  String email,
                                  String resetToken) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setEmail(email);
        userEntity.setPassword(password);
        userEntity.setPassword(resetToken);
        return userEntity;
    }
}
