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

    Currency getCurrency(String currencyCode) {
        Currency currency = jdbcTemplate.queryForObject("SELECT * FROM currency WHERE code = ?", currencyRowMapper, currencyCode);
        if (currency == null || currency.getRate() == 0) {
            throw new RuntimeException(String.format("Invalid rate of %s currency", currencyCode));
        }
        return currency;
    }
}
