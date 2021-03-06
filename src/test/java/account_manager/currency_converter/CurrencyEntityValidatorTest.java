package account_manager.currency_converter;

import account_manager.repository.currency.CurrencyEntity;
import account_manager.service.validator.CurrencyValidator;
import account_manager.web.exception_handling.InputParameterValidationException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class CurrencyEntityValidatorTest {
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
        CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setRate(invalidRate);
        validator.validateGet(currencyEntity);
    }

    @Test
    public void validCurrency() {
        CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setRate(1.0);
        validator.validateGet(currencyEntity);
    }
}
