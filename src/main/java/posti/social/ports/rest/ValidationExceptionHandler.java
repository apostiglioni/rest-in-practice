package posti.social.ports.rest;

import posti.social.application.api.ConstraintViolationException;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.ResponseEntity.badRequest;

@ControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class ValidationExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handle(ConstraintViolationException exception) {
        return badRequest().body(ErrorsBody.builder().toResource(exception.getConstraintViolations()));
    }
}
