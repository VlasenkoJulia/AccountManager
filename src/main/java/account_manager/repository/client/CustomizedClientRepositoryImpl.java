package account_manager.repository.client;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class CustomizedClientRepositoryImpl
        implements CustomizedClientRepository<ClientEntity, Integer> {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void deleteById(Integer id) {
        ClientEntity clientEntity = em.find(ClientEntity.class, id);
        if (clientEntity == null) {
            throw new InputParameterValidationException("Client with passed ID do not exist");
        }
        em.remove(clientEntity);
    }

    @Override
    public void update(ClientEntity clientEntity) {
        if (em.find(ClientEntity.class, clientEntity.getId()) == null) {
            throw new InputParameterValidationException("Client with passed ID do not exist");
        }
        em.merge(clientEntity);
    }
}
