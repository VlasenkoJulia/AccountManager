package account_manager.currency_converter;

import account_manager.account.Account;
import account_manager.account.AccountRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CurrencyConverter {
    private AccountRepository accountRepository = new AccountRepository();
    private CurrencyRepository currencyRepository = new CurrencyRepository();

    public List<Account> convert(ConversionDto conversionDto) {
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
