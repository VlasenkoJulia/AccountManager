package account_manager.account;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class AccountServiceTest {
    @Mock
    AccountRepository accountRepository;
    @Mock
    AccountValidator validator;
    @InjectMocks
    AccountService accountService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = InputParameterValidationException.class)
    public void getById_AccountNotFound_ShouldThrowException() {
        when(accountRepository.getById(1)).thenReturn(null);
        doThrow(InputParameterValidationException.class).when(validator).validateGet(null);
        accountService.getById(1);
    }

    @Test
    public void getById_AccountFound_ShouldReturnAccount() {
        Account account = createAccount(1);
        when(accountRepository.getById(1)).thenReturn(account);
        doNothing().when(validator).validateGet(account);
        Account accountFound = accountService.getById(1);
        Assert.assertEquals(account, accountFound);
    }

    @Test(expected = InputParameterValidationException.class)
    public void create_AccountIsNotValid_ShouldThrowException() {
        Account account = createAccount(1);
        doThrow(InputParameterValidationException.class).when(validator).validateCreate(account);
        accountService.create(account);
    }

    @Test
    public void create_AccountValid_ShouldReturnSuccessMessage() {
        Account account = createAccount(null);
        doNothing().when(validator).validateCreate(account);
        when(accountRepository.create(account)).thenReturn(createAccount(1));
        String message = accountService.create(account);
        Assert.assertEquals("Created account #1", message);
    }

    @Test(expected = InputParameterValidationException.class)
    public void update_AccountIsNotValid_ShouldThrowException() {
        Account account = createAccount(null);
        doThrow(InputParameterValidationException.class).when(validator).validateUpdate(account);
        accountService.update(account);
    }

    @Test
    public void update_AccountValid_ShouldReturnSuccessMessage() {
        Account account = createAccount(1);
        doNothing().when(validator).validateUpdate(account);
        doNothing().when(accountRepository).update(account);
        String message = accountService.update(account);
        Assert.assertEquals("Account updated successfully", message);
    }

    @Test
    public void delete_ClientIdIsValid_ShouldReturnSuccessMessage() {
        doNothing().when(accountRepository).deleteById(1);
        String message = accountService.delete(1);
        Assert.assertEquals("Deleted account #1", message);
    }

    @Test(expected = InputParameterValidationException.class)
    public void getByClientId_ClientIdIsNotValid_ShouldThrowException() {
        doThrow(InputParameterValidationException.class).when(validator).validateGetByClientId(null);
        accountService.getByClientId(null);
    }

    @Test
    public void getByClientId_ClientIdIsValid_ShouldReturnListOfAccounts() {
        doNothing().when(validator).validateGetByClientId(1);
        List<Account> accounts = Arrays.asList(createAccount(1), createAccount(2));
        when(accountRepository.getByClientId(1)).thenReturn(accounts);
        List<Account> accountsByClientId = accountService.getByClientId(1);
        Assert.assertEquals(accounts, accountsByClientId);
    }

    private Account createAccount(Integer id) {
        Account account = new Account();
        account.setId(id);
        return account;
    }
}
