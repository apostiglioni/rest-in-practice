package posti.social.application.api;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.valuehandling.UnwrapValidatedValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import posti.social.application.domain.FindMessageQuery;
import posti.social.application.domain.Message;

public class FindMessageCommand {
    private final FindMessageQuery query;

    public FindMessageCommand(FindMessageQuery query) {
        this.query = query;
    }

    @Transactional(readOnly = true)
    public Page<Message> execute(ValidatingSupplier<Request> supplier) throws ConstraintViolationException {
        Request request = supplier.getValidOrFail();

        query.setAuthorName(request.getAuthorName());
        query.setContains(request.getContains());
        query.setPublishedAfter(request.getPublishedAfter());
        query.setPublishedBefore(request.getPublishedBefore());

        return query.findAll(request.getPageable());
    }

    public interface Request {
        @NotNull @UnwrapValidatedValue(false) Optional<String> getAuthorName();
        @NotNull @UnwrapValidatedValue(false) Optional<LocalDateTime> getPublishedBefore();
        @NotNull @UnwrapValidatedValue(false) Optional<LocalDateTime> getPublishedAfter();
        @NotNull @UnwrapValidatedValue(false) Optional<String> getContains();
        @NotNull Pageable getPageable();
    }
}
