package account_manager.user;

import account_manager.repository.entity.User;
import account_manager.service.validator.UserValidator;
import account_manager.web.exception_handling.InputParameterValidationException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class UserValidatorTest {
    private UserValidator validator;

    @Before
    public void setup() {
        validator = new UserValidator();
    }

    private static Object[] getInvalidField() {
        return new String[][]{{""}, {null}};
    }

    @Test(expected = InputParameterValidationException.class)
    @Parameters(method = "getInvalidField")
    public void validateCreate_UserNameIsInvalid_ShouldThrowException(String invalidUserName) {
        User user = createUser(invalidUserName);
        validator.validateCreate(user);
    }

    @Test(expected = InputParameterValidationException.class)
    @Parameters(method = "getInvalidField")
    public void validateCreate_PasswordIsInvalid_ShouldThrowException(String invalidPassword) {
        User user = createUser("username", invalidPassword);
        validator.validateCreate(user);
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateCreate_PasswordDoesNotMatch_ShouldThrowException() {
        User user = createUser("username", "password", "pass");
        validator.validateCreate(user);
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateCreate_EmailIsInvalid_ShouldThrowException() {
        User user = createUser("username", "password", "password", "email");
        validator.validateCreate(user);
    }

    @Test
    public void validateCreate_UserIsValid() {
        User user = createUser("username", "password", "password", "email@demo.com");
        validator.validateCreate(user);
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateLoadByUserName_UserIsNull_ShouldThrowException() {
        validator.validateLoadByUserName(null);
    }

    @Test
    public void validateLoadByUserName_UserValid() {
        User user = createUser("username", "password", "password", "email@demo.com");
        validator.validateLoadByUserName(user);
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateReset_UserIsNull_ShouldThrowException() {
        validator.validateReset(null);
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateReset_PasswordDoesNotMatch_ShouldThrowException() {
        User user = createUser("username", "password", "pass");
        validator.validateReset(user);
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateReset_NoResetToken_ShouldThrowException() {
        User user = createUser("username", "password", "password", "email@demo.com", null);
        validator.validateReset(user);
    }

    @Test
    public void validateReset_ValidUser() {
        User user = createUser("username", "password", "password", "email@demo.com", "token");
        validator.validateReset(user);
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateGetByEmail_UserIsNull_ShouldThrowException() {
        validator.validateGetByEmail(null);
    }

    @Test
    public void validateGetByEmail_UserFound() {
        User user = createUser("username", "password", "password", "email@demo.com");
        validator.validateGetByEmail(user);
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateGetByResetToken_UserIsNull_ShouldThrowException() {
        validator.validateGetByResetToken(null);
    }

    @Test
    public void validateGetByResetToken_UserFound() {
        User user = createUser("username", "password", "password", "email@demo.com", "token");
        validator.validateGetByResetToken(user);
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateSetResetToken_UserIsNull_ShouldThrowException() {
        User user = createUser("username", "password", "password", "email@demo.com", null);
        validator.validateSetResetToken(user);
    }

    @Test
    public void validateSetResetToken_UserFound() {
        User user = createUser("username", "password", "password", "email@demo.com", "token");
        validator.validateSetResetToken(user);
    }

    private User createUser(String userName) {
        User user = new User();
        user.setUserName(userName);
        return user;
    }

    private User createUser(String userName, String password) {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        return user;
    }

    private User createUser(String userName, String password, String confirmedPassword) {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setConfirmPassword(confirmedPassword);
        return user;
    }

    private User createUser(String userName, String password, String confirmedPassword, String email) {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setConfirmPassword(confirmedPassword);
        user.setEmail(email);
        return user;
    }

    private User createUser(String userName, String password, String confirmedPassword, String email, String token) {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setConfirmPassword(confirmedPassword);
        user.setEmail(email);
        user.setResetToken(token);
        return user;
    }
}
