package account_manager.service.validator;

import account_manager.repository.currency.CurrencyEntity;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Component;

@Component
public class CurrencyValidator {

    public void validateGet(CurrencyEntity currencyEntity) {
        if (currencyEntity == null) {
            throw new InputParameterValidationException("Currency not found");
        }

        if (currencyEntity.getRate() <= 0) {
            throw new InputParameterValidationException("Currency rate could not be equal or less than 0");
        }
    }
}
