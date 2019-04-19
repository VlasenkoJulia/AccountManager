package account_manager.web.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorDto handleValidationException(ValidationException exception) {
        String message = exception.getMessage();
        return new ErrorDto(message, ErrorType.INVALID);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorDto handleRuntimeException(RuntimeException exception) {
        String message = exception.getMessage();
        return new ErrorDto(message, ErrorType.UNKNOWN);
    }
}
