package posti.social.application.api;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import posti.social.application.domain.Message;
import posti.social.application.domain.User;
import posti.social.application.domain.UserRepository;
import org.springframework.transaction.annotation.Transactional;

public class PublishMessageService {
    private final UserRepository userRepository;

    public PublishMessageService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Message execute(ValidatingSupplier<Request> supplier) throws UserNotFoundException, ConstraintViolationException {
        Request request = supplier.getValidOrFail();

        User user = userRepository.findById(request.getUserId()).orElseThrow(UserNotFoundException::new);
        Message message = user.publish(request.getMessage());

        return message;
    }

    public interface Request {
        @NotNull(message = "content.empty.message")
        @Size(min = 1, max = 140, message = "content.size.invalid.message")
        String getMessage();

        @NotNull(message = "publish.user.null.message")
        UUID getUserId();
    }
}
