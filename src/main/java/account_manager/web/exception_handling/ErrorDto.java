package account_manager.web.exception_handling;

public class ErrorDto {
    private String message;
    private ErrorType type;

    ErrorDto(String message, ErrorType type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public ErrorType getType() {
        return type;
    }
}
