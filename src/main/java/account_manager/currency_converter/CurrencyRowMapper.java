package account_manager.currency_converter;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyRowMapper implements RowMapper<Currency> {

    @Override
    public Currency mapRow(ResultSet resultSet, int i) throws SQLException {
        String code = resultSet.getString("code");
        double rate = resultSet.getDouble("rate");
        return new Currency(code, rate);
    }
}
