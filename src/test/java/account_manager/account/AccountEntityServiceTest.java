package account_manager.account;

import account_manager.repository.account.AccountEntity;
import account_manager.repository.account.AccountRepository;
import account_manager.repository.account.AccountType;
import account_manager.repository.card.CardEntity;
import account_manager.repository.client.ClientEntity;
import account_manager.repository.currency.CurrencyEntity;
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

public class AccountEntityServiceTest {
    @Mock
    AccountRepository accountRepository;
    @Mock
    AccountValidator validator;
    @Mock
    CardService cardService;
    @Mock
    Converter<AccountEntity, AccountDto> converter;
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
        ClientEntity clientEntity = new ClientEntity(1, "John", "Doe");
        CurrencyEntity currencyEntity = new CurrencyEntity("840", 1.0, "US Dollar", "USD");
        AccountEntity accountEntity = new AccountEntity(1, "111", currencyEntity, AccountType.CURRENT, 1.0, Instant.ofEpochSecond(0L), clientEntity, Collections.emptySet());
        AccountDto accountDto = new AccountDto(1, "111", "840", "CURRENT", 1.0, "01.01.1970", 1);
        when(accountRepository.findById(1)).thenReturn(Optional.of(accountEntity));
        doNothing().when(validator).validateGet(accountEntity);
        when(converter.convertFrom(accountEntity)).thenReturn(accountDto);
        AccountDto accountFound = accountService.getById(1);
        Assert.assertEquals(accountDto, accountFound);
    }

    @Test(expected = InputParameterValidationException.class)
    public void create_AccountIsNotValid_ShouldThrowException() {
        AccountDto accountDto = createAccountDto(1);
        AccountEntity accountEntity = createAccount(1);
        when(converter.convertTo(accountDto)).thenReturn(accountEntity);
        doThrow(InputParameterValidationException.class).when(validator).validateCreate(accountEntity);
        accountService.create(accountDto);
    }

    @Test
    public void create_AccountValid_ShouldReturnSuccessMessage() {
        AccountDto accountDto = new AccountDto();
        AccountEntity accountEntity = createAccount(null);
        when(converter.convertTo(accountDto)).thenReturn(accountEntity);
        doNothing().when(validator).validateCreate(accountEntity);
        when(accountRepository.save(accountEntity)).thenReturn(createAccount(1));
        String message = accountService.create(accountDto);
        Assert.assertEquals("Created account #1", message);
    }

    @Test(expected = InputParameterValidationException.class)
    public void update_AccountIsNotValid_ShouldThrowException() {
        AccountDto accountDto = new AccountDto();
        AccountEntity accountEntity = createAccount(null);
        when(converter.convertTo(accountDto)).thenReturn(accountEntity);
        doThrow(InputParameterValidationException.class).when(validator).validateUpdate(accountEntity);
        accountService.update(accountDto);
    }

    @Test
    public void update_AccountValid_ShouldReturnSuccessMessage() {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(1);
        AccountEntity accountEntity = createAccount(1);
        when(converter.convertTo(accountDto)).thenReturn(accountEntity);
        doNothing().when(validator).validateUpdate(accountEntity);
        doNothing().when(accountRepository).update(accountEntity);
        String message = accountService.update(accountDto);
        Assert.assertEquals("Account updated successfully", message);
    }

    @Test
    public void deleteById_ClientIdIsValid_ShouldReturnSuccessMessage() {
        Set<CardEntity> cardEntities = new HashSet<>();

        List<AccountEntity> accountEntities = new ArrayList<>(Collections.singletonList(new AccountEntity()));
        HashSet<CardEntity> accountCardEntities = new HashSet<>(
                Collections.singletonList(new CardEntity(1, "111", Collections.emptyList())));
        accountEntities.get(0).setCards(accountCardEntities);
        accountEntities.get(0).setId(1);

        cardEntities.add(new CardEntity(1, "111", accountEntities));
        doNothing().when(accountRepository).deleteById(1);
        when(cardService.getByAccountId(1)).thenReturn(cardEntities);
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
        List<AccountEntity> accountEntities = Collections.singletonList(createAccount(1));
        List<AccountDto> accountDtos = Collections.singletonList(createAccountDto(1));
        when(accountRepository.findAllByClientId(1)).thenReturn(accountEntities);
        when(converter.convertFrom(accountEntities.get(0))).thenReturn(accountDtos.get(0));
        List<AccountDto> accountsByClientId = accountService.getByClientId(1);
        Assert.assertEquals(accountDtos, accountsByClientId);
    }

    private AccountEntity createAccount(Integer id) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(id);
        return accountEntity;
    }

    private AccountDto createAccountDto(Integer id) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(id);
        return accountDto;
    }
}
