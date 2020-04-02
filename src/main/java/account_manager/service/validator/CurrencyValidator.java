package account_manager.service.validator;

import account_manager.repository.entity.Currency;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Component;

@Component
public class CurrencyValidator {

    public void validateGet(Currency currency) {
        if (currency == null) {
            throw new InputParameterValidationException("Currency not found");
        }

        if (currency.getRate() <= 0) {
            throw new InputParameterValidationException("Currency rate could not be equal or less than 0");
        }
    }
}
