package account_manager.web.exception_handling;

class ValidationException extends RuntimeException {
    ValidationException(String message) {
        super(message);
    }
}
