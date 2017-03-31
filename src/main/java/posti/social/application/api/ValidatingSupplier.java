package posti.social.application.api;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

public class ValidatingSupplier<T> {
    private final Validator validator;
    private final T target;

    public ValidatingSupplier(Validator validator, T target) {
        this.validator = validator;
        this.target = target;
    }

    public T getValidOrFail() throws ConstraintViolationException {
        Set<ConstraintViolation<T>> violations = validator.validate(target);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        } else {
            return target;
        }
    }
}
