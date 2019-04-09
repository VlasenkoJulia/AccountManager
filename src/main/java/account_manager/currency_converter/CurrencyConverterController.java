package account_manager.currency_converter;


import account_manager.account.Account;
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
    public String convert(@RequestBody String body) {
        ConversionDto conversionDto = gson.fromJson(body, ConversionDto.class);
        if (conversionDto.getSourceAccountId() == null || conversionDto.getTargetAccountId() == null) {
            throw new IllegalStateException("Can not provide conversion operation with passed accounts");
        }
        List<Account> accounts = currencyConverter.convert(conversionDto);
        return gson.toJson(accounts);
    }
}
