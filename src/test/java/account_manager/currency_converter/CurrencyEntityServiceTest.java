package account_manager.currency_converter;

import account_manager.repository.currency.CurrencyEntity;
import account_manager.repository.currency.CurrencyRepository;
import account_manager.service.CurrencyService;
import account_manager.service.validator.CurrencyValidator;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CurrencyEntityServiceTest {
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
        when(currencyRepository.findById("840")).thenReturn(Optional.empty());
        doThrow(InputParameterValidationException.class).when(validator).validateGet(null);
        currencyService.getByCode("840");
    }

    @Test
    public void getByCode_CurrencyFound_ShouldReturnCurrency() {
        CurrencyEntity currencyEntity = new CurrencyEntity("840", 1.0, "US Dollar", "USD");
        when(currencyRepository.findById("840")).thenReturn(Optional.of(currencyEntity));
        doNothing().when(validator).validateGet(currencyEntity);
        CurrencyEntity currencyEntityFound = currencyService.getByCode("840");
        assertEquals(currencyEntity, currencyEntityFound);
    }

}
