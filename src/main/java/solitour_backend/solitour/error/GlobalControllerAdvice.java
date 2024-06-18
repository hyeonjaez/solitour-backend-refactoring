package solitour_backend.solitour.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import solitour_backend.solitour.error.exception.RequestValidationFailedException;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(RequestValidationFailedException.class)
    public ResponseEntity<String> validationException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}
