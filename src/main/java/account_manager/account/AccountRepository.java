package account_manager.account;

import account_manager.card.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Repository
public class AccountRepository {
    private final JdbcTemplate jdbcTemplate;
    private final CardRepository cardRepository;
    private final AccountRowMapper accountRowMapper;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public AccountRepository(JdbcTemplate jdbcTemplate,
                             CardRepository cardRepository,
                             AccountRowMapper accountRowMapper,
                             SimpleJdbcInsert simpleJdbcInsert) {
        this.jdbcTemplate = jdbcTemplate;
        this.cardRepository = cardRepository;
        this.accountRowMapper = accountRowMapper;
        this.simpleJdbcInsert = simpleJdbcInsert;
    }

    public Account create(Account account) {
        SimpleJdbcInsert insert = simpleJdbcInsert
                .withTableName("account")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("number", account.getNumber());
        parameters.put("currency_code", account.getCurrencyCode());
        parameters.put("type", account.getType().getTitle());
        parameters.put("open_date", new Date(Calendar.getInstance().getTimeInMillis()));
        parameters.put("client_id", account.getOwnerId());
        int createdAccountId = insert.executeAndReturnKey(parameters).intValue();
        return getById(createdAccountId);
    }

    void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM account_cards WHERE account_id = ?", id);
        int rowsAffected = jdbcTemplate.update("DELETE FROM account WHERE id = ?", id);
        if (rowsAffected < 1) {
            throw new RuntimeException("Account with passed ID do not exist");
        }
        cardRepository.deleteNotActive();
    }

    public Account getById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM account WHERE id = ?", accountRowMapper, id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Account with passed ID do not exist");
        }
    }

    public void update(Account account) {
        int rowsAffected = jdbcTemplate.update(
                "UPDATE account SET number = ?, currency_code = ?, type = ?, balance = ?, open_date = ?, client_id = ? WHERE id = ?",
                account.getNumber(), account.getCurrencyCode(), account.getType().getTitle(),
                account.getBalance(), account.getOpenDate(), account.getOwnerId(), account.getId());
        if (rowsAffected < 1) {
            throw new RuntimeException("Account with passed ID do not exist");
        }
    }
}
