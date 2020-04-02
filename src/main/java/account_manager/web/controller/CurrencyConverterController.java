package account_manager.web.controller;


import account_manager.service.dto.ConversionDto;
import account_manager.service.CurrencyConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/converter")
public class CurrencyConverterController {
    private final CurrencyConverterService currencyConverterService;

    @Autowired
    public CurrencyConverterController(CurrencyConverterService currencyConverterService) {
        this.currencyConverterService = currencyConverterService;
    }

    @PostMapping
    @ResponseBody
    public String convert(@RequestBody ConversionDto conversionDto) {
        return currencyConverterService.convert(conversionDto);
    }
}
