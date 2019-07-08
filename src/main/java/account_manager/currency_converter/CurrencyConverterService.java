package account_manager.currency_converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyConverterService {
    private final CurrencyConverter currencyConverter;
    private final ConversionDtoValidator validator;

    @Autowired
    public CurrencyConverterService(CurrencyConverter currencyConverter, ConversionDtoValidator validator) {
        this.currencyConverter = currencyConverter;
        this.validator = validator;
    }

    public String convert(ConversionDto conversionDto) {
        validator.validate(conversionDto);
        currencyConverter.convert(conversionDto);
        return "Successful conversion";
    }
}
