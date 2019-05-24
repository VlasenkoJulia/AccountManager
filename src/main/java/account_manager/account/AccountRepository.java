package account_manager.account;

import account_manager.card.Card;
import account_manager.card.CardRepository;
import account_manager.client.Client;
import account_manager.currency_converter.Currency;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

@Repository
@Transactional
public class AccountRepository {
    private final CardRepository cardRepository;
    private final SessionFactory sessionFactory;

    @Autowired
    public AccountRepository(CardRepository cardRepository, SessionFactory sessionFactory) {
        this.cardRepository = cardRepository;
        this.sessionFactory = sessionFactory;
    }

    public Account create(Account account) {
        Session session = sessionFactory.getCurrentSession();
        account.setOpenDate(new Date(Calendar.getInstance().getTimeInMillis()));
        session.persist(account);
        return account;
    }

    public void deleteById(int id) throws InputParameterValidationException {
        Session session = sessionFactory.getCurrentSession();
        Account account = session.get(Account.class, id);
        if (account == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
        session.delete(account);
// TODO: 24.05.2019 rework using service
//        cardRepository.deleteNotActive();
    }

    public Account getById(int id) {
        Session session = sessionFactory.getCurrentSession();
        Account dBAccount = session.get(Account.class, id);
        Currency currency = new Currency(
                dBAccount.getCurrency().getCode(),
                dBAccount.getCurrency().getRate(),
                dBAccount.getCurrency().getName(),
                dBAccount.getCurrency().getIso()
        );
        Client client = new Client(
                dBAccount.getClient().getId(),
                dBAccount.getClient().getLastName(),
                dBAccount.getClient().getFirstName()
        );
        Set<Card> cards = new HashSet<>();
        for (Card card : dBAccount.getCards()) {
            cards.add(new Card(card.getId(), card.getNumber()));
        }
        return new Account(
                dBAccount.getId(),
                dBAccount.getNumber(),
                currency,
                dBAccount.getType(),
                dBAccount.getBalance(),
                dBAccount.getOpenDate(),
                client,
                cards
        );
    }

    public List<Account> getByClientId(int id) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Account> criteriaQuery = criteriaBuilder.createQuery(Account.class);
        Root<Account> accountRoot = criteriaQuery.from(Account.class);

        return session.createQuery(criteriaQuery.select(accountRoot)
                .where(criteriaBuilder.equal(accountRoot.get("client").get("id"), id)))
                .getResultList();
    }

    @Transactional
    public void update(Account account) throws InputParameterValidationException {
        Session session = sessionFactory.getCurrentSession();
        if (session.get(Account.class, account.getId()) == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
        session.evict(account);
        session.merge(account);
    }
}
