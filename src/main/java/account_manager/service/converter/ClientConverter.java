package account_manager.service.converter;

import account_manager.repository.client.Client;
import account_manager.service.dto.ClientDto;
import org.springframework.stereotype.Service;

@Service
public class ClientConverter implements Converter<Client, ClientDto> {
    @Override
    public Client convertTo(ClientDto dto) {
      return new Client(dto.getId(), dto.getLastName(), dto.getFirstName());
    }

    @Override
    public ClientDto convertFrom(Client client) {
        return new ClientDto(client.getId(), client.getLastName(), client.getFirstName());
    }
}
