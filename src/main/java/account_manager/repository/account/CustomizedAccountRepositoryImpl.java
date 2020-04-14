package account_manager.repository.account;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class CustomizedAccountRepositoryImpl
        implements CustomizedAccountRepository<Account, Integer> {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void deleteById(Integer id) {
        Account account = em.find(Account.class, id);
        if (account == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
        em.remove(account);
    }

    @Override
    public void update(Account account) {
        if (em.find(Account.class, account.getId()) == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
        em.merge(account);
    }
}
