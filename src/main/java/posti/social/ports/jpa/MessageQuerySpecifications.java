package posti.social.ports.jpa;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import posti.social.application.domain.Message;
import posti.social.application.domain.Message_;
import posti.social.application.domain.User;
import posti.social.application.domain.User_;

import static java.util.Objects.requireNonNull;

public class MessageQuerySpecifications {
    public static Specification<Message> publishedAfter(LocalDateTime when) {
        requireNonNull(when, "null date is not a valid search criteria");
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(Message_.publishTime), when);
    }

    public static Specification<Message> publishedBefore(LocalDateTime when) {
        requireNonNull(when, "null date is not a valid search criteria");
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(Message_.publishTime), when);
    }

    public static Specification<Message> authoredBy(String author) {
        requireNonNull(author, "null author is not a valid search criteria");
        return (root, query, cb) -> cb.equal(root.get(Message_.author).get(User_.username), author);
    }

    public static Specification<Message> authoredBy(UUID author) {
        requireNonNull(author, "null author id is not a valid search criteria");
        return (root, query, cb) -> cb.equal(root.get(Message_.author).get(User_.id), author);
    }

    public static Specification<Message> authoredBy(User author) {
        requireNonNull(author, "null author is not a valid search criteria");
        return (root, query, cb) -> cb.equal(root.get(Message_.author), author);
    }

    public static Specification<Message> contains(String text) {
        requireNonNull(text, "null body is not a valid search criteria");
        return (root, query, cb) -> cb.like(cb.lower(root.get(Message_.body)), "%" + text.toLowerCase() + "%");
    }
}
