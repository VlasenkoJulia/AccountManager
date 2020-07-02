package account_manager.service;

import account_manager.repository.client.ClientEntity;
import account_manager.repository.client.ClientRepository;
import account_manager.service.converter.Converter;
import account_manager.service.dto.ClientDto;
import account_manager.service.validator.ClientValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientService.class.getName());

    private final ClientRepository clientRepository;
    private final ClientValidator validator;
    private final Converter<ClientEntity, ClientDto> converter;
    private final Converter<ClientDocument, ClientEntity> documentConverter;
    private final SearchClient<ClientDocument> elasticSearchClient;

    @Autowired
    public ClientService(ClientRepository clientRepository,
                         ClientValidator validator, Converter<ClientEntity, ClientDto> converter,
                         Converter<ClientDocument, ClientEntity> documentConverter,
                         SearchClient<ClientDocument> elasticSearchClient) {
        this.clientRepository = clientRepository;
        this.validator = validator;
        this.converter = converter;
        this.documentConverter = documentConverter;
        this.elasticSearchClient = elasticSearchClient;
    }

    public void index() {
        List<ClientDocument> clients = clientRepository.findAll()
                .stream()
                .map(documentConverter::convertTo)
                .collect(Collectors.toList());
        elasticSearchClient.bulk(clients);
    }

    public List<ClientDto> search(String searchText) {
        return elasticSearchClient.search(searchText)
                .stream()
                .map(documentConverter::convertFrom)
                .map(converter::convertFrom)
                .collect(Collectors.toList());
    }

    public ClientDto getById(Integer clientId) {
        ClientEntity clientEntity = clientRepository.findById(clientId).orElse(null);
        validator.validateGet(clientEntity);
        return converter.convertFrom(clientEntity);
    }

    public String create(ClientDto clientDto) {
        ClientEntity client = converter.convertTo(clientDto);
        validator.validateCreate(client);
        ClientEntity clientCreated = clientRepository.save(client);
        elasticSearchClient.index(documentConverter.convertTo(clientCreated));
        log.info("Created client #{}", clientCreated.getId());
        return "Created client #" + clientCreated.getId();
    }

    public String update(ClientDto clientDto) {
        ClientEntity client = converter.convertTo(clientDto);
        validator.validateUpdate(client);
        clientRepository.update(client);
        elasticSearchClient.update(documentConverter.convertTo(client));
        log.info("Updated client #{}", client.getId());
        return "Client updated successfully";
    }

    public String deleteById(Integer clientId) {
        clientRepository.deleteById(clientId);
        elasticSearchClient.delete(clientId.toString());
        log.info("Deleted client #{}", clientId);
        return "Deleted client #" + clientId;
    }
}
