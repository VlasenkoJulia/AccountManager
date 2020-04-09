package account_manager.repository.account;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CustomizedAccountRepositoryImpl
        implements CustomizedAccountRepository<Account, Integer> {
    private final SessionFactory sessionFactory;

    @Autowired
    public CustomizedAccountRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void deleteById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        Account account = session.get(Account.class, id);
        if (account == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
        session.delete(account);
    }

    @Override
    public void update(Account account) {
        Session session = sessionFactory.getCurrentSession();
        if (session.get(Account.class, account.getId()) == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
        session.evict(account);
        session.merge(account);
    }
}
