package account_manager.web.controllers;


import account_manager.currency_converter.ConversionDto;
import account_manager.currency_converter.ConversionDtoValidator;
import account_manager.currency_converter.CurrencyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/converter")
public class CurrencyConverterController {
    private final CurrencyConverter currencyConverter;
    private final ConversionDtoValidator conversionDtoValidator;

    @Autowired
    public CurrencyConverterController(CurrencyConverter currencyConverter,
                                       ConversionDtoValidator conversionDtoValidator) {
        this.currencyConverter = currencyConverter;
        this.conversionDtoValidator = conversionDtoValidator;
    }

    @PostMapping
    @ResponseBody
    public String convert(@RequestBody ConversionDto conversionDto) {
        conversionDtoValidator.validate(conversionDto);
        currencyConverter.convert(conversionDto);
        return "Successful conversion";
    }
}
