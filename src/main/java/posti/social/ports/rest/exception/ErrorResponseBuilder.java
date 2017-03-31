package posti.social.ports.rest.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.validation.ConstraintViolation;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class ErrorResponseBuilder {
    private final ResourceBundleMessageSource messageSource;
    private final List<String> messages;
    private final Locale locale;

    public ErrorResponseBuilder() {
        this(Locale.getDefault());
    }

    public ErrorResponseBuilder(Locale locale) {
        this.locale = locale;
        messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("validation-messages");
        messages = new ArrayList<>();
    }

    public ErrorResponseBuilder withMessage(String message) {
       return this.withMessage(message, new Object[] {});
    }

    public ErrorResponseBuilder withMessage(String message, Object... args) {
        requireNonNull(message, "Can't build from a response without error message");

        messages.add(messageSource.getMessage(message, args, message,locale));

        return this;
    }

    public ErrorResponseBuilder forViolations(Set<ConstraintViolation<?>> violations) {
        requireNonNull(violations, "Can't build from null violations");

        violations.forEach(this::forViolation);

        return this;
    }

    public ErrorResponseBuilder forViolation(ConstraintViolation<?> violation) {
        withMessage(violation.getMessage(), violation.getExecutableReturnValue());
        return this;
    }

    public ResponseEntity<ErrorsResponseBody> build(HttpStatus status) {
        return new ResponseEntity<>(new ErrorsResponseBody(messages), status);
    }

    public ResponseEntity<ErrorsResponseBody> badRequest() {
        return this.build(BAD_REQUEST);
    }

    public ResponseEntity<ErrorsResponseBody> internalServerError() {
        return this.build(INTERNAL_SERVER_ERROR);
    }
}
