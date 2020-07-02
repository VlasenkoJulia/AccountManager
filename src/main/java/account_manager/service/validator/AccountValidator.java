package account_manager.service.validator;

import account_manager.repository.account.AccountEntity;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Component;

@Component
public class AccountValidator {
    public void validateGet(AccountEntity accountEntity) {
        if (accountEntity == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
    }

    public void validateCreate(AccountEntity accountEntity) {
        if (accountEntity.getId() != null) {
            throw new InputParameterValidationException("Can not provide insert operation with passed account");
        }
        if (accountEntity.getCurrency() == null || accountEntity.getCurrency().getCode().equals("")) {
            throw new InputParameterValidationException("Missed account currency");
        }
        if (accountEntity.getType() == null) {
            throw new InputParameterValidationException("Missed account type");
        }
    }

    public void validateUpdate(AccountEntity accountEntity) {
        if (accountEntity.getId() == null) {
            throw new InputParameterValidationException("Can not provide setResetToken operation with passed account");
        }
    }

    public void validateGetByClientId(Integer clientId) {
        if (clientId == null) {
            throw new InputParameterValidationException("Client Id can not be null");
        }
    }
}
