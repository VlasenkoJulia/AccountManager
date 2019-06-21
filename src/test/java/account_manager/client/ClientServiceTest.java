package account_manager.client;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientValidator validator;
    @InjectMocks
    ClientService clientService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = InputParameterValidationException.class)
    public void getById_ClientNotFound_ShouldThrowException() {
        when(clientRepository.getById(1)).thenReturn(null);
        doThrow(InputParameterValidationException.class).when(validator).validateGet(null);
        clientService.getById(1);
    }

    @Test
    public void getById_ClientFound_ShouldReturnClient() {
        Client client = createClient(1);
        when(clientRepository.getById(1)).thenReturn(client);
        doNothing().when(validator).validateGet(client);
        Client clientFound = clientService.getById(1);
        assertEquals(client, clientFound);
    }

    @Test(expected = InputParameterValidationException.class)
    public void create_ClientIsNotValid_ShouldThrowException() {
        Client client = createClient(1);
        doThrow(InputParameterValidationException.class).when(validator).validateCreate(client);
        clientService.create(client);
    }

    @Test
    public void create_ClientIsValid_ShouldReturnSuccessMessage() {
        Client client = createClient(null);
        doNothing().when(validator).validateCreate(client);
        when(clientRepository.create(client)).thenReturn(createClient(1));
        String message = clientService.create(client);
        Assert.assertEquals("Created client #1", message);
    }

    @Test(expected = InputParameterValidationException.class)
    public void update_ClientIsNotValid_ShouldThrowException() {
        Client client = createClient(null);
        doThrow(InputParameterValidationException.class).when(validator).validateUpdate(client);
        clientService.update(client);
    }

    @Test
    public void update_ClientIsValid_ShouldReturnSuccessMessage() {
        Client client = createClient(1);
        doNothing().when(validator).validateUpdate(client);
        doNothing().when(clientRepository).update(client);
        String message = clientService.update(client);
        Assert.assertEquals("Client updated successfully", message);
    }

    @Test
    public void deleteById_ClientFound_ShouldReturnSuccessMessage() {
        doNothing().when(clientRepository).deleteById(1);
        String message = clientService.deleteById(1);
        Assert.assertEquals("Deleted client #1", message);
    }

    private Client createClient(Integer clientId) {
        Client client = new Client();
        client.setId(clientId);
        return client;
    }
}