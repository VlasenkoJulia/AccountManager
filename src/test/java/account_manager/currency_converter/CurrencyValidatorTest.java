package account_manager.currency_converter;

import account_manager.repository.entity.Currency;
import account_manager.service.validator.CurrencyValidator;
import account_manager.web.exception_handling.InputParameterValidationException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class CurrencyValidatorTest {
    private CurrencyValidator validator;

    @Before
    public void setup() {
        validator = new CurrencyValidator();
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateGet_CurrencyIsNull_ShouldThrowException() {
        validator.validateGet(null);
    }

    private static Object[] getInvalidCurrencyRate() {
        return new Double[][]{{0.0}, {-1.0}};
    }

    @Test(expected = InputParameterValidationException.class)
    @Parameters(method = "getInvalidCurrencyRate")
    public void validateGet_CurrencyRateIsInvalid_ShouldThrowException(double invalidRate) {
        Currency currency = new Currency();
        currency.setRate(invalidRate);
        validator.validateGet(currency);
    }

    @Test
    public void validCurrency() {
        Currency currency = new Currency();
        currency.setRate(1.0);
        validator.validateGet(currency);
    }
}
