package account_manager.repository.client;

import account_manager.repository.AbstractTestRepository;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class ClientEntityRepositoryTest extends AbstractTestRepository {
    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void create() {
        ClientEntity clientEntity = new ClientEntity(null, "John", "Doe");
        clientRepository.save(clientEntity);
        assertEquals(1, clientRepository.count());
    }

    @Test
    public void update() {
        ClientEntity clientEntity = new ClientEntity(null, "John", "Doe");
        clientRepository.save(clientEntity);
        clientRepository.update(new ClientEntity(clientEntity.getId(), "John", "Black"));
        Optional<ClientEntity> updatedClient = clientRepository.findById(clientEntity.getId());
        assertThat(updatedClient).isPresent();
        assertEquals("Black", updatedClient.get().getFirstName());
    }

    @Test(expected = InputParameterValidationException.class)
    public void updateInvalidClient_ShouldThrowException() {
        ClientEntity clientEntity = new ClientEntity(null, "John", "Doe");
        clientRepository.save(clientEntity);
        clientRepository.update(new ClientEntity(clientEntity.getId() + 1, "John", "Black"));
    }

    @Test
    public void findById() {
        ClientEntity clientEntity = new ClientEntity(null, "John", "Doe");
        clientRepository.save(clientEntity);
        Optional<ClientEntity> foundClient = clientRepository.findById(clientEntity.getId());
        assertThat(foundClient).isPresent();
        assertEquals(clientEntity, foundClient.get());
    }

    @Test
    public void deleteById() {
        ClientEntity clientEntity = new ClientEntity(null, "John", "Doe");
        clientRepository.save(clientEntity);
        clientRepository.deleteById(clientEntity.getId());
        assertEquals(0, clientRepository.count());
    }

    @Test(expected = InputParameterValidationException.class)
    public void deleteInvalidClient_ShouldThrowException() {
        ClientEntity clientEntity = new ClientEntity(null, "John", "Doe");
        clientRepository.save(clientEntity);
        clientRepository.deleteById(clientEntity.getId() + 1);
    }
}
