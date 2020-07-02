package account_manager.service.converter;

import account_manager.repository.client.ClientEntity;
import account_manager.service.dto.ClientDto;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientConverterTest {
    private final ClientConverter clientConverter = new ClientConverter();
    private static final ClientEntity CLIENT_ENTITY = new ClientEntity(1, "Doe", "John",
            "123456789", "Some City", "Some Street", "123", "123");

    private static final ClientDto DTO = new ClientDto(1, "Doe", "John",
            "123456789", "Some City", "Some Street", "123", "123");
    @Test
    public void convertToEntityFromDto() {
        ClientEntity clientEntityConverted = clientConverter.convertTo(DTO);
        assertThat(clientEntityConverted.getId()).isEqualTo(CLIENT_ENTITY.getId());
        assertThat(clientEntityConverted.getFirstName()).isEqualTo(CLIENT_ENTITY.getFirstName());
        assertThat(clientEntityConverted.getLastName()).isEqualTo(CLIENT_ENTITY.getLastName());
        assertThat(clientEntityConverted.getSocialNumber()).isEqualTo(CLIENT_ENTITY.getSocialNumber());
        assertThat(clientEntityConverted.getCity()).isEqualTo(CLIENT_ENTITY.getCity());
        assertThat(clientEntityConverted.getStreet()).isEqualTo(CLIENT_ENTITY.getStreet());
        assertThat(clientEntityConverted.getHouseNumber()).isEqualTo(CLIENT_ENTITY.getHouseNumber());
        assertThat(clientEntityConverted.getApartment()).isEqualTo(CLIENT_ENTITY.getApartment());
    }

    @Test
    public void convertFromEntityToDto() {
        ClientDto dtoConverted = clientConverter.convertFrom(CLIENT_ENTITY);
        assertThat(dtoConverted).isEqualTo(DTO);
    }
}
