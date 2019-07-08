package account_manager.web.exception_handling;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorDto)) return false;
        ErrorDto errorDto = (ErrorDto) o;
        return Objects.equals(getMessage(), errorDto.getMessage()) &&
                getType() == errorDto.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMessage(), getType());
    }
}
