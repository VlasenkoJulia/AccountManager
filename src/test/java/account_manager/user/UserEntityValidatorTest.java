package account_manager.user;

import account_manager.repository.user.UserEntity;
import account_manager.service.validator.UserValidator;
import account_manager.web.exception_handling.InputParameterValidationException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class UserEntityValidatorTest {
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
        UserEntity userEntity = createUser(invalidUserName);
        validator.validateCreate(userEntity);
    }

    @Test(expected = InputParameterValidationException.class)
    @Parameters(method = "getInvalidField")
    public void validateCreate_PasswordIsInvalid_ShouldThrowException(String invalidPassword) {
        UserEntity userEntity = createUser("username", invalidPassword);
        validator.validateCreate(userEntity);
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateCreate_PasswordDoesNotMatch_ShouldThrowException() {
        UserEntity userEntity = createUser("username", "password", "pass");
        validator.validateCreate(userEntity);
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateCreate_EmailIsInvalid_ShouldThrowException() {
        UserEntity userEntity = createUser("username", "password", "password", "email");
        validator.validateCreate(userEntity);
    }

    @Test
    public void validateCreate_UserIsValid() {
        UserEntity userEntity = createUser("username", "password", "password", "email@demo.com");
        validator.validateCreate(userEntity);
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateLoadByUserName_UserIsNull_ShouldThrowException() {
        validator.validateLoadByUserName(null);
    }

    @Test
    public void validateLoadByUserName_UserValid() {
        UserEntity userEntity = createUser("username", "password", "password", "email@demo.com");
        validator.validateLoadByUserName(userEntity);
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateReset_UserIsNull_ShouldThrowException() {
        validator.validateReset(null);
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateReset_PasswordDoesNotMatch_ShouldThrowException() {
        UserEntity userEntity = createUser("username", "password", "pass");
        validator.validateReset(userEntity);
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateReset_NoResetToken_ShouldThrowException() {
        UserEntity userEntity = createUser("username", "password", "password", "email@demo.com", null);
        validator.validateReset(userEntity);
    }

    @Test
    public void validateReset_ValidUser() {
        UserEntity userEntity = createUser("username", "password", "password", "email@demo.com", "token");
        validator.validateReset(userEntity);
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateGetByEmail_UserIsNull_ShouldThrowException() {
        validator.validateGetByEmail(null);
    }

    @Test
    public void validateGetByEmail_UserFound() {
        UserEntity userEntity = createUser("username", "password", "password", "email@demo.com");
        validator.validateGetByEmail(userEntity);
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateGetByResetToken_UserIsNull_ShouldThrowException() {
        validator.validateGetByResetToken(null);
    }

    @Test
    public void validateGetByResetToken_UserFound() {
        UserEntity userEntity = createUser("username", "password", "password", "email@demo.com", "token");
        validator.validateGetByResetToken(userEntity);
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateSetResetToken_UserIsNull_ShouldThrowException() {
        UserEntity userEntity = createUser("username", "password", "password", "email@demo.com", null);
        validator.validateSetResetToken(userEntity);
    }

    @Test
    public void validateSetResetToken_UserFound() {
        UserEntity userEntity = createUser("username", "password", "password", "email@demo.com", "token");
        validator.validateSetResetToken(userEntity);
    }

    private UserEntity createUser(String userName) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        return userEntity;
    }

    private UserEntity createUser(String userName, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setPassword(password);
        return userEntity;
    }

    private UserEntity createUser(String userName, String password, String confirmedPassword) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setPassword(password);
        userEntity.setConfirmPassword(confirmedPassword);
        return userEntity;
    }

    private UserEntity createUser(String userName, String password, String confirmedPassword, String email) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setPassword(password);
        userEntity.setConfirmPassword(confirmedPassword);
        userEntity.setEmail(email);
        return userEntity;
    }

    private UserEntity createUser(String userName, String password, String confirmedPassword, String email, String token) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setPassword(password);
        userEntity.setConfirmPassword(confirmedPassword);
        userEntity.setEmail(email);
        userEntity.setResetToken(token);
        return userEntity;
    }
}
