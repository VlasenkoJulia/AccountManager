package account_manager.repository.account;

import account_manager.repository.AbstractTestRepository;
import account_manager.repository.client.ClientEntity;
import account_manager.repository.client.ClientRepository;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class AccountEntityRepositoryTest extends AbstractTestRepository {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void create() {
        AccountEntity accountEntity = new AccountEntity(null, "111", null, AccountType.CURRENT, 1.0, null, null, Collections.emptySet());
        accountRepository.save(accountEntity);
        Optional<AccountEntity> foundAccount = accountRepository.findById(accountEntity.getId());
        assertThat(foundAccount).isPresent();
        assertEquals(accountEntity, foundAccount.get());
    }

    @Test
    public void findById() {
        AccountEntity accountEntity = new AccountEntity(null, "111", null, AccountType.CURRENT, 1.0, null, null, Collections.emptySet());
        accountRepository.save(accountEntity);
        Optional<AccountEntity> foundAccount = accountRepository.findById(accountEntity.getId());
        assertThat(foundAccount).isPresent();
        assertEquals(accountEntity, foundAccount.get());
    }

    @Test
    public void findAccountsByClientId() {
        ClientEntity clientEntity = clientRepository.save(new ClientEntity(null, "John", "Doe"));
        AccountEntity accountEntity = new AccountEntity(null, "111", null, AccountType.CURRENT, 1.0, null, clientEntity, Collections.emptySet());
        accountRepository.save(accountEntity);
        List<AccountEntity> accountEntities = accountRepository.findAllByClientId(clientEntity.getId());
        assertThat(accountEntities).containsOnly(accountEntity);
    }

    @Test
    public void deleteById() {
        AccountEntity accountEntity = new AccountEntity(null, "111", null, AccountType.CURRENT, 1.0, null, null, Collections.emptySet());
        accountRepository.save(accountEntity);
        accountRepository.deleteById(accountEntity.getId());
        assertEquals(0, accountRepository.count());
    }

    @Test(expected = InputParameterValidationException.class)
    public void deleteInvalidAccount_ShouldThrowException() {
        AccountEntity accountEntity = new AccountEntity(null, "111", null, AccountType.CURRENT, 1.0, null, null, Collections.emptySet());
        accountRepository.save(accountEntity);
        accountRepository.deleteById(accountEntity.getId() + 1);
    }

    @Test
    public void update() {
        AccountEntity accountEntity = new AccountEntity(null, "111", null, AccountType.CURRENT, 1.0, null, null, Collections.emptySet());
        accountRepository.save(accountEntity);
        accountRepository.update(new AccountEntity(accountEntity.getId(), "1111", null, AccountType.CURRENT, 1.0, null, null, Collections.emptySet()));
        Optional<AccountEntity> updatedAccount = accountRepository.findById(accountEntity.getId());
        assertThat(updatedAccount).isPresent();
        assertEquals("1111", updatedAccount.get().getNumber());
    }

    @Test(expected = InputParameterValidationException.class)
    public void updateInvalidAccount_ShouldThrowException() {
        AccountEntity accountEntity = new AccountEntity(null, "111", null, AccountType.CURRENT, 1.0, null, null, Collections.emptySet());
        accountRepository.save(accountEntity);
        accountRepository.update(new AccountEntity(accountEntity.getId() + 1, "111", null, AccountType.CURRENT, 1.0, null, null, Collections.emptySet()));
    }
}
