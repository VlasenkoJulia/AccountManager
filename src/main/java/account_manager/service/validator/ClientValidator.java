package account_manager.service.validator;

import account_manager.repository.client.Client;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Component;

@Component
public class ClientValidator {

    public void validateGet(Client client) {
        if (client == null) {
            throw new InputParameterValidationException("Client with passed ID do not exist");
        }
    }

    public void validateCreate(Client client) {
        if (client.getId() != null) {
            throw new InputParameterValidationException("Can not provide insert operation with passed client");
        }
    }

    public void validateUpdate(Client client) {
        if (client.getId() == null) {
            throw new InputParameterValidationException("Can not provide setResetToken operation with passed client");
        }
    }
}
