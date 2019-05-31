package account_manager.currency_converter;

import account_manager.account.Account;
import account_manager.account.AccountRepository;
import account_manager.web.exception_handling.CurrencyConversionValidationException;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CurrencyConverter {
    private final AccountRepository accountRepository;
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyConverter(CurrencyRepository currencyRepository, AccountRepository accountRepository) {
        this.currencyRepository = currencyRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void convert(ConversionDto conversionDto) {

        double amount = conversionDto.getAmount();
        if (amount < 0) {
            throw new InputParameterValidationException("Passed amount less than 0");
        }

        Integer sourceAccountId = conversionDto.getSourceAccountId();
        if (sourceAccountId == null || sourceAccountId <= 0) {
            throw new InputParameterValidationException("Passed source account ID can not be null or less/equal 0");
        }

        Account sourceAccount = accountRepository.getById(sourceAccountId);
        if (sourceAccount == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }

        Integer targetAccountId = conversionDto.getTargetAccountId();
        if (targetAccountId == null || targetAccountId <= 0) {
            throw new InputParameterValidationException("Passed target account ID can not be null or less/equal 0");
        }

        Account targetAccount = accountRepository.getById(targetAccountId);
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
        accountRepository.update(sourceAccount);
        accountRepository.update(targetAccount);
    }

    private void checkBalance(Account sourceAccount, double amount) {
        if (sourceAccount.getBalance() < amount)
            throw new CurrencyConversionValidationException("Client does not have enough money for this transaction");
    }

    private double calculateExchangeRate(String sourceCurrencyCode, String targetCurrencyCode) {
        Currency sourceCurrency = currencyRepository.getCurrency(sourceCurrencyCode);
        checkCurrency(sourceCurrency);
        Currency targetCurrency = currencyRepository.getCurrency(targetCurrencyCode);
        checkCurrency(targetCurrency);
        return targetCurrency.getRate() / sourceCurrency.getRate();
    }

    private void checkCurrency(Currency currency) {
        if (currency == null || currency.getRate() <= 0) {
            throw new InputParameterValidationException("Invalid currency rate or currency");
        }
    }
}
