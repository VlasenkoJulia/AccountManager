package account_manager.currency_converter;

import account_manager.account.Account;
import account_manager.account.AccountRepository;
import account_manager.web.exception_handling.CurrencyConversionValidationException;
import account_manager.web.exception_handling.InputParameterValidationException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
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
    private final static double INVALID_AMOUNT = -1000.0;

    @Mock
    AccountRepository accountRepository;
    @Mock
    CurrencyRepository currencyRepository;
    @InjectMocks
    CurrencyConverter currencyConverter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(accountRepository.getById(VALID_SOURCE_ID)).thenReturn(createAccount(VALID_SOURCE_ID, 2000.0, "980", 27.15));
        when(accountRepository.getById(VALID_TARGET_ID)).thenReturn(createAccount(VALID_TARGET_ID, 2000.0, "978", 0.88));
        when(currencyRepository.getCurrency("980")).thenReturn(createCurrency("980", 27.15));
        when(currencyRepository.getCurrency("978")).thenReturn(createCurrency("978", 0.88));
    }

    @Test(expected = InputParameterValidationException.class)
    public void shouldThrowExceptionIfAmountIsInvalid() {
        ConversionDto conversionDto = new ConversionDto(VALID_SOURCE_ID, VALID_TARGET_ID, INVALID_AMOUNT);
        currencyConverter.convert(conversionDto);
    }

    private static Object[] getInvalidAccountId() {
        return new Integer[][]{{null}, {-5}};
    }

    @Test(expected = InputParameterValidationException.class)
    @Parameters(method = "getInvalidAccountId")
    public void shouldThrowExceptionIfSourceAccountIdIsInvalid(Integer invalidAccountId) {
        ConversionDto conversionDto = new ConversionDto(invalidAccountId, VALID_TARGET_ID, VALID_AMOUNT);
        currencyConverter.convert(conversionDto);
    }

    @Test(expected = InputParameterValidationException.class)
    @Parameters(method = "getInvalidAccountId")
    public void shouldThrowExceptionIfTargetAccountIdIsInvalid(Integer invalidAccountId) {
        ConversionDto conversionDto = new ConversionDto(VALID_SOURCE_ID, invalidAccountId, VALID_AMOUNT);
        currencyConverter.convert(conversionDto);
    }

    @Test(expected = InputParameterValidationException.class)
    public void shouldThrowExceptionIfSourceAccountIsNull() {
        ConversionDto conversionDto = new ConversionDto(VALID_SOURCE_ID, VALID_TARGET_ID, VALID_AMOUNT);
        when(accountRepository.getById(VALID_SOURCE_ID)).thenReturn(null);
        currencyConverter.convert(conversionDto);
    }

    @Test(expected = InputParameterValidationException.class)
    public void shouldThrowExceptionIfTargetAccountIsNull() {
        ConversionDto conversionDto = new ConversionDto(VALID_SOURCE_ID, VALID_TARGET_ID, VALID_AMOUNT);
        when(accountRepository.getById(VALID_TARGET_ID)).thenReturn(null);
        currencyConverter.convert(conversionDto);
    }

    @Test(expected = CurrencyConversionValidationException.class)
    public void shouldThrowExceptionIfNotEnoughBalance() {
        when(accountRepository.getById(VALID_SOURCE_ID)).thenReturn(createAccount(VALID_SOURCE_ID, 10.0));
        currencyConverter.convert(new ConversionDto(VALID_SOURCE_ID, VALID_TARGET_ID, VALID_AMOUNT));
    }

    @Test(expected = InputParameterValidationException.class)
    public void shouldThrowExceptionIfSourceCurrencyIsNull() {
        ConversionDto conversionDto = new ConversionDto(VALID_SOURCE_ID, VALID_TARGET_ID, VALID_AMOUNT);
        when(currencyRepository.getCurrency("980")).thenReturn(null);
        currencyConverter.convert(conversionDto);
    }

    @Test(expected = InputParameterValidationException.class)
    public void shouldThrowExceptionIfTargetCurrencyIsNull() {
        ConversionDto conversionDto = new ConversionDto(VALID_SOURCE_ID, VALID_TARGET_ID, VALID_AMOUNT);
        when(currencyRepository.getCurrency("978")).thenReturn(null);
        currencyConverter.convert(conversionDto);
    }

    private static Object[] getInvalidCurrencyRates() {
        return new Double[][]{{0.0}, {-5.0}};
    }

    @Test(expected = InputParameterValidationException.class)
    @Parameters(method = "getInvalidCurrencyRates")
    public void shouldThrowExceptionIfSourceCurrencyRateIsInvalid(Double invalidCurrencyRate) {
        ConversionDto conversionDto = new ConversionDto(VALID_SOURCE_ID, VALID_TARGET_ID, VALID_AMOUNT);
        when(currencyRepository.getCurrency("980")).thenReturn(createCurrency("980", invalidCurrencyRate));
        currencyConverter.convert(conversionDto);
    }

    @Test(expected = InputParameterValidationException.class)
    @Parameters(method = "getInvalidCurrencyRates")
    public void shouldThrowExceptionIfTargetCurrencyRateIsInvalid(Double invalidCurrencyRate) {
        ConversionDto conversionDto = new ConversionDto(VALID_SOURCE_ID, VALID_TARGET_ID, VALID_AMOUNT);
        when(currencyRepository.getCurrency("978")).thenReturn(createCurrency("978", invalidCurrencyRate));
        currencyConverter.convert(conversionDto);
    }

    @Test
    public void checkIfSetNewBalancesCorrectly() {
        ConversionDto conversionDto = new ConversionDto(VALID_SOURCE_ID, VALID_TARGET_ID, VALID_AMOUNT);
        double exchangeRate = 0.88 / 27.15;
        double amountToAdd = Math.round((VALID_AMOUNT * exchangeRate) * 100.0) / 100.0;
        currencyConverter.convert(conversionDto);
        verify(accountRepository).update(refEq(createAccount(VALID_SOURCE_ID, 1000.0, "980", 27.15)));
        verify(accountRepository).update(refEq(createAccount(VALID_TARGET_ID, (2000.0 + amountToAdd), "978", 0.88)));
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