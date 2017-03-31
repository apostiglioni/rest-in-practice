package posti.social.application.api;

import java.util.Optional;
import java.util.UUID;
import javax.validation.constraints.NotNull;

import posti.social.application.domain.Message;
import posti.social.application.domain.MessageRepository;
import org.springframework.transaction.annotation.Transactional;

public class GetMessageByIdService {
    private final MessageRepository repository;

    public GetMessageByIdService(MessageRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Optional<Message> execute(ValidatingSupplier<Request> supplier) throws ConstraintViolationException {
        Request request = supplier.getValidOrFail();

        return repository.findById(request.getMessageId());
    }

    public interface Request {
        @NotNull UUID getMessageId();
    }
}
