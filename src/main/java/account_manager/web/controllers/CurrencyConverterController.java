package account_manager.web.controllers;


import account_manager.account.Account;
import account_manager.currency_converter.ConversionDto;
import account_manager.currency_converter.CurrencyConverter;
import account_manager.web.exception_handling.CurrencyConversionValidationException;
import account_manager.web.exception_handling.InputParameterValidationException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/converter")
public class CurrencyConverterController {
    private final CurrencyConverter currencyConverter;
    private final Gson gson;

    @Autowired
    public CurrencyConverterController(CurrencyConverter currencyConverter, Gson gson) {
        this.currencyConverter = currencyConverter;
        this.gson = gson;
    }

    @PostMapping
    public String convert(@RequestBody String body) throws InputParameterValidationException, CurrencyConversionValidationException {
        ConversionDto conversionDto = gson.fromJson(body, ConversionDto.class);
        if (conversionDto.getSourceAccountId() == null || conversionDto.getTargetAccountId() == null) {
            throw new InputParameterValidationException("Can not provide conversion operation with passed accounts");
        }
        List<Account> accounts = currencyConverter.convert(conversionDto);
        return gson.toJson(accounts);
    }
}
