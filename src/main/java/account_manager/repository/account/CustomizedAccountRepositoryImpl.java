package account_manager.repository.account;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class CustomizedAccountRepositoryImpl
        implements CustomizedAccountRepository<AccountEntity, Integer> {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void deleteById(Integer id) {
        AccountEntity accountEntity = em.find(AccountEntity.class, id);
        if (accountEntity == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
        em.remove(accountEntity);
    }

    @Override
    public void update(AccountEntity accountEntity) {
        if (em.find(AccountEntity.class, accountEntity.getId()) == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
        em.merge(accountEntity);
    }
}
