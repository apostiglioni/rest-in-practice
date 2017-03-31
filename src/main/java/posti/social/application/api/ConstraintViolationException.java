package posti.social.application.api;

import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;

public class ConstraintViolationException extends Exception {
    private final Set<ConstraintViolation<?>> constraintViolations;

    /**
     * Creates a constraint violation report.
     *
     * @param message error message
     * @param constraintViolations {@code Set} of {@link ConstraintViolation}
     */
    public ConstraintViolationException(String message,
                                        Set<? extends ConstraintViolation<?>> constraintViolations) {
        super( message );

        if ( constraintViolations == null ) {
            this.constraintViolations = null;
        }
        else {
            this.constraintViolations = new HashSet<ConstraintViolation<?>>( constraintViolations );
        }
    }

    /**
     * Creates a constraint violation report.
     *
     * @param constraintViolations {@code Set} of {@link ConstraintViolation}
     */
    public ConstraintViolationException(Set<? extends ConstraintViolation<?>> constraintViolations) {
        this( null, constraintViolations );
    }

    /**
     * Set of constraint violations reported during a validation.
     *
     * @return {@code Set} of {@link ConstraintViolation}
     */
    public Set<ConstraintViolation<?>> getConstraintViolations() {
        return constraintViolations;
    }
}
