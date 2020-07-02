package account_manager.client;

import account_manager.repository.client.ClientEntity;
import account_manager.service.validator.ClientValidator;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Before;
import org.junit.Test;

public class ClientEntityValidatorTest {
    private ClientValidator validator;

    @Before
    public void setup() {
        validator = new ClientValidator();
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateGet_ClientIsNull_ShouldThrowException() {
        validator.validateGet(null);
    }

    @Test
    public void validateGet_ClientIsNotNull() {
        validator.validateGet(new ClientEntity());
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateCreate_ClientIdIsNotNull_ShouldThrowException() {
        validator.validateCreate(createClient(1));
    }

    @Test
    public void validateCreate_ClientIdIsNull() {
        validator.validateCreate(new ClientEntity());
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateUpdate_ClientIdIsNull_ShouldThrowException() {
        validator.validateUpdate(new ClientEntity());
    }

    @Test
    public void validateUpdate_ClientIdIsNotNull() {
        validator.validateUpdate(createClient(1));
    }


    private ClientEntity createClient(Integer clientId) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(clientId);
        return clientEntity;
    }

}
