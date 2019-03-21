package account_manager.account;

import account_manager.DataSourceCreator;
import account_manager.card.CardRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AccountRepository {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceCreator.createDataSource());
    private CardRepository cardRepository = new CardRepository();

    public Account create(Account account) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(DataSourceCreator.createDataSource()).withTableName("account").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("number", account.getNumber());
        parameters.put("currency_code", account.getCurrencyCode());
        parameters.put("type", account.getType().getTitle());
        parameters.put("open_date", new Date(Calendar.getInstance().getTimeInMillis()));
        parameters.put("client_id", account.getOwnerId());
        int createdAccountId = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
        return getById(createdAccountId);
    }

    public void deleteById(int id) {
        int rowsAffected = jdbcTemplate.update("DELETE FROM account_cards WHERE account_id = ?", id);
        if (rowsAffected < 1) {
            throw new RuntimeException("Account with passed ID do not exist");
        }
        jdbcTemplate.update("DELETE FROM account WHERE id = ?", id);
        cardRepository.deleteNotActive();
    }


    public Account getById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM account WHERE id = ?", new AccountMapper(), id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Account with passed ID do not exist");
        }
    }

    public void update(Account account) {
        int rowsAffected = jdbcTemplate.update(
                "UPDATE account SET number = ?, currency_code = ?, type = ?, balance = ?, open_date = ?, client_id = ? WHERE id = ?",
                account.getNumber(), account.getCurrencyCode(), account.getType().getTitle(), account.getBalance(), account.getOpenDate(), account.getOwnerId(), account.getId());
        if (rowsAffected < 1) {
            throw new RuntimeException("Account with passed ID do not exist");
        }
    }
}
