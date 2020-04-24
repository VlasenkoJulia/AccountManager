package account_manager.repository.account;

import account_manager.repository.AbstractTestRepository;
import account_manager.repository.client.Client;
import account_manager.repository.client.ClientRepository;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class AccountRepositoryTest extends AbstractTestRepository {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void create() {
        Account account = new Account(null, "111", null, AccountType.CURRENT, 1.0, null, null, Collections.emptySet());
        accountRepository.save(account);
        assertEquals(1, accountRepository.count());
    }

    @Test
    public void findById() {
        Account account = new Account(null, "111", null, AccountType.CURRENT, 1.0, null, null, Collections.emptySet());
        accountRepository.save(account);
        Optional<Account> foundAccount = accountRepository.findById(account.getId());
        assertThat(foundAccount).isPresent();
        assertEquals(account, foundAccount.get());
    }

    @Test
    public void findAccountsByClientId() {
        Client client = clientRepository.save(new Client(null, "John", "Doe"));
        Account account = new Account(null, "111", null, AccountType.CURRENT, 1.0, null, client, Collections.emptySet());
        accountRepository.save(account);
        List<Account> accounts = accountRepository.findAllByClientId(client.getId());
        assertThat(accounts).containsOnly(account);
    }

    @Test
    public void deleteById() {
        Account account = new Account(null, "111", null, AccountType.CURRENT, 1.0, null, null, Collections.emptySet());
        accountRepository.save(account);
        accountRepository.deleteById(account.getId());
        assertEquals(0, accountRepository.count());
    }

    @Test(expected = InputParameterValidationException.class)
    public void deleteInvalidAccount_ShouldThrowException() {
        Account account = new Account(null, "111", null, AccountType.CURRENT, 1.0, null, null, Collections.emptySet());
        accountRepository.save(account);
        accountRepository.deleteById(account.getId() + 1);
    }

    @Test
    public void update() {
        Account account = new Account(null, "111", null, AccountType.CURRENT, 1.0, null, null, Collections.emptySet());
        accountRepository.save(account);
        accountRepository.update(new Account(account.getId(), "1111", null, AccountType.CURRENT, 1.0, null, null, Collections.emptySet()));
        Optional<Account> updatedAccount = accountRepository.findById(account.getId());
        assertThat(updatedAccount).isPresent();
        assertEquals("1111", updatedAccount.get().getNumber());
    }

    @Test(expected = InputParameterValidationException.class)
    public void updateInvalidAccount_ShouldThrowException() {
        Account account = new Account(null, "111", null, AccountType.CURRENT, 1.0, null, null, Collections.emptySet());
        accountRepository.save(account);
        accountRepository.update(new Account(account.getId() + 1, "111", null, AccountType.CURRENT, 1.0, null, null, Collections.emptySet()));
    }
}
