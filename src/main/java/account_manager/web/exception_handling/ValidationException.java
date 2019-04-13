package account_manager.web.exception_handling;

class ValidationException extends Exception {
    ValidationException(String message) {
        super(message);
    }
}
