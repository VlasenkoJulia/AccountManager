package account_manager.service.validator;

import account_manager.repository.client.ClientEntity;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.stereotype.Component;

@Component
public class ClientValidator {

    public void validateGet(ClientEntity clientEntity) {
        if (clientEntity == null) {
            throw new InputParameterValidationException("Client with passed ID do not exist");
        }
    }

    public void validateCreate(ClientEntity clientEntity) {
        if (clientEntity.getId() != null) {
            throw new InputParameterValidationException("Can not provide insert operation with passed client");
        }
    }

    public void validateUpdate(ClientEntity clientEntity) {
        if (clientEntity.getId() == null) {
            throw new InputParameterValidationException("Can not provide setResetToken operation with passed client");
        }
    }
}
