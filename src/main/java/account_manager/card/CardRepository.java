package account_manager.card;

import account_manager.account.Account;
import account_manager.account.AccountRepository;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@Transactional
public class CardRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public CardRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(Card card) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(card);
        for (Account account : card.getAccounts()) {
            account.getCards().add(card);
        }
    }

    public Card getById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Card.class, id);
    }

    public void deleteById(int id){
        Session session = sessionFactory.getCurrentSession();
        Card card = session.get(Card.class, id);
        if (card == null) {
            throw new InputParameterValidationException("Card with passed ID do not exist");
        }
        session.delete(card);
        for (Account account : card.getAccounts()) {
            account.getCards().remove(card);
        }
    }
// TODO: 24.05.2019 rework this logic using service
//    public void deleteNotActive() {
//        List<Integer> notActiveCardIds = jdbcTemplate.query(
//                "SELECT id FROM card"
//                        + " WHERE NOT EXISTS ("
//                        + "SELECT * FROM account_cards WHERE card.id = account_cards.card_id)",
//                (resultSet, i) -> resultSet.getInt("id"));
//        notActiveCardIds.forEach(cardId -> jdbcTemplate.update("DELETE FROM card WHERE id = ?", cardId));
//    }
}
