package account_manager.service;

import account_manager.repository.currency.CurrencyEntity;
import account_manager.service.dto.AccountDto;
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
        AccountDto sourceAccount = accountService.getById(conversionDto.getSourceAccountId());
        if (sourceAccount == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
        AccountDto targetAccount = accountService.getById(conversionDto.getTargetAccountId());
        if (targetAccount == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
        checkBalance(sourceAccount, amount);
        double exchangeRate = calculateExchangeRate(sourceAccount.getCurrencyCode(),
                targetAccount.getCurrencyCode());
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        double amountToAdd = Math.round((amount * exchangeRate) * 100.0) / 100.0;
        targetAccount.setBalance(targetAccount.getBalance() + amountToAdd);
        updateBalance(sourceAccount, targetAccount);
    }

    private void updateBalance(AccountDto sourceAccount, AccountDto targetAccount) {
        accountService.update(sourceAccount);
        accountService.update(targetAccount);
    }

    private void checkBalance(AccountDto sourceAccount, double amount) {
        if (sourceAccount.getBalance() < amount)
            throw new CurrencyConversionValidationException("Client does not have enough money for this transaction");
    }

    private double calculateExchangeRate(String sourceCurrencyCode, String targetCurrencyCode) {
        CurrencyEntity sourceCurrency = currencyService.getByCode(sourceCurrencyCode);
        CurrencyEntity targetCurrency = currencyService.getByCode(targetCurrencyCode);
        return targetCurrency.getRate() / sourceCurrency.getRate();
    }
}
