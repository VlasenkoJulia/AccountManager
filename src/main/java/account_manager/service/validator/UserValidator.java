package account_manager.service.validator;

import account_manager.repository.user.UserEntity;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserValidator {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public void validateCreate(UserEntity userEntity) {
        if (userEntity.getUserName() == null || userEntity.getUserName().equals("")) {
            throw new InputParameterValidationException("User name can not be empty");
        }

        if (userEntity.getPassword() == null || userEntity.getPassword().equals("")) {
            throw new InputParameterValidationException("Password can not be empty");
        }

        if (!(userEntity.getPassword().equals(userEntity.getConfirmPassword()))) {
            throw new InputParameterValidationException("Password does not match");
        }
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(userEntity.getEmail());
        if (!matcher.find()) {
            throw new InputParameterValidationException("Invalid email");
        }
    }

    public void validateLoadByUserName(UserEntity userEntity) {
        if (userEntity == null) {
            throw new InputParameterValidationException("User with passed user name do not exist");
        }
    }

    public void validateReset(UserEntity userEntity) {
        if (userEntity == null) {
            throw new InputParameterValidationException("User is invalid");
        }
        if (!(userEntity.getPassword().equals(userEntity.getConfirmPassword()))) {
            throw new InputParameterValidationException("Password do not match");
        }
        if (userEntity.getResetToken() == null) {
            throw new InputParameterValidationException("No reset token");
        }
    }

    public void validateGetByEmail(UserEntity userEntity) {
        if (userEntity == null) {
            throw new InputParameterValidationException("User with passed email do not exist");
        }
    }

    public void validateGetByResetToken(UserEntity userEntity) {
        if (userEntity == null) {
            throw new InputParameterValidationException(" This is an invalid password reset link. "
                    + "User with passed token do not exist");
        }
    }

    public void validateSetResetToken(UserEntity userEntity) {
        if (userEntity.getResetToken() == null) {
            throw new InputParameterValidationException("User reset token can not be null");
        }
    }
}
