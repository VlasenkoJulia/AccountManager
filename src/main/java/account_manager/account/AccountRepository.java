package account_manager.account;

import account_manager.card.CardRepository;
import account_manager.client.ClientRepository;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AccountRepository {
    private final JdbcTemplate jdbcTemplate;
    private final CardRepository cardRepository;
    private final AccountRowMapper accountRowMapper;
    private final ClientRepository clientRepository;

    @Autowired
    public AccountRepository(JdbcTemplate jdbcTemplate,
                             CardRepository cardRepository,
                             AccountRowMapper accountRowMapper,
                             ClientRepository clientRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.cardRepository = cardRepository;
        this.accountRowMapper = accountRowMapper;
        this.clientRepository = clientRepository;
    }

    public Account create(Account account) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
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

    public void deleteById(int id) throws InputParameterValidationException {
        jdbcTemplate.update("DELETE FROM account_cards WHERE account_id = ?", id);
        int rowsAffected = jdbcTemplate.update("DELETE FROM account WHERE id = ?", id);
        if (rowsAffected < 1) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
        cardRepository.deleteNotActive();
    }

    public Account getById(int id) {
        List<Account> accounts = jdbcTemplate.query("SELECT * FROM account WHERE id = ?", accountRowMapper, id);
        return DataAccessUtils.singleResult(accounts);
    }

    public List<Account> getByClientId(int id) {
        return jdbcTemplate.query("SELECT * FROM account WHERE client_id = ?", accountRowMapper, id);
    }

    public void update(Account account) throws InputParameterValidationException {
        int rowsAffected = jdbcTemplate.update(
                "UPDATE account SET number = ?, currency_code = ?, type = ?, balance = ?, open_date = ?, client_id = ? WHERE id = ?",
                account.getNumber(), account.getCurrencyCode(), account.getType().getTitle(),
                account.getBalance(), account.getOpenDate(), account.getOwnerId(), account.getId());
        if (rowsAffected < 1) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
    }
}
