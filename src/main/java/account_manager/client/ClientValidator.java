package account_manager.client;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Component;

@Component
public class ClientValidator {

    void validateGet(Client client) {
        if (client == null) {
            throw new InputParameterValidationException("Client with passed ID do not exist");
        }
    }

    void validateCreate(Client client) {
        if (client.getId() != null) {
            throw new InputParameterValidationException("Can not provide insert operation with passed client");
        }
    }

    void validateUpdate(Client client) {
        if (client.getId() == null) {
            throw new InputParameterValidationException("Can not provide setResetToken operation with passed client");
        }
    }
}
