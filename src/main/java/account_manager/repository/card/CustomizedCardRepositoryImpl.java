package account_manager.repository.card;

import account_manager.repository.account.AccountEntity;
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
        implements CustomizedCardRepository<CardEntity, Integer> {
    @PersistenceContext
    private EntityManager em;


    @Override
    public void deleteById(Integer id) {
        CardEntity cardEntity = em.find(CardEntity.class, id);
        if (cardEntity == null) {
            throw new InputParameterValidationException("Card with passed ID do not exist");
        }
        em.remove(cardEntity);
        for (AccountEntity accountEntity : cardEntity.getAccounts()) {
            accountEntity.getCards().remove(cardEntity);
        }
    }

    @Override
    public Set<CardEntity> findByAccountId(Integer accountId) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<CardEntity> criteriaQuery = criteriaBuilder.createQuery(CardEntity.class);
        Root<AccountEntity> accountRoot = criteriaQuery.from(AccountEntity.class);

        criteriaQuery.where(criteriaBuilder.equal(accountRoot.get("id"),
                accountId));
        Join<AccountEntity, CardEntity> accounts = accountRoot.join("cards");
        CriteriaQuery<CardEntity> cq = criteriaQuery.select(accounts);
        TypedQuery<CardEntity> query = em.createQuery(cq);
        return Set.copyOf(query.getResultList());
    }
}
