package account_manager.user;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    void validateCreate(User user) {
        if (user.getUserName().equals("") || user.getUserName() == null) {
            throw new InputParameterValidationException("User name can not be empty");
        }

        if (!(user.getPassword().equals(user.getConfirmPassword()))) {
            throw new InputParameterValidationException("Password do not match");
        }
    }

    void validateGet(User user) {
        if (user == null) {
            throw new InputParameterValidationException("User with passed user name do not exist");
        }
    }
}
