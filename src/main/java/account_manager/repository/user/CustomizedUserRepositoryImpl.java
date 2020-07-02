package account_manager.repository.user;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Repository
@Transactional
public class CustomizedUserRepositoryImpl
        implements CustomizedUserRepository<UserEntity> {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void update(UserEntity userEntity) {
        if (em.find(UserEntity.class, userEntity.getUserName()) == null) {
            throw new InputParameterValidationException("User with passed username do not exist");
        }
        em.merge(userEntity);
    }
}
