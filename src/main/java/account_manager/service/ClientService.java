package account_manager.service;

import account_manager.repository.client.Client;
import account_manager.repository.client.ClientRepository;
import account_manager.service.converter.Converter;
import account_manager.service.dto.ClientDto;
import account_manager.service.validator.ClientValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private static Logger log = LoggerFactory.getLogger(ClientService.class.getName());

    private final ClientRepository clientRepository;
    private final ClientValidator validator;
    private final Converter<Client, ClientDto> converter;

    @Autowired
    public ClientService(ClientRepository clientRepository, ClientValidator validator, Converter<Client, ClientDto> converter) {
        this.clientRepository = clientRepository;
        this.validator = validator;
        this.converter = converter;
    }

    public ClientDto getById(Integer clientId) {
        Client client = clientRepository.findById(clientId).orElse(null);
        validator.validateGet(client);
        return converter.convertFrom(client);
    }

    public String create(ClientDto clientDto) {
        Client client = converter.convertTo(clientDto);
        validator.validateCreate(client);
        Client clientCreated = clientRepository.save(client);
        log.info("Created client #{}", clientCreated.getId());
        return "Created client #" + clientCreated.getId();
    }

    public String update(ClientDto clientDto) {
        Client client = converter.convertTo(clientDto);
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
