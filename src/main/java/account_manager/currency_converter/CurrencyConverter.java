package account_manager.currency_converter;

import account_manager.DataSourceCreator;
import account_manager.account.Account;
import account_manager.account.AccountRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashSet;
import java.util.Set;

public class CurrencyConverter {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceCreator.createDataSource());
    private AccountRepository accountRepository = new AccountRepository();

    public Set<Account> convert(ConversionDto conversionDto) {
        double amount = conversionDto.getAmount();
        Account sourceAccount = accountRepository.getById(conversionDto.getSourceAccountId());
        Account targetAccount = accountRepository.getById(conversionDto.getTargetAccountId());

        checkBalance(sourceAccount, amount);
        double exchangeRate = calculateExchangeRate(sourceAccount.getCurrencyCode(), targetAccount.getCurrencyCode());
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount * exchangeRate);
        updateBalance(sourceAccount, targetAccount);
        return getUpdatedAccounts(sourceAccount.getId(), targetAccount.getId());
    }

    private Set<Account> getUpdatedAccounts(int sourceAccountId, int targetAccountId) {
        HashSet<Account> updatedAccounts = new HashSet<>();
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

    private double calculateExchangeRate(String currencyFrom, String currencyTo) {
        Double rateFrom = jdbcTemplate.queryForObject("SELECT rate FROM currency WHERE code = ?",
                (resultSet, i) -> resultSet.getDouble("rate"), currencyFrom);
        Double rateTo = jdbcTemplate.queryForObject("SELECT rate FROM currency WHERE code = ?",
                (resultSet, i) -> resultSet.getDouble("rate"), currencyTo);
        return rateTo / rateFrom;
    }
}
