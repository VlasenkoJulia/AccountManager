package account_manager.client;

import account_manager.repository.client.Client;
import account_manager.repository.client.ClientRepository;
import account_manager.service.ClientService;
import account_manager.service.converter.Converter;
import account_manager.service.dto.ClientDto;
import account_manager.service.validator.ClientValidator;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientValidator validator;
    @Mock
    private  Converter<Client, ClientDto> converter;
    @InjectMocks
    ClientService clientService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = InputParameterValidationException.class)
    public void getById_ClientNotFound_ShouldThrowException() {
        when(clientRepository.findById(1)).thenReturn(Optional.empty());
        doThrow(InputParameterValidationException.class).when(validator).validateGet(null);
        clientService.getById(1);
    }

    @Test
    public void getById_ClientFound_ShouldReturnClient() {
        Client client = createClient(1);
        when(clientRepository.findById(1)).thenReturn(Optional.of(client));
        doNothing().when(validator).validateGet(client);
        ClientDto clientDto = createClientDto(1);
        when(converter.convertFrom(client)).thenReturn(clientDto);
        ClientDto clientFound = clientService.getById(1);
        assertEquals(clientDto, clientFound);
    }

    @Test(expected = InputParameterValidationException.class)
    public void create_ClientIsNotValid_ShouldThrowException() {
        ClientDto clientDto = createClientDto(1);
        Client client = createClient(1);
        when(converter.convertTo(clientDto)).thenReturn(client);
        doThrow(InputParameterValidationException.class).when(validator).validateCreate(client);
        clientService.create(clientDto);
    }

    @Test
    public void create_ClientIsValid_ShouldReturnSuccessMessage() {
        ClientDto clientDto = createClientDto(null);
        Client client = createClient(null);
        when(converter.convertTo(clientDto)).thenReturn(client);
        doNothing().when(validator).validateCreate(client);
        when(clientRepository.save(client)).thenReturn(createClient(1));
        String message = clientService.create(clientDto);
        Assert.assertEquals("Created client #1", message);
    }

    @Test(expected = InputParameterValidationException.class)
    public void update_ClientIsNotValid_ShouldThrowException() {
        Client client = createClient(null);
        ClientDto clientDto = createClientDto(null);
        when(converter.convertTo(clientDto)).thenReturn(client);
        doThrow(InputParameterValidationException.class).when(validator).validateUpdate(client);
        clientService.update(clientDto);
    }

    @Test
    public void update_ClientIsValid_ShouldReturnSuccessMessage() {
        Client client = createClient(1);
        ClientDto clientDto = createClientDto(1);
        when(converter.convertTo(clientDto)).thenReturn(client);
        doNothing().when(validator).validateUpdate(client);
        doNothing().when(clientRepository).update(client);
        String message = clientService.update(clientDto);
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

    private ClientDto createClientDto(Integer clientId) {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(clientId);
        return clientDto;
    }
}
