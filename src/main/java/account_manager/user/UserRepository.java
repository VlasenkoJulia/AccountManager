package account_manager.user;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


@Repository
@Transactional
public class UserRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(user);
    }

    public User getByUsername(String userName) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class, userName);
    }

    public User getByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);

        return DataAccessUtils.singleResult(session.createQuery(criteriaQuery.select(userRoot)
                .where(criteriaBuilder.equal(userRoot.get("email"), email)))
                .getResultList());
    }

    public User getByResetToken(String resetToken) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);

        return DataAccessUtils.singleResult(session.createQuery(criteriaQuery.select(userRoot)
                .where(criteriaBuilder.equal(userRoot.get("resetToken"), resetToken)))
                .getResultList());
    }

    public void update(User user) {
        Session session = sessionFactory.getCurrentSession();
        if (session.get(User.class, user.getUserName()) == null) {
            throw new InputParameterValidationException("User with passed username do not exist");
        }
        session.evict(user);
        session.merge(user);
    }
}
