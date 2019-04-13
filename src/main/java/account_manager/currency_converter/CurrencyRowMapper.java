package account_manager.currency_converter;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CurrencyRowMapper implements RowMapper<Currency> {
    @Nullable
    @Override
    public Currency mapRow(ResultSet resultSet, int i) throws SQLException {
        String code = resultSet.getString("code");
        double rate = resultSet.getDouble("rate");
        return new Currency(code, rate);
    }
}
