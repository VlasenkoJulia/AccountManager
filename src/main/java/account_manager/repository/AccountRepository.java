package account_manager.repository;

import account_manager.repository.entity.Account;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class AccountRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public AccountRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Account create(Account account) {
        Session session = sessionFactory.getCurrentSession();
        account.setOpenDate(new Date(Calendar.getInstance().getTimeInMillis()));
        session.persist(account);
        return account;
    }

    public void deleteById(int id) {
        Session session = sessionFactory.getCurrentSession();
        Account account = session.get(Account.class, id);
        if (account == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
        session.delete(account);
    }

    public Account getById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Account.class, id);
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
    public void update(Account account) {
        Session session = sessionFactory.getCurrentSession();
        if (session.get(Account.class, account.getId()) == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
        session.evict(account);
        session.merge(account);
    }
}
