package account_manager.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CardRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    void create(Card card) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("card")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("number", card.getNumber());
        int createdCardId = insert.executeAndReturnKey(parameters).intValue();
        for (Integer accountId : card.getAccountsId()) {
            jdbcTemplate.update("INSERT INTO account_cards VALUES (?, ?)", accountId, createdCardId);
        }
    }

    Card getById(int id) {
        return jdbcTemplate.query(
                "SELECT * FROM account_cards"
                        + " INNER JOIN card ON account_cards.card_id = card.id"
                        + " WHERE card_id = ?",
                resultSet -> {
                    Set<Integer> accountIds = new HashSet<>();
                    int cardId;
                    String number;
                    if (resultSet.first()) {
                        cardId = resultSet.getInt("card_id");
                        number = resultSet.getString("number");
                        accountIds.add(resultSet.getInt("account_id"));
                    } else {
                        throw new RuntimeException("Card with passed ID do not exist");
                    }
                    while (resultSet.next()) {
                        accountIds.add(resultSet.getInt("account_id"));
                    }
                    return new Card(cardId, number, accountIds);
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

    void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM account_cards WHERE card_id = ?", id);
        int rowsAffected = jdbcTemplate.update("DELETE FROM card WHERE id = ?", id);
        if (rowsAffected < 1) {
            throw new RuntimeException("Card with passed ID do not exist");
        }
    }

    public void deleteNotActive() {
        List<Integer> notActiveCardIds = jdbcTemplate.query(
                "SELECT id FROM card"
                        + " WHERE NOT EXISTS ("
                        + "SELECT * FROM account_cards WHERE card.id = account_cards.card_id)",
                (resultSet, i) -> resultSet.getInt("id"));
        notActiveCardIds.forEach(cardId -> jdbcTemplate.update("DELETE FROM card WHERE id = ?", cardId));
    }
}
