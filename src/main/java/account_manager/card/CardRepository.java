package account_manager.card;

import account_manager.DataSourceCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.*;

public class CardRepository {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceCreator.createDataSource());

    public void create(Card card) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(DataSourceCreator.createDataSource()).withTableName("card").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("number", card.getNumber());
        int createdCardId = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
        for (Integer accountId : card.getAccountsId()) {
            jdbcTemplate.update("INSERT INTO account_cards VALUES (?, ?)", accountId, createdCardId);
        }
    }

    public Card getById(int id) {
       return jdbcTemplate.query("SELECT * FROM account_cards INNER JOIN card ON account_cards.card_id = card.id WHERE card_id = ?",
               resultSet -> {
                   HashSet<Integer> accountId = new HashSet<>();
                   int cardId;
                   String number;
                   if (resultSet.first()) {
                       cardId = resultSet.getInt("card_id");
                       number = resultSet.getString("number");
                       accountId.add(resultSet.getInt("account_id"));
                   } else {
                       throw new RuntimeException("Card with passed ID do not exist");
                   }
                   while (resultSet.next()) {
                       accountId.add(resultSet.getInt("account_id"));
                   }
                   return new Card(cardId, number, accountId);
               }, id);
    }

    public Set<Card> getByAccountId(int id) {
        List<Card> cards = jdbcTemplate.query("SELECT * FROM account_cards INNER JOIN card ON account_cards.card_id = card.id WHERE account_id = ?",
                (resultSet, i) -> {
            int cardId = resultSet.getInt("card_id");
            return getById(cardId);
        }, id);
        return new HashSet<>(cards);
    }

    public void deleteById(int id) {
        int rowsAffected = jdbcTemplate.update("DELETE FROM account_cards WHERE card_id = ?", id);
        if (rowsAffected < 1) {
            throw new RuntimeException("Card with passed ID do not exist");
        }
        jdbcTemplate.update("DELETE FROM card WHERE id = ?", id);
    }

    public void deleteNotActive() {
        List<Integer> notActiveCardIds = jdbcTemplate.query("SELECT id FROM card WHERE NOT EXISTS (SELECT * FROM account_cards WHERE card.id = account_cards.card_id)",
                (resultSet, i) -> resultSet.getInt("id"));
        notActiveCardIds.forEach(cardId -> jdbcTemplate.update("DELETE FROM card WHERE id = ?", cardId));
    }
}
