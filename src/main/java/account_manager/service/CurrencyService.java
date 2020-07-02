package account_manager.service;

import account_manager.repository.currency.CurrencyEntity;
import account_manager.repository.currency.CurrencyRepository;
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

    public CurrencyEntity getByCode(String currencyCode) {
        CurrencyEntity currency = currencyRepository.findById(currencyCode).orElse(null);
        validator.validateGet(currency);
        return currency;
    }
}
