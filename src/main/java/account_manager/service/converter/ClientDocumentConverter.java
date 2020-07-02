package account_manager.service.converter;

import account_manager.repository.client.ClientEntity;
import account_manager.service.ClientDocument;
import org.springframework.stereotype.Service;

@Service
public class ClientDocumentConverter implements Converter<ClientDocument, ClientEntity> {
    @Override
    public ClientDocument convertTo(ClientEntity entity) {
        return new ClientDocument(entity.getId(),
                entity.getLastName(),
                entity.getFirstName(),
                entity.getSocialNumber(),
                entity.getCity(),
                entity.getStreet(),
                entity.getHouseNumber(),
                entity.getApartment());
    }

    @Override
    public ClientEntity convertFrom(ClientDocument document) {
        return new ClientEntity(document.getId(),
                document.getLastName(),
                document.getFirstName(),
                document.getSocialNumber(),
                document.getCity(),
                document.getStreet(),
                document.getHouseNumber(),
                document.getApartment());
    }
}
