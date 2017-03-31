package posti.social.adapters;

import java.util.UUID;
import javax.validation.Validator;

import posti.social.application.api.ConstraintViolationException;
import posti.social.application.domain.Message;
import posti.social.application.api.ValidatingSupplier;
import posti.social.application.api.MessageNotFoundException;
import posti.social.application.api.ReplyMessageService;
import posti.social.application.api.ReplyMessageService.Request;
import posti.social.application.api.UserNotFoundException;

public class ReplyMessageServiceAdapter {
    private final ReplyMessageService command;
    private final Validator validator;

    public ReplyMessageServiceAdapter(ReplyMessageService command, Validator validator) {
        this.command = command;
        this.validator = validator;
    }

    public MessageBuilderVisitor execute(RequestBuilderVisitor visitor) throws ConstraintViolationException, MessageNotFoundException, UserNotFoundException {
        ValidatingSupplier<Request> request = visitor.accept(new RequestBuilder()).buildValidatingSupplier(validator);
        Message message = command.execute(request);

        return new MessageAdapter(message);
    }

    @FunctionalInterface
    public interface RequestBuilderVisitor {
        RequestBuilder accept(RequestBuilder builder);
    }

    public static class RequestBuilder {
        private UUID authorId;
        private UUID messageId;
        private String content;

        public RequestBuilder withAuthorId(UUID authorId) {
            this.authorId = authorId;
            return this;
        }

        public RequestBuilder withMessageId(UUID messageId) {
            this.messageId = messageId;
            return this;
        }

        public RequestBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        private ValidatingSupplier<Request> buildValidatingSupplier(Validator validator) {
            Request request = build();
            return new ValidatingSupplier<>(validator, request);
        }

        private Request build() {
            return new Request() {
                @Override
                public UUID getAuthorId() {
                    return authorId;
                }

                @Override
                public UUID getMessageId() {
                    return messageId;
                }

                @Override
                public String getContent() {
                    return content;
                }
            };
        }
    }
}
