package posti.social.application.api;

import java.util.UUID;
import javax.validation.constraints.NotNull;

import posti.social.application.domain.User;
import posti.social.application.domain.UserRepository;
import org.springframework.transaction.annotation.Transactional;

public class FollowUserService {
    private final UserRepository userRepository;

    public FollowUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void execute(ValidatingSupplier<Request> supplier) throws UserNotFoundException, ConstraintViolationException {
        Request request = supplier.getValidOrFail();

        User follower = userRepository.findById(request.getFollowerId()).orElseThrow(UserNotFoundException::new);
        User target = userRepository.findById(request.getTargetId()).orElseThrow(UserNotFoundException::new);

        follower.follow(target);
    }

    public interface Request {
        @NotNull UUID getFollowerId();
        @NotNull UUID getTargetId();
    }
}
