package account_manager.currency_converter;

import account_manager.account.Account;
import account_manager.account.AccountRepository;
import account_manager.web.exception_handling.CurrencyConversionValidationException;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    public List<Account> convert(ConversionDto conversionDto)
            throws InputParameterValidationException, CurrencyConversionValidationException {
        double amount = conversionDto.getAmount();
        Account sourceAccount = accountRepository.getById(conversionDto.getSourceAccountId());
        if (sourceAccount == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
        Account targetAccount = accountRepository.getById(conversionDto.getTargetAccountId());
        if (targetAccount == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
        checkBalance(sourceAccount, amount);
        double exchangeRate = calculateExchangeRate(sourceAccount.getCurrency().getCode(), targetAccount.getCurrency().getCode());
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        double amountToAdd = Math.round((amount * exchangeRate) * 100.0) / 100.0;
        targetAccount.setBalance(targetAccount.getBalance() + amountToAdd);
        updateBalance(sourceAccount, targetAccount);
        return getUpdatedAccounts(sourceAccount.getId(), targetAccount.getId());
    }

    private List<Account> getUpdatedAccounts(int sourceAccountId, int targetAccountId) {
        List<Account> updatedAccounts = new ArrayList<>();
        updatedAccounts.add(accountRepository.getById(sourceAccountId));
        updatedAccounts.add(accountRepository.getById(targetAccountId));
        return updatedAccounts;
    }

    private void updateBalance(Account sourceAccount, Account targetAccount) throws InputParameterValidationException {
        accountRepository.update(sourceAccount);
        accountRepository.update(targetAccount);
    }

    private void checkBalance(Account sourceAccount, double amount) throws CurrencyConversionValidationException {
        if (sourceAccount.getBalance() < amount)
            throw new CurrencyConversionValidationException("Client does not have enough money for this transaction");
    }


    private double calculateExchangeRate(String sourceCurrencyCode, String targetCurrencyCode) throws InputParameterValidationException {
        Currency sourceCurrency = currencyRepository.getCurrency(sourceCurrencyCode);
        checkCurrency(sourceCurrency);
        Currency targetCurrency = currencyRepository.getCurrency(targetCurrencyCode);
        checkCurrency(targetCurrency);
        return targetCurrency.getRate() / sourceCurrency.getRate();
    }

    private void checkCurrency(Currency currency) throws InputParameterValidationException {
        if (currency == null || currency.getRate() == 0) {
            throw new InputParameterValidationException("Invalid currency rate or currency");
        }
    }
}
