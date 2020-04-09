package account_manager.repository.client;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CustomizedClientRepositoryImpl
        implements CustomizedClientRepository<Client, Integer> {

    private final SessionFactory sessionFactory;

    @Autowired
    public CustomizedClientRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void deleteById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        Client client = session.get(Client.class, id);
        if (client == null) {
            throw new InputParameterValidationException("Client with passed ID do not exist");
        }
        session.delete(client);
    }

    @Override
    public void update(Client client) {
        Session session = sessionFactory.getCurrentSession();
        if (session.get(Client.class, client.getId()) == null) {
            throw new InputParameterValidationException("Client with passed ID do not exist");
        }
        session.evict(client);
        session.merge(client);
    }
}
