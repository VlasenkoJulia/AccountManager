package account_manager.currency_converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class CurrencyRepository {
    private final JdbcTemplate jdbcTemplate;
    private final CurrencyRowMapper currencyRowMapper;

    @Autowired
    public CurrencyRepository(JdbcTemplate jdbcTemplate, CurrencyRowMapper currencyRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.currencyRowMapper = currencyRowMapper;
    }

    double calculateExchangeRate(String sourceCurrencyCode, String targetCurrencyCode) {
        Currency sourceCurrency = jdbcTemplate.queryForObject("SELECT * FROM currency WHERE code = ?",
                currencyRowMapper, sourceCurrencyCode);
        if (sourceCurrency.getRate() == 0) {
            throw new RuntimeException("Can not perform exchange rate calculation, invalid rate of one of  the passed currency");
        }
        Currency targetCurrency = jdbcTemplate.queryForObject("SELECT * FROM currency WHERE code = ?",
                currencyRowMapper, targetCurrencyCode);
        if (targetCurrency.getRate() == 0) {
            throw new RuntimeException("Can not perform exchange rate calculation, invalid rate of one of  the passed currency");
        }
        return targetCurrency.getRate() / sourceCurrency.getRate();
    }
}
