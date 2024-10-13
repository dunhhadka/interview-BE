package org.example.interview.user.infrastructure.configuration.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.example.interview.user.application.exception.ConstraintViolationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class HandleCommonException {

    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorMessage doException(ConstraintViolationException exception, HttpServletResponse response) {
        if (exception.getStatus() != null) {
            response.setStatus(exception.getStatus());
        }
        return ErrorMessage.builder()
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorMessage handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String message = String.join(", ", errors.values());
        return ErrorMessage.builder()
                .message(message)
                .build();
    }

}
