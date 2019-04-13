package account_manager.web.exception_handling;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CustomExceptionHandler {
    private final Gson gson;

    @Autowired
    public CustomExceptionHandler(Gson gson) {
        this.gson = gson;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    protected String handleValidationException(ValidationException exception) {
        String message = exception.getMessage();
        ErrorDto errorDto = new ErrorDto(message, ErrorType.INVALID);
        return gson.toJson(errorDto);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    protected String handleRuntimeException(RuntimeException exception) {
        String message = exception.getMessage();
        ErrorDto errorDto = new ErrorDto(message, ErrorType.UNKNOWN);
        return gson.toJson(errorDto);
    }
}
