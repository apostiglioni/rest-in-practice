package posti.social.adapters;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.validation.Validator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import posti.social.application.api.ConstraintViolationException;
import posti.social.application.api.FindMessageCommand;
import posti.social.application.api.FindMessageCommand.Request;
import posti.social.application.api.ValidatingSupplier;
import posti.social.application.domain.Message;

public class FindMessageCommandAdapter {
    private final FindMessageCommand command;
    private final Validator validator;

    public FindMessageCommandAdapter(FindMessageCommand command, Validator validator) {
        this.command = command;
        this.validator = validator;
    }

    public PageBuilderVisitor<MessageBuilderVisitor> execute(RequestBuilderVisitor visitor) throws ConstraintViolationException {
        ValidatingSupplier<Request> request = visitor.accept(new RequestBuilder()).buildValidatingSupplier(validator);
        Page<Message> page = command.execute(request);

        return new PageAdapter<>(page).map(MessageAdapter::new);
    }

    @FunctionalInterface
    public interface RequestBuilderVisitor {
        RequestBuilder accept(RequestBuilder builder);
    }

    public static class RequestBuilder {
        private Optional<String> authorName;
        private Optional<String> contains;
        private Optional<LocalDateTime> publishedBefore;
        private Optional<LocalDateTime> publishedAfter;
        private Pageable pageable;

        public RequestBuilder withAuthorName(Optional<String> authorName) {
            this.authorName = authorName;
            return this;
        }

        public RequestBuilder withContains(Optional<String> contains) {
            this.contains = contains;
            return this;
        }

        public RequestBuilder withPublishedBefore(Optional<LocalDateTime> publishedBefore) {
            this.publishedBefore = publishedBefore;
            return this;
        }

        public RequestBuilder withPublishedAfter(Optional<LocalDateTime> publishedAfter) {
            this.publishedAfter = publishedAfter;
            return this;
        }

        public RequestBuilder withPageable(Pageable pageable) {
            this.pageable = pageable;
            return this;
        }

        private Request build() {
            return new Request() {
                @Override
                public Optional<String> getAuthorName() {
                    return authorName;
                }

                @Override
                public Optional<LocalDateTime> getPublishedBefore() {
                    return publishedBefore;
                }

                @Override
                public Optional<LocalDateTime> getPublishedAfter() {
                    return publishedAfter;
                }

                @Override
                public Optional<String> getContains() {
                    return contains;
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
