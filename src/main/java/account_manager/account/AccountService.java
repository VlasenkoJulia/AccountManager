package account_manager.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountValidator validator;

    @Autowired
    public AccountService(AccountRepository accountRepository, AccountValidator validator) {
        this.accountRepository = accountRepository;
        this.validator = validator;
    }

    public Account getById(Integer accountId) {
        Account account = accountRepository.getById(accountId);
        validator.validateGet(account);
        return account;
    }

    public String create(Account account) {
        validator.validateCreate(account);
        Account accountCreated = accountRepository.create(account);
        return "Created account #" + accountCreated.getId();
    }

    public String update(Account account) {
        validator.validateUpdate(account);
        accountRepository.update(account);
        return "Account updated successfully";
    }

    public String delete(Integer accountId) {
        accountRepository.deleteById(accountId);
        return "Deleted account #" + accountId;
    }

    public List<Account> getByClientId(Integer clientId) {
        validator.validateGetByClientId(clientId);
        return accountRepository.getByClientId(clientId);
    }
}
