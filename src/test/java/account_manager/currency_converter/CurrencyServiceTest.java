package account_manager.currency_converter;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CurrencyServiceTest {
    @Mock
    CurrencyValidator validator;
    @Mock
    CurrencyRepository currencyRepository;
    @InjectMocks
    CurrencyService currencyService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = InputParameterValidationException.class)
    public void getByCode_currencyNotFound_ShouldThrowException() {
        when(currencyRepository.getCurrency("840")).thenReturn(null);
        doThrow(InputParameterValidationException.class).when(validator).validateGet(null);
        currencyService.getByCode("840");
    }

    @Test
    public void getByCode_CurrencyFound_ShouldReturnCurrency() {
        Currency currency = new Currency("840", 1.0, "US Dollar", "USD");
        when(currencyRepository.getCurrency("840")).thenReturn(currency);
        doNothing().when(validator).validateGet(currency);
        Currency currencyFound = currencyService.getByCode("840");
        assertEquals(currency, currencyFound);
    }

}