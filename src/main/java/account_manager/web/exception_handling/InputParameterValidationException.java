package account_manager.web.exception_handling;

public class InputParameterValidationException extends ValidationException {
    public InputParameterValidationException(String message) {
        super(message);
    }
}
