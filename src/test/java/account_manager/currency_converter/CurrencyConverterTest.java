package account_manager.currency_converter;

import account_manager.account.Account;
import account_manager.account.AccountService;
import account_manager.web.exception_handling.CurrencyConversionValidationException;
import account_manager.web.exception_handling.InputParameterValidationException;
import junitparams.JUnitParamsRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class CurrencyConverterTest {
    private final static int VALID_SOURCE_ID = 1;
    private final static int VALID_TARGET_ID = 2;
    private final static double VALID_AMOUNT = 1000.0;

    @Mock
    AccountService accountService;
    @Mock
    CurrencyService currencyService;
    @InjectMocks
    CurrencyConverter currencyConverter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(accountService.getById(VALID_SOURCE_ID)).thenReturn(createAccount(VALID_SOURCE_ID, 2000.0, "980", 27.15));
        when(accountService.getById(VALID_TARGET_ID)).thenReturn(createAccount(VALID_TARGET_ID, 2000.0, "978", 0.88));
        when(currencyService.getByCode("980")).thenReturn(createCurrency("980", 27.15));
        when(currencyService.getByCode("978")).thenReturn(createCurrency("978", 0.88));
    }

    @Test(expected = InputParameterValidationException.class)
    public void shouldThrowExceptionIfSourceAccountIsNull() {
        ConversionDto conversionDto = new ConversionDto(VALID_SOURCE_ID, VALID_TARGET_ID, VALID_AMOUNT);
        when(accountService.getById(VALID_SOURCE_ID)).thenReturn(null);
        currencyConverter.convert(conversionDto);
    }

    @Test(expected = InputParameterValidationException.class)
    public void shouldThrowExceptionIfTargetAccountIsNull() {
        ConversionDto conversionDto = new ConversionDto(VALID_SOURCE_ID, VALID_TARGET_ID, VALID_AMOUNT);
        when(accountService.getById(VALID_TARGET_ID)).thenReturn(null);
        currencyConverter.convert(conversionDto);
    }

    @Test(expected = CurrencyConversionValidationException.class)
    public void shouldThrowExceptionIfNotEnoughBalance() {
        when(accountService.getById(VALID_SOURCE_ID)).thenReturn(createAccount(VALID_SOURCE_ID, 10.0));
        currencyConverter.convert(new ConversionDto(VALID_SOURCE_ID, VALID_TARGET_ID, VALID_AMOUNT));
    }

    @Test
    public void checkIfSetNewBalancesCorrectly() {
        ConversionDto conversionDto = new ConversionDto(VALID_SOURCE_ID, VALID_TARGET_ID, VALID_AMOUNT);
        double exchangeRate = 0.88 / 27.15;
        double amountToAdd = Math.round((VALID_AMOUNT * exchangeRate) * 100.0) / 100.0;
        currencyConverter.convert(conversionDto);
        verify(accountService).update(refEq(createAccount(VALID_SOURCE_ID, 1000.0, "980", 27.15)));
        verify(accountService).update(refEq(createAccount(VALID_TARGET_ID, (2000.0 + amountToAdd), "978", 0.88)));
    }

    private Account createAccount(Integer id, double balance) {
        Account account = new Account();
        account.setId(id);
        account.setBalance(balance);
        return account;
    }

    private Account createAccount(Integer id, double balance, String currencyCode, double currencyRate) {
        Account account = new Account();
        account.setId(id);
        account.setBalance(balance);
        account.setCurrency(createCurrency(currencyCode, currencyRate));
        return account;
    }

    private Currency createCurrency(String code, double rate) {
        Currency currency = new Currency();
        currency.setCode(code);
        currency.setRate(rate);
        return currency;
    }
}