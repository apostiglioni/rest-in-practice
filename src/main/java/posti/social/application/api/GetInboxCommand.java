package posti.social.application.api;

import java.util.UUID;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import posti.social.application.domain.InboxQuery;
import posti.social.application.domain.Message;

public class GetInboxCommand {
    private final InboxQuery query;

    public GetInboxCommand(InboxQuery query) {
        this.query = query;
    }

    public Page<Message> execute(ValidatingSupplier<Request> supplier) throws ConstraintViolationException {
        Request request = supplier.getValidOrFail();

        query.setUserId(request.getUserId());

        Page<Message> messages = query.findAll(request.getPageable());
        return messages;
    }

    public interface Request {
        @NotNull UUID getUserId();
        @NotNull Pageable getPageable();
    }
}
