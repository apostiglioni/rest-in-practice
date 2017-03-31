package posti.social.adapters;

import java.util.UUID;
import javax.validation.Validator;

import posti.social.application.api.ConstraintViolationException;
import posti.social.application.api.FollowUserService;
import posti.social.application.api.FollowUserService.Request;
import posti.social.application.api.UserNotFoundException;
import posti.social.application.api.ValidatingSupplier;

public class FollowUserServiceAdapter {
    private final FollowUserService service;
    private final Validator validator;

    public FollowUserServiceAdapter(FollowUserService service, Validator validator) {
        this.service = service;
        this.validator = validator;
    }

    public void execute(RequestBuilderVisitor visitor) throws ConstraintViolationException, UserNotFoundException {
        ValidatingSupplier<Request> request = visitor.accept(new RequestBuilder()).buildValidatingSupplier(validator);
        service.execute(request);
    }

    @FunctionalInterface
    public interface RequestBuilderVisitor {
        RequestBuilder accept(RequestBuilder builder);
    }

    public static class RequestBuilder {
        private UUID followerId;
        private UUID targetId;

        public RequestBuilder withTargetId(UUID followerId) {
            this.targetId = followerId;
            return this;
        }

        public RequestBuilder withFollowerId(UUID followerId) {
            this.followerId = followerId;
            return this;
        }

        private Request build() {
            return new Request() {
                @Override
                public UUID getFollowerId() {
                    return followerId;
                }

                @Override
                public UUID getTargetId() {
                    return targetId;
                }
            };
        }

        private ValidatingSupplier<Request> buildValidatingSupplier(Validator validator) {
            Request request = build();
            return new ValidatingSupplier<>(validator, request);
        }
    }
}
