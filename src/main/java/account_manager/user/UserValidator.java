package account_manager.user;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserValidator {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    void validateCreate(User user) {
        if (user.getUserName() == null || user.getUserName().equals("")) {
            throw new InputParameterValidationException("User name can not be empty");
        }

        if (user.getPassword() == null || user.getPassword().equals("")) {
            throw new InputParameterValidationException("Password can not be empty");
        }

        if (!(user.getPassword().equals(user.getConfirmPassword()))) {
            throw new InputParameterValidationException("Password does not match");
        }
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(user.getEmail());
        if (!matcher.find()) {
            throw new InputParameterValidationException("Invalid email");
        }
    }

    void validateLoadByUserName(User user) {
        if (user == null) {
            throw new InputParameterValidationException("User with passed user name do not exist");
        }
    }

    void validateReset(User user) {
        if (user == null) {
            throw new InputParameterValidationException("User is invalid");
        }
        if (!(user.getPassword().equals(user.getConfirmPassword()))) {
            throw new InputParameterValidationException("Password do not match");
        }
        if (user.getResetToken() == null) {
            throw new InputParameterValidationException("No reset token");
        }
    }

    void validateGetByEmail(User user) {
        if (user == null) {
            throw new InputParameterValidationException("User with passed email do not exist");
        }
    }

    void validateGetByResetToken(User user) {
        if (user == null) {
            throw new InputParameterValidationException(" This is an invalid password reset link. "
                    + "User with passed token do not exist");
        }
    }

    void validateSetResetToken(User user) {
        if (user.getResetToken() == null) {
            throw new InputParameterValidationException("User reset token can not be null");
        }
    }
}
