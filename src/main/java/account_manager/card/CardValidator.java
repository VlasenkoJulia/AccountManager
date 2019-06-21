package account_manager.card;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Component;

@Component
public class CardValidator {
    void validateGet(Card card) {
        if (card == null) {
            throw new InputParameterValidationException("Card with passed ID do not exist");
        }
    }

    void validateCreate(Card card) {
        if (card.getId() != null) {
            throw new InputParameterValidationException("Can not provide insert operation with passed card");
        }
        if (card.getAccounts() == null || card.getAccounts().isEmpty()) {
            throw new InputParameterValidationException("Card can not be created without reference to the account(s)");
        }
    }

}
