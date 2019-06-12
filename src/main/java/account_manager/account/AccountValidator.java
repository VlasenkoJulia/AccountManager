package account_manager.account;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Component;

@Component
public class AccountValidator {
    void validateGet(Account account) {
        if (account == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
    }

    void validateCreate(Account account) {
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

    void validateUpdate(Account account) {
        if (account.getId() == null) {
            throw new InputParameterValidationException("Can not provide update operation with passed account");
        }
    }

    void validateGetByClientId(Integer clientId) {
        if (clientId == null) {
            throw new InputParameterValidationException("Client Id can not be null");
        }
    }
}