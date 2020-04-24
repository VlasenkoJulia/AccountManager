package account_manager.repository.client;

import account_manager.repository.AbstractTestRepository;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class ClientRepositoryTest extends AbstractTestRepository {
    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void create() {
        Client client = new Client(null, "John", "Doe");
        clientRepository.save(client);
        assertEquals(1, clientRepository.count());
    }

    @Test
    public void update() {
        Client client = new Client(null, "John", "Doe");
        clientRepository.save(client);
        clientRepository.update(new Client(client.getId(), "John", "Black"));
        Optional<Client> updatedClient = clientRepository.findById(client.getId());
        assertThat(updatedClient).isPresent();
        assertEquals("Black", updatedClient.get().getFirstName());
    }

    @Test(expected = InputParameterValidationException.class)
    public void updateInvalidClient_ShouldThrowException() {
        Client client = new Client(null, "John", "Doe");
        clientRepository.save(client);
        clientRepository.update(new Client(client.getId() + 1, "John", "Black"));
    }

    @Test
    public void findById() {
        Client client = new Client(null, "John", "Doe");
        clientRepository.save(client);
        Optional<Client> foundClient = clientRepository.findById(client.getId());
        assertThat(foundClient).isPresent();
        assertEquals(client, foundClient.get());
    }

    @Test
    public void deleteById() {
        Client client = new Client(null, "John", "Doe");
        clientRepository.save(client);
        clientRepository.deleteById(client.getId());
        assertEquals(0, clientRepository.count());
    }

    @Test(expected = InputParameterValidationException.class)
    public void deleteInvalidClient_ShouldThrowException() {
        Client client = new Client(null, "John", "Doe");
        clientRepository.save(client);
        clientRepository.deleteById(client.getId() + 1);
    }
}
