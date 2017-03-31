package posti.social.ports.rest.exception;

import java.util.List;

import static java.util.Collections.unmodifiableList;

public class ErrorsResponseBody {
    private final List<String> errors;

    public ErrorsResponseBody(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return unmodifiableList(errors);
    }
}
