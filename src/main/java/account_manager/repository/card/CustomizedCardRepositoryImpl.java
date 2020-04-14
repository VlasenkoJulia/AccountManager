package account_manager.repository.card;

import account_manager.repository.account.Account;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.Set;

@Repository
@Transactional
public class CustomizedCardRepositoryImpl
        implements CustomizedCardRepository<Card, Integer> {
    @PersistenceContext
    private EntityManager em;


    @Override
    public void deleteById(Integer id) {
        Card card = em.find(Card.class, id);
        if (card == null) {
            throw new InputParameterValidationException("Card with passed ID do not exist");
        }
        em.remove(card);
        for (Account account : card.getAccounts()) {
            account.getCards().remove(card);
        }
    }

    @Override
    public Set<Card> findByAccountId(Integer accountId) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Card> criteriaQuery = criteriaBuilder.createQuery(Card.class);
        Root<Account> accountRoot = criteriaQuery.from(Account.class);

        criteriaQuery.where(criteriaBuilder.equal(accountRoot.get("id"),
                accountId));
        Join<Account, Card> accounts = accountRoot.join("cards");
        CriteriaQuery<Card> cq = criteriaQuery.select(accounts);
        TypedQuery<Card> query = em.createQuery(cq);
        return Set.copyOf(query.getResultList());
    }
}
