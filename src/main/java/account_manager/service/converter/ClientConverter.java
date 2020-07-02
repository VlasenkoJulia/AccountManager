package account_manager.service.converter;

import account_manager.repository.client.ClientEntity;
import account_manager.service.dto.ClientDto;
import org.springframework.stereotype.Service;

@Service
public class ClientConverter implements Converter<ClientEntity, ClientDto> {
    @Override
    public ClientEntity convertTo(ClientDto dto) {
      return new ClientEntity(dto.getId(),
              dto.getLastName(),
              dto.getFirstName(),
              dto.getSocialNumber(),
              dto.getCity(),
              dto.getStreet(),
              dto.getHouseNumber(),
              dto.getApartment());
    }

    @Override
    public ClientDto convertFrom(ClientEntity clientEntity) {
        return new ClientDto(clientEntity.getId(),
                clientEntity.getLastName(),
                clientEntity.getFirstName(),
                clientEntity.getSocialNumber(),
                clientEntity.getCity(),
                clientEntity.getStreet(),
                clientEntity.getHouseNumber(),
                clientEntity.getApartment());
    }
}
