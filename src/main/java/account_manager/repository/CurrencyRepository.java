package account_manager.repository;

import account_manager.repository.entity.Currency;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public class CurrencyRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public CurrencyRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Currency getCurrency(String currencyCode) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Currency.class, currencyCode);
    }
}
