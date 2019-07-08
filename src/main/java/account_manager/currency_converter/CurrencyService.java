package account_manager.currency_converter;

import org.springframework.stereotype.Service;

@Service
public class CurrencyService {
   private final CurrencyRepository currencyRepository;
   private final CurrencyValidator validator;

    public CurrencyService(CurrencyRepository currencyRepository, CurrencyValidator validator) {
        this.currencyRepository = currencyRepository;
        this.validator = validator;
    }

    Currency getByCode(String currencyCode) {
        Currency currency = currencyRepository.getCurrency(currencyCode);
        validator.validateGet(currency);
        return currency;
    }
}
