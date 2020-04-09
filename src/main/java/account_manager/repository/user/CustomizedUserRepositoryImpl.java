package account_manager.repository.user;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public class CustomizedUserRepositoryImpl
        implements CustomizedUserRepository<User> {
    
    private final SessionFactory sessionFactory;

    @Autowired
    public CustomizedUserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void update(User user) {
        Session session = sessionFactory.getCurrentSession();
        if (session.get(User.class, user.getUserName()) == null) {
            throw new InputParameterValidationException("User with passed username do not exist");
        }
        session.evict(user);
        session.merge(user);
    }
}
