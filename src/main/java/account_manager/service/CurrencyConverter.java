package account_manager.service;

import account_manager.repository.account.Account;
import account_manager.repository.currency.Currency;
import account_manager.service.dto.ConversionDto;
import account_manager.web.exception_handling.CurrencyConversionValidationException;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CurrencyConverter {
    private final AccountService accountService;
    private final CurrencyService currencyService;

    @Autowired
    public CurrencyConverter(CurrencyService currencyService, AccountService accountService) {
        this.currencyService = currencyService;
        this.accountService = accountService;
    }

    @Transactional
    public void convert(ConversionDto conversionDto) {
        double amount = conversionDto.getAmount();
        Account sourceAccount = accountService.getById(conversionDto.getSourceAccountId());
        if (sourceAccount == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
        Account targetAccount = accountService.getById(conversionDto.getTargetAccountId());
        if (targetAccount == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
        checkBalance(sourceAccount, amount);
        double exchangeRate = calculateExchangeRate(sourceAccount.getCurrency().getCode(), targetAccount.getCurrency().getCode());
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        double amountToAdd = Math.round((amount * exchangeRate) * 100.0) / 100.0;
        targetAccount.setBalance(targetAccount.getBalance() + amountToAdd);
        updateBalance(sourceAccount, targetAccount);
    }

    private void updateBalance(Account sourceAccount, Account targetAccount) {
        accountService.update(sourceAccount);
        accountService.update(targetAccount);
    }

    private void checkBalance(Account sourceAccount, double amount) {
        if (sourceAccount.getBalance() < amount)
            throw new CurrencyConversionValidationException("Client does not have enough money for this transaction");
    }

    private double calculateExchangeRate(String sourceCurrencyCode, String targetCurrencyCode) {
        Currency sourceCurrency = currencyService.getByCode(sourceCurrencyCode);
        Currency targetCurrency = currencyService.getByCode(targetCurrencyCode);
        return targetCurrency.getRate() / sourceCurrency.getRate();
    }
}
