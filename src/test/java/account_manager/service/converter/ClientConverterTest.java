package account_manager.service.converter;

import account_manager.repository.client.Client;
import account_manager.service.dto.ClientDto;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientConverterTest {
    private ClientConverter clientConverter = new ClientConverter();

    @Test
    public void convertToEntityFromDto() {
        ClientDto dto = new ClientDto(1, "Doe", "John");
        Client client = clientConverter.convertTo(dto);
        assertThat(client.getId()).isEqualTo(dto.getId());
        assertThat(client.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(client.getLastName()).isEqualTo(dto.getLastName());
    }

    @Test
    public void convertFromEntityToDto() {
        Client client = new Client(1, "Doe", "John");
        ClientDto clientDto = clientConverter.convertFrom(client);
        assertThat(clientDto.getId()).isEqualTo(client.getId());
        assertThat(clientDto.getFirstName()).isEqualTo(client.getFirstName());
        assertThat(clientDto.getLastName()).isEqualTo(client.getLastName());
    }
}
