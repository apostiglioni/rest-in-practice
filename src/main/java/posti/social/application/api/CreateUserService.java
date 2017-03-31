package posti.social.application.api;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import posti.social.application.domain.User;
import posti.social.application.domain.UserRepository;
import org.springframework.transaction.annotation.Transactional;

public class CreateUserService {
    private final UserRepository repository;

    public CreateUserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public User execute(ValidatingSupplier<Request> supplier) throws ConstraintViolationException {
        Request request = supplier.getValidOrFail();

        User newUser = new User(request.getUsername(), request.getEmail());
        return repository.save(newUser);
    }

    public interface Request {
        @NotNull(message = "username.empty.message")
        @Size(min = 1, message = "username.empty.message")
        String getUsername();

        @NotNull(message = "email.empty.message")
        @Size(min = 1, message = "email.empty.message")
        String getEmail();
    }
}
