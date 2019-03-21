package account_manager.account;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccountRowMapper implements RowMapper<Account> {
    @Nullable
    @Override
    public Account mapRow(ResultSet resultSet, int i) throws SQLException {
        Integer accountId = resultSet.getInt("id");
        String number = resultSet.getString("number");
        String currencyCode = resultSet.getString("currency_code");
        AccountType type = AccountType.valueOf(resultSet.getString("type").toUpperCase());
        double balance = resultSet.getDouble("balance");
        String openDateString = resultSet.getString("open_date");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date openDate = null;
        try {
            openDate = sdf.parse(openDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int ownerId = resultSet.getInt("client_id");
        return new Account(number, currencyCode, type, ownerId, accountId, balance, openDate);
    }
}
