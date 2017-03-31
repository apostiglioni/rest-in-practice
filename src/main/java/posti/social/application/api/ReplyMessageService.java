package posti.social.application.api;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.transaction.annotation.Transactional;
import posti.social.application.domain.Message;
import posti.social.application.domain.MessageRepository;
import posti.social.application.domain.User;
import posti.social.application.domain.UserRepository;

public class ReplyMessageService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public ReplyMessageService(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public Message execute(ValidatingSupplier<Request> supplier)
            throws MessageNotFoundException, UserNotFoundException, ConstraintViolationException {

        Request request = supplier.getValidOrFail();

        Message message = messageRepository.findById(request.getMessageId()).orElseThrow(MessageNotFoundException::new);
        User user = userRepository.findById(request.getAuthorId()).orElseThrow(UserNotFoundException::new);
        Message reply = user.reply(request.getContent(), message);

        return reply;
    }

    public interface Request {
        @NotNull
        UUID getAuthorId();

        @NotNull
        UUID getMessageId();

        @NotNull(message = "content.empty.message")
        @Size(min = 1, max = 140, message = "content.size.invalid.message")
        String getContent();
    }
}
