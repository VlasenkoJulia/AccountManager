package account_manager.account;

import account_manager.repository.entity.Card;
import account_manager.service.CardService;
import account_manager.repository.entity.Client;
import account_manager.repository.entity.Currency;
import account_manager.repository.entity.Account;
import account_manager.repository.enums.AccountType;
import account_manager.repository.AccountRepository;
import account_manager.service.AccountService;
import account_manager.service.validator.AccountValidator;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.Mockito.*;

public class AccountServiceTest {
    @Mock
    AccountRepository accountRepository;
    @Mock
    AccountValidator validator;
    @Mock
    CardService cardService;
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
        Client client = new Client(1, "John", "Doe");
        Currency currency = new Currency("840", 1.0, "US Dollar", "USD");
        Account account = new Account(1, "111", currency, AccountType.CURRENT, 1.0, new Date(1L), client, Collections.emptySet());
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
    public void deleteById_ClientIdIsValid_ShouldReturnSuccessMessage() {
        ArrayList<Card> cards = new ArrayList<>();

        ArrayList<Account> accounts = new ArrayList<>(Collections.singletonList(new Account()));
        HashSet<Card> accountCards = new HashSet<>(
                Collections.singletonList(new Card(1, "111", Collections.emptyList())));
        accounts.get(0).setCards(accountCards);
        accounts.get(0).setId(1);

        cards.add(new Card(1, "111", accounts));
        doNothing().when(accountRepository).deleteById(1);
        when(cardService.getByAccountId(1)).thenReturn(cards);
        when(cardService.deleteById(1)).thenReturn("Deleted card #1");
        String message = accountService.deleteById(1);
        Assert.assertEquals("Deleted account #1", message);
        verify(cardService).deleteById(1);
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
