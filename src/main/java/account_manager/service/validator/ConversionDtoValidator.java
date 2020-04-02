package account_manager.service.validator;

import account_manager.service.dto.ConversionDto;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Component;

@Component
public class ConversionDtoValidator {
    public void validate(ConversionDto conversionDto) {
        double amount = conversionDto.getAmount();
        if (amount < 0) {
            throw new InputParameterValidationException("Passed amount less than 0");
        }
        Integer sourceAccountId = conversionDto.getSourceAccountId();
        if (sourceAccountId == null || sourceAccountId <= 0) {
            throw new InputParameterValidationException("Passed source account ID can not be null or less/equal 0");
        }
        Integer targetAccountId = conversionDto.getTargetAccountId();
        if (targetAccountId == null || targetAccountId <= 0) {
            throw new InputParameterValidationException("Passed target account ID can not be null or less/equal 0");
        }
    }
}
