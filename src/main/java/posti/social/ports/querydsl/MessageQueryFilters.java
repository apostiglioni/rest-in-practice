package posti.social.ports.querydsl;

import java.time.LocalDateTime;
import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;
import posti.social.application.domain.QMessage;
import posti.social.application.domain.User;

import static java.util.Objects.requireNonNull;

public class MessageQueryFilters {
    public static BooleanExpression publishedAfter(LocalDateTime when) {
        requireNonNull(when, "null date is not a valid search criteria");
        return QMessage.message.publishTime.after(when);
    }

    public static BooleanExpression publishedBefore(LocalDateTime when) {
        requireNonNull(when, "null date is not a valid search criteria");
        return QMessage.message.publishTime.before(when);
    }

    public static BooleanExpression authoredBy(String author) {
        requireNonNull(author, "null author id is not a valid search criteria");
        return QMessage.message.author.username.eq(author);
    }

    public static BooleanExpression authoredBy(UUID author) {
        requireNonNull(author, "null author id is not a valid search criteria");
        return QMessage.message.author.id.eq(author);
    }

    public static BooleanExpression authoredBy(User author) {
        requireNonNull(author, "null author is not a valid search criteria");
        return QMessage.message.author.eq(author);
    }

    public static BooleanExpression contains(String text) {
        requireNonNull(text, "null text is not a valid search criteria");
        return QMessage.message.body.toLowerCase().like("%" + text.toLowerCase() + "%");
    }
}
