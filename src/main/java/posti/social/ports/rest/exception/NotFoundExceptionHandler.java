package posti.social.ports.rest.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import posti.social.application.api.MessageNotFoundException;
import posti.social.application.api.UserNotFoundException;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.ResponseEntity.notFound;

@ControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class NotFoundExceptionHandler {
    @ExceptionHandler({ UserNotFoundException.class, MessageNotFoundException.class })
    public ResponseEntity<Void> handle() {
        return notFound().build();
    }
}
