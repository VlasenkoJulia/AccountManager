package account_manager.service;

import account_manager.repository.entity.Currency;
import account_manager.repository.CurrencyRepository;
import account_manager.service.validator.CurrencyValidator;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {
   private final CurrencyRepository currencyRepository;
   private final CurrencyValidator validator;

    public CurrencyService(CurrencyRepository currencyRepository, CurrencyValidator validator) {
        this.currencyRepository = currencyRepository;
        this.validator = validator;
    }

    public Currency getByCode(String currencyCode) {
        Currency currency = currencyRepository.getCurrency(currencyCode);
        validator.validateGet(currency);
        return currency;
    }
}
