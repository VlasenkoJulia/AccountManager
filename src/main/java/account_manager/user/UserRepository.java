package account_manager.user;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public class UserRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public User create(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(user);
        return user;
    }

    public User getByUsername(String userName) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class, userName);
    }
}
