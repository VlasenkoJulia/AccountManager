package account_manager.currency_converter;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

public class CurrencyConverterServiceTest {
    @Mock
    CurrencyConverter currencyConverter;
    @Mock
    ConversionDtoValidator validator;
    @InjectMocks
    CurrencyConverterService currencyConverterService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = InputParameterValidationException.class)
    public void convert_notValidConversionDto_ShouldThrowException() {
        ConversionDto conversionDto = new ConversionDto(0, 2, 1.0);
        doThrow(InputParameterValidationException.class).when(validator).validate(conversionDto);
        currencyConverterService.convert(conversionDto);
    }

    @Test
    public void convert_validConversionDto_ShouldReturnSuccessMessage() {
        ConversionDto conversionDto = new ConversionDto(1, 2, 1.0);
        doNothing().when(validator).validate(conversionDto);
        doNothing().when(currencyConverter).convert(conversionDto);
        String message = currencyConverterService.convert(conversionDto);
        Assert.assertEquals("Successful conversion", message);
    }

}