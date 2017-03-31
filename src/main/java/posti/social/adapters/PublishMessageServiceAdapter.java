package posti.social.adapters;

import java.util.UUID;
import javax.validation.Validator;

import posti.social.application.api.ConstraintViolationException;
import posti.social.application.api.PublishMessageService;
import posti.social.application.api.PublishMessageService.Request;
import posti.social.application.api.UserNotFoundException;
import posti.social.application.api.ValidatingSupplier;
import posti.social.application.domain.Message;

public class PublishMessageServiceAdapter {
    private final PublishMessageService service;
    private final Validator validator;

    public PublishMessageServiceAdapter(PublishMessageService service, Validator validator) {
        this.service = service;
        this.validator = validator;
    }

    public MessageBuilderVisitor execute(RequestBuilderVisitor visitor) throws ConstraintViolationException, UserNotFoundException {
        ValidatingSupplier<Request> request = visitor.accept(new RequestBuilder()).buildValidatingSupplier(validator);
        Message message = service.execute(request);
        return new MessageAdapter(message);
    }

    @FunctionalInterface
    public interface RequestBuilderVisitor {
        RequestBuilder accept(RequestBuilder builder);
    }

    public static class RequestBuilder {
        private String message;
        private UUID userId;

        public RequestBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public RequestBuilder withUserId(UUID userId) {
            this.userId = userId;
            return this;
        }

        private Request build() {
            return new Request() {
                @Override
                public String getMessage() {
                    return message;
                }

                @Override
                public UUID getUserId() {
                    return userId;
                }
            };
        }

        private ValidatingSupplier<Request> buildValidatingSupplier(Validator validator) {
            Request request = build();
            return new ValidatingSupplier<>(validator, request);
        }
    }
}
