package account_manager.currency_converter;

import account_manager.account.Account;
import account_manager.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    List<Account> convert(ConversionDto conversionDto) {
        double amount = conversionDto.getAmount();
        Account sourceAccount = accountRepository.getById(conversionDto.getSourceAccountId());
        Account targetAccount = accountRepository.getById(conversionDto.getTargetAccountId());

        checkBalance(sourceAccount, amount);
        double exchangeRate = currencyRepository.calculateExchangeRate(sourceAccount.getCurrencyCode(), targetAccount.getCurrencyCode());
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount * exchangeRate);
        updateBalance(sourceAccount, targetAccount);
        return getUpdatedAccounts(sourceAccount.getId(), targetAccount.getId());
    }

    private List<Account> getUpdatedAccounts(int sourceAccountId, int targetAccountId) {
        List<Account> updatedAccounts = new ArrayList<>();
        updatedAccounts.add(accountRepository.getById(sourceAccountId));
        updatedAccounts.add(accountRepository.getById(targetAccountId));
        return updatedAccounts;
    }

    private void updateBalance(Account sourceAccount, Account targetAccount) {
        accountRepository.update(sourceAccount);
        accountRepository.update(targetAccount);
    }

    private void checkBalance(Account sourceAccount, double amount) {
        if (sourceAccount.getBalance() < amount)
            throw new RuntimeException("Client does not have enough money for this transaction");
    }
}
