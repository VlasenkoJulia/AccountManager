package account_manager.repository.user;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Repository
@Transactional
public class CustomizedUserRepositoryImpl
        implements CustomizedUserRepository<User> {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void update(User user) {
        if (em.find(User.class, user.getUserName()) == null) {
            throw new InputParameterValidationException("User with passed username do not exist");
        }
        em.merge(user);
    }
}
