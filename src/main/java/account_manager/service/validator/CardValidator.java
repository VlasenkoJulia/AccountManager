package account_manager.service.validator;

import account_manager.repository.card.CardEntity;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Component;

@Component
public class CardValidator {
   public void validateGet(CardEntity cardEntity) {
        if (cardEntity == null) {
            throw new InputParameterValidationException("Card with passed ID do not exist");
        }
    }

   public void validateCreate(CardEntity cardEntity) {
        if (cardEntity.getId() != null) {
            throw new InputParameterValidationException("Can not provide insert operation with passed card");
        }
        if (cardEntity.getAccounts() == null || cardEntity.getAccounts().isEmpty()) {
            throw new InputParameterValidationException("Card can not be created without reference to the account(s)");
        }
    }

}
