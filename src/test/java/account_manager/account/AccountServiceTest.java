package account_manager.account;

import account_manager.repository.account.Account;
import account_manager.repository.account.AccountRepository;
import account_manager.repository.account.AccountType;
import account_manager.repository.card.Card;
import account_manager.repository.client.Client;
import account_manager.repository.currency.Currency;
import account_manager.service.AccountService;
import account_manager.service.CardService;
import account_manager.service.converter.Converter;
import account_manager.service.dto.AccountDto;
import account_manager.service.validator.AccountValidator;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.*;

import static org.mockito.Mockito.*;

public class AccountServiceTest {
    @Mock
    AccountRepository accountRepository;
    @Mock
    AccountValidator validator;
    @Mock
    CardService cardService;
    @Mock
    Converter<Account, AccountDto> converter;
    @InjectMocks
    AccountService accountService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = InputParameterValidationException.class)
    public void getById_AccountNotFound_ShouldThrowException() {
        when(accountRepository.findById(1)).thenReturn(Optional.empty());
        doThrow(InputParameterValidationException.class).when(validator).validateGet(null);
        accountService.getById(1);
    }

    @Test
    public void getById_AccountFound_ShouldReturnAccount() {
        Client client = new Client(1, "John", "Doe");
        Currency currency = new Currency("840", 1.0, "US Dollar", "USD");
        Account account = new Account(1, "111", currency, AccountType.CURRENT, 1.0, Instant.ofEpochSecond(0L), client, Collections.emptySet());
        AccountDto accountDto = new AccountDto(1, "111", "840", "CURRENT", 1.0, "01.01.1970", 1);
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));
        doNothing().when(validator).validateGet(account);
        when(converter.convertFrom(account)).thenReturn(accountDto);
        AccountDto accountFound = accountService.getById(1);
        Assert.assertEquals(accountDto, accountFound);
    }

    @Test(expected = InputParameterValidationException.class)
    public void create_AccountIsNotValid_ShouldThrowException() {
        AccountDto accountDto = createAccountDto(1);
        Account account = createAccount(1);
        when(converter.convertTo(accountDto)).thenReturn(account);
        doThrow(InputParameterValidationException.class).when(validator).validateCreate(account);
        accountService.create(accountDto);
    }

    @Test
    public void create_AccountValid_ShouldReturnSuccessMessage() {
        AccountDto accountDto = new AccountDto();
        Account account = createAccount(null);
        when(converter.convertTo(accountDto)).thenReturn(account);
        doNothing().when(validator).validateCreate(account);
        when(accountRepository.save(account)).thenReturn(createAccount(1));
        String message = accountService.create(accountDto);
        Assert.assertEquals("Created account #1", message);
    }

    @Test(expected = InputParameterValidationException.class)
    public void update_AccountIsNotValid_ShouldThrowException() {
        AccountDto accountDto = new AccountDto();
        Account account = createAccount(null);
        when(converter.convertTo(accountDto)).thenReturn(account);
        doThrow(InputParameterValidationException.class).when(validator).validateUpdate(account);
        accountService.update(accountDto);
    }

    @Test
    public void update_AccountValid_ShouldReturnSuccessMessage() {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(1);
        Account account = createAccount(1);
        when(converter.convertTo(accountDto)).thenReturn(account);
        doNothing().when(validator).validateUpdate(account);
        doNothing().when(accountRepository).update(account);
        String message = accountService.update(accountDto);
        Assert.assertEquals("Account updated successfully", message);
    }

    @Test
    public void deleteById_ClientIdIsValid_ShouldReturnSuccessMessage() {
        Set<Card> cards = new HashSet<>();

        List<Account> accounts = new ArrayList<>(Collections.singletonList(new Account()));
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
        List<Account> accounts = Collections.singletonList(createAccount(1));
        List<AccountDto> accountDtos = Collections.singletonList(createAccountDto(1));
        when(accountRepository.findAllByClientId(1)).thenReturn(accounts);
        when(converter.convertFrom(accounts.get(0))).thenReturn(accountDtos.get(0));
        List<AccountDto> accountsByClientId = accountService.getByClientId(1);
        Assert.assertEquals(accountDtos, accountsByClientId);
    }

    private Account createAccount(Integer id) {
        Account account = new Account();
        account.setId(id);
        return account;
    }

    private AccountDto createAccountDto(Integer id) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(id);
        return accountDto;
    }
}
