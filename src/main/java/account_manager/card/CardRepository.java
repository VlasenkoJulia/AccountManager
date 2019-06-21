package account_manager.card;

import account_manager.account.Account;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

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

    public void deleteById(int id) {
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

    public List<Card> getByAccountId(Integer accountId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Card> criteriaQuery = criteriaBuilder.createQuery(Card.class);
        Root<Account> accountRoot = criteriaQuery.from(Account.class);

        criteriaQuery.where(criteriaBuilder.equal(accountRoot.get("id"),
                accountId));
        Join<Account, Card> accounts = accountRoot.join("cards");
        CriteriaQuery<Card> cq = criteriaQuery.select(accounts);
        TypedQuery<Card> query = session.createQuery(cq);
        return query.getResultList();
    }
}
