package account_manager.client;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClientRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public ClientRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Client create(Client client) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(client);
        return client;
    }

    public Client getById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Client.class, id);
    }

    public void deleteById(int id) {
        Session session = sessionFactory.getCurrentSession();
        Client client = session.get(Client.class, id);
        if (client == null) {
            throw new InputParameterValidationException("Client with passed ID do not exist");
        }
        session.delete(client);
    }

    public Client update(Client client) {
        Session session = sessionFactory.getCurrentSession();
        if (session.get(Client.class, client.getId()) == null) {
            throw new InputParameterValidationException("Client with passed ID do not exist");
        }
        session.evict(client);
        session.merge(client);
        return client;
    }
}
