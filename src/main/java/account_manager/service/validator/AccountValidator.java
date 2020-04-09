package account_manager.service.validator;

import account_manager.repository.account.Account;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Component;

@Component
public class AccountValidator {
    public void validateGet(Account account) {
        if (account == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
    }

    public void validateCreate(Account account) {
        if (account.getId() != null) {
            throw new InputParameterValidationException("Can not provide insert operation with passed account");
        }
        if (account.getCurrency() == null || account.getCurrency().getCode().equals("")) {
            throw new InputParameterValidationException("Missed account currency");
        }
        if (account.getType() == null) {
            throw new InputParameterValidationException("Missed account type");
        }
    }

    public void validateUpdate(Account account) {
        if (account.getId() == null) {
            throw new InputParameterValidationException("Can not provide setResetToken operation with passed account");
        }
    }

    public void validateGetByClientId(Integer clientId) {
        if (clientId == null) {
            throw new InputParameterValidationException("Client Id can not be null");
        }
    }
}
