package account_manager.currency_converter;

import account_manager.DataSourceCreator;
import org.springframework.jdbc.core.JdbcTemplate;

class CurrencyRepository {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceCreator.createDataSource());

    double calculateExchangeRate(String sourceCurrencyCode, String targetCurrencyCode) {
        Currency sourceCurrency = jdbcTemplate.queryForObject("SELECT * FROM currency WHERE code = ?",
                new CurrencyRowMapper(), sourceCurrencyCode);
        if (sourceCurrency.getRate() == 0) {
            throw new RuntimeException("Can not perform exchange rate calculation, invalid rate of one of  the passed currency");
        }
        Currency targetCurrency = jdbcTemplate.queryForObject("SELECT * FROM currency WHERE code = ?",
                new CurrencyRowMapper(), targetCurrencyCode);
        if (targetCurrency.getRate() == 0) {
            throw new RuntimeException("Can not perform exchange rate calculation, invalid rate of one of  the passed currency");
        }
        return targetCurrency.getRate() / sourceCurrency.getRate();
    }
}
