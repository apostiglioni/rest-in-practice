package posti.social.ports.rest.exception;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.lang.String.format;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@ControllerAdvice
@Order(LOWEST_PRECEDENCE)
public class CatchAllExceptionHandler {
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorsResponseBody> handle(Throwable exception) {
        UUID exceptionId = UUID.randomUUID();
        String message = format("Internal server error: %s", exceptionId);

        Logger logger = LoggerFactory.getLogger(CatchAllExceptionHandler.class);
        logger.error(message, exception);

        return new ErrorResponseBuilder()
                     .withMessage(message)
                     .internalServerError();
    }
}
