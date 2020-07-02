package account_manager.client;

import account_manager.repository.client.ClientEntity;
import account_manager.repository.client.ClientRepository;
import account_manager.service.ClientDocument;
import account_manager.service.ClientService;
import account_manager.service.SearchClient;
import account_manager.service.converter.Converter;
import account_manager.service.dto.ClientDto;
import account_manager.service.validator.ClientValidator;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@Ignore
// TODO: 7/2/2020 fix tests!
public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientValidator validator;
    @Mock
    private Converter<ClientEntity, ClientDto> converter;
    @Mock
    private Converter<ClientDocument, ClientEntity> documentConverter;
    @Mock
    private SearchClient<ClientDocument> elasticSearchClient;
    @InjectMocks
    ClientService clientService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private static final ClientEntity client = new ClientEntity(1, "Doe", "John",
            "123456789", "Some City", "Some Street", "123", "123");
    private static final ClientDocument document = new ClientDocument(1, "Doe", "John",
            "123456789", "Some City", "Some Street", "123", "123");
    private static final ClientDto dto = new ClientDto(1, "Doe", "John",
            "123456789", "Some City", "Some Street", "123", "123");

    @Test(expected = InputParameterValidationException.class)
    public void getById_ClientNotFound_ShouldThrowException() {
        when(clientRepository.findById(1)).thenReturn(Optional.empty());
        doThrow(InputParameterValidationException.class).when(validator).validateGet(null);
        clientService.getById(1);
    }

    @Test
    public void getById_ClientFound_ShouldReturnClient() {
        ClientEntity clientEntity = createClient(1);
        when(clientRepository.findById(1)).thenReturn(Optional.of(clientEntity));
        doNothing().when(validator).validateGet(clientEntity);
        ClientDto clientDto = createClientDto(1);
        when(converter.convertFrom(clientEntity)).thenReturn(clientDto);
        ClientDto clientFound = clientService.getById(1);
        assertEquals(clientDto, clientFound);
    }

    @Test(expected = InputParameterValidationException.class)
    public void create_ClientIsNotValid_ShouldThrowException() {
        ClientDto clientDto = createClientDto(1);
        ClientEntity clientEntity = createClient(1);
        when(converter.convertTo(clientDto)).thenReturn(clientEntity);
        doThrow(InputParameterValidationException.class).when(validator).validateCreate(clientEntity);
        clientService.create(clientDto);
    }

    @Test
    public void create_ClientIsValid_ShouldReturnSuccessMessage() {
        ClientDto clientDto = createClientDto(null);
        ClientEntity clientEntity = createClient(null);
        when(converter.convertTo(clientDto)).thenReturn(clientEntity);
        doNothing().when(validator).validateCreate(clientEntity);
        ClientEntity clientEntityCreated = createClient(1);
        when(clientRepository.save(clientEntity)).thenReturn(clientEntityCreated);
        ClientDocument document = new ClientDocument();
        when(documentConverter.convertTo(clientEntity)).thenReturn(document);
        doNothing().when(elasticSearchClient).index(document);
        String message = clientService.create(clientDto);
        Assert.assertEquals("Created client #1", message);
    }

    @Test(expected = InputParameterValidationException.class)
    public void update_ClientIsNotValid_ShouldThrowException() {
        ClientEntity clientEntity = createClient(null);
        ClientDto clientDto = createClientDto(null);
        when(converter.convertTo(clientDto)).thenReturn(clientEntity);
        doThrow(InputParameterValidationException.class).when(validator).validateUpdate(clientEntity);
        clientService.update(clientDto);
    }

    @Test
    public void update_ClientIsValid_ShouldReturnSuccessMessage() {
        ClientEntity clientEntity = createClient(1);
        ClientDto clientDto = createClientDto(1);
        when(converter.convertTo(clientDto)).thenReturn(clientEntity);
        doNothing().when(validator).validateUpdate(clientEntity);
        doNothing().when(clientRepository).update(clientEntity);
        ClientDocument document = new ClientDocument();
        when(documentConverter.convertTo(clientEntity)).thenReturn(document);
        doNothing().when(elasticSearchClient).update(document);
        String message = clientService.update(clientDto);
        Assert.assertEquals("Client updated successfully", message);
    }

    @Test
    public void deleteById_ClientFound_ShouldReturnSuccessMessage() {
        doNothing().when(clientRepository).deleteById(1);
        doNothing().when(elasticSearchClient).delete("1");
        String message = clientService.deleteById(1);
        Assert.assertEquals("Deleted client #1", message);
    }

    @Test
    public void index() {
        when(clientRepository.findAll()).thenReturn(List.of(client));
        when(documentConverter.convertTo(client)).thenReturn(document);
        List<ClientDocument> documents = List.of(document);
        doNothing().when(elasticSearchClient).bulk(documents);
        clientService.index();
        verify(clientRepository).findAll();
        verify(elasticSearchClient).bulk(documents);
    }

    @Test
    public void search() {
        when(elasticSearchClient.search("Doe")).thenReturn(List.of(document));
        when(documentConverter.convertFrom(document)).thenReturn(client);
        when(converter.convertFrom(client)).thenReturn(dto);
        List<ClientDto> expectedResult = List.of(dto);
        when(clientService.search("Doe")).thenReturn(expectedResult);
        List<ClientDto> result = clientService.search("Doe");
        Assertions.assertThat(result).containsExactly(dto);
    }

    private ClientEntity createClient(Integer clientId) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(clientId);
        return clientEntity;
    }

    private ClientDto createClientDto(Integer clientId) {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(clientId);
        return clientDto;
    }
}
