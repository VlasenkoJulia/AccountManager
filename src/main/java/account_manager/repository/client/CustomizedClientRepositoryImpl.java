package account_manager.repository.client;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class CustomizedClientRepositoryImpl
        implements CustomizedClientRepository<Client, Integer> {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void deleteById(Integer id) {
        Client client = em.find(Client.class, id);
        if (client == null) {
            throw new InputParameterValidationException("Client with passed ID do not exist");
        }
        em.remove(client);
    }

    @Override
    public void update(Client client) {
        if (em.find(Client.class, client.getId()) == null) {
            throw new InputParameterValidationException("Client with passed ID do not exist");
        }
        em.merge(client);
    }
}
