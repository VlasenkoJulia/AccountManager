package account_manager.currency_converter;

import account_manager.service.dto.ConversionDto;
import account_manager.service.validator.ConversionDtoValidator;
import account_manager.web.exception_handling.InputParameterValidationException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class ConversionDtoValidatorTest {
    private ConversionDtoValidator validator;

    @Before
    public void setup() {
        validator = new ConversionDtoValidator();
    }

    @Test(expected = InputParameterValidationException.class)
    public void shouldThrowExceptionAmountIsLessThanZero() {
        ConversionDto conversionDto = new ConversionDto(1, 2, -1.0);
        validator.validate(conversionDto);
    }

    private static Object[] getInvalidAccountId() {
        return new Integer[][]{{0}, {-1}, {null}};
    }

    @Test(expected = InputParameterValidationException.class)
    @Parameters(method = "getInvalidAccountId")
    public void shouldThrowExceptionIfSourceAccountIdIsInvalid(Integer invalidId) {
        ConversionDto conversionDto = new ConversionDto(invalidId, 2, 1.0);
        validator.validate(conversionDto);
    }

    @Test(expected = InputParameterValidationException.class)
    @Parameters(method = "getInvalidAccountId")
    public void shouldThrowExceptionIfTargetAccountIdIsInvalid(Integer invalidId) {
        ConversionDto conversionDto = new ConversionDto(1, invalidId, 1.0);
        validator.validate(conversionDto);
    }

    @Test
    public void validateValidConversionDto() {
        ConversionDto conversionDto = new ConversionDto(1, 2, 1.0);
        validator.validate(conversionDto);
    }
}
