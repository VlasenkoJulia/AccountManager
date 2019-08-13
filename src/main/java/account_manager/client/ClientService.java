package account_manager.client;

import account_manager.LoggerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private static Logger log = LoggerFactory.getLogger(ClientService.class.getName());

    private final ClientRepository clientRepository;
    private final ClientValidator validator;

    @Autowired
    public ClientService(ClientRepository clientRepository, ClientValidator validator) {
        this.clientRepository = clientRepository;
        this.validator = validator;
    }

    public Client getById(Integer clientId) {
        Client client = clientRepository.getById(clientId);
        validator.validateGet(client);
        return client;
    }

    public String create(Client client) {
        validator.validateCreate(client);
        Client clientCreated = clientRepository.create(client);
        log.info("Created client #{}", clientCreated.getId());
        return "Created client #" + clientCreated.getId();
    }

    public String update(Client client) {
        validator.validateUpdate(client);
        clientRepository.update(client);
        log.info("Updated client #{}", client.getId());
        return "Client updated successfully";
    }

    public String deleteById(Integer clientId) {
        clientRepository.deleteById(clientId);
        log.info("Deleted client #{}", clientId);
        return "Deleted client #" + clientId;
    }
}
