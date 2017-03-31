package posti.social.adapters;

import javax.validation.Validator;

import posti.social.application.api.ConstraintViolationException;
import posti.social.application.domain.User;
import posti.social.application.api.ValidatingSupplier;
import posti.social.application.api.CreateUserService;

public class CreateUserServiceAdapter {
    private final CreateUserService service;
    private final Validator validator;

    public CreateUserServiceAdapter(CreateUserService service, Validator validator) {
        this.service = service;
        this.validator = validator;
    }

    public UserBuilderVisitor execute(RequestBuilderVisitor visitor) throws ConstraintViolationException {
        ValidatingSupplier<CreateUserService.Request> request = visitor.accept(new RequestBuilder()).buildValidatingSupplier(validator);
        User user = service.execute(request);
        return new UserAdapter(user);
    }

    @FunctionalInterface
    public interface RequestBuilderVisitor {
        RequestBuilder accept(RequestBuilder builder);
    }

    public static class RequestBuilder {
        private String email;
        private String username;

        public RequestBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public RequestBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        private CreateUserService.Request build() {
            return new CreateUserService.Request() {
                @Override
                public String getUsername() {
                    return username;
                }

                @Override
                public String getEmail() {
                    return email;
                }
            };
        }

        private ValidatingSupplier<CreateUserService.Request> buildValidatingSupplier(Validator validator) {
            CreateUserService.Request request = build();
            return new ValidatingSupplier<>(validator, request);
        }
    }
}
