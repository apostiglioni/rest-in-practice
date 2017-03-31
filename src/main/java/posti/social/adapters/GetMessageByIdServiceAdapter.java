package posti.social.adapters;

import java.util.Optional;
import java.util.UUID;
import javax.validation.Validator;

import posti.social.application.domain.Message;
import posti.social.application.api.ValidatingSupplier;
import posti.social.application.api.ConstraintViolationException;
import posti.social.application.api.GetMessageByIdService;

public class GetMessageByIdServiceAdapter {
    private final GetMessageByIdService command;
    private final Validator validator;

    public GetMessageByIdServiceAdapter(GetMessageByIdService command, Validator validator) {
        this.command = command;
        this.validator = validator;
    }

    public Optional<MessageBuilderVisitor> execute(RequestBuilderVisitor visitor) throws ConstraintViolationException {
        ValidatingSupplier<GetMessageByIdService.Request> request = visitor.accept(new RequestBuilder()).buildValidatingSupplier(validator);
        Optional<Message> optional = command.execute(request);

        return optional.map(MessageAdapter::new);
    }

    @FunctionalInterface
    public interface RequestBuilderVisitor {
        RequestBuilder accept(RequestBuilder builder);
    }

    public static class RequestBuilder {
        private UUID messageId;

        public RequestBuilder withMessageId(UUID messageId) {
            this.messageId = messageId;
            return this;
        }

        private GetMessageByIdService.Request build() {
            return () -> messageId;
        }

        private ValidatingSupplier<GetMessageByIdService.Request> buildValidatingSupplier(Validator validator) {
            GetMessageByIdService.Request request = build();
            return new ValidatingSupplier<>(validator, request);
        }
    }
}
