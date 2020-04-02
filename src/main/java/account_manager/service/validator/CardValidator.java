package account_manager.service.validator;

import account_manager.repository.entity.Card;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Component;

@Component
public class CardValidator {
   public void validateGet(Card card) {
        if (card == null) {
            throw new InputParameterValidationException("Card with passed ID do not exist");
        }
    }

   public void validateCreate(Card card) {
        if (card.getId() != null) {
            throw new InputParameterValidationException("Can not provide insert operation with passed card");
        }
        if (card.getAccounts() == null || card.getAccounts().isEmpty()) {
            throw new InputParameterValidationException("Card can not be created without reference to the account(s)");
        }
    }

}
