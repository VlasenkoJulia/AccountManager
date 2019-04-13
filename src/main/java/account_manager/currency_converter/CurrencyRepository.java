package account_manager.currency_converter;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


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
        List<Currency> currency = jdbcTemplate.query("SELECT * FROM currency WHERE code = ?", currencyRowMapper, currencyCode);
        return DataAccessUtils.singleResult(currency);
    }
}
