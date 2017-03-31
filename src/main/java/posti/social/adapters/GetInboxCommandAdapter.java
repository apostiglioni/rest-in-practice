package posti.social.adapters;

import java.util.UUID;
import javax.validation.Validator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import posti.social.application.api.ConstraintViolationException;
import posti.social.application.api.GetInboxCommand;
import posti.social.application.api.GetInboxCommand.Request;
import posti.social.application.api.ValidatingSupplier;
import posti.social.application.domain.Message;

public class GetInboxCommandAdapter {
    private final GetInboxCommand command;
    private final Validator validator;

    public GetInboxCommandAdapter(GetInboxCommand command, Validator validator) {
        this.command = command;
        this.validator = validator;
    }

    public PageBuilderVisitor<MessageBuilderVisitor> execute(RequestBuilderVisitor visitor) throws ConstraintViolationException {
        ValidatingSupplier<Request> request = visitor.accept(new RequestBuilder()).buildValidatingSupplier(validator);
        Page<Message> inbox = command.execute(request);
        return new PageAdapter<>(inbox).map(MessageAdapter::new);
    }

    @FunctionalInterface
    public interface RequestBuilderVisitor {
        RequestBuilder accept(RequestBuilder builder);
    }

    public static class RequestBuilder {
        private Pageable pageable;
        private UUID userId;

        public RequestBuilder withPageable(Pageable pageable) {
            this.pageable = pageable;
            return this;
        }

        public RequestBuilder withUserId(UUID userId) {
            this.userId = userId;
            return this;
        }

        private Request build() {
            return new Request() {
                @Override
                public UUID getUserId() {
                    return userId;
                }

                @Override
                public Pageable getPageable() {
                    return pageable;
                }
            };
        }

        private ValidatingSupplier<Request> buildValidatingSupplier(Validator validator) {
            Request request = build();
            return new ValidatingSupplier<>(validator, request);
        }
    }
}
