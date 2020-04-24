package account_manager.repository.currency;

import account_manager.repository.AbstractTestRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;


public class CurrencyRepositoryTest extends AbstractTestRepository {
    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    public void getCurrency() {
        Currency currency = new Currency("980", 27.15, "Ukrainian Hryvnia", "UAH");
        Optional<Currency> foundCurrency = currencyRepository.findById("980");
        assertThat(foundCurrency).isPresent();
        assertEquals(currency, foundCurrency.get());
    }
}
