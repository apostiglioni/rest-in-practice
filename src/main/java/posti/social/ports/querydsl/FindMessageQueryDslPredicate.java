package posti.social.ports.querydsl;

import java.time.LocalDateTime;
import java.util.Optional;

import posti.social.application.domain.FindMessageQuery;
import posti.social.application.domain.Message;
import posti.social.application.domain.MessageRepository;
import posti.social.application.domain.User;

public class FindMessageQueryDslPredicate extends ComposableQueryDslPredicate<Message> implements FindMessageQuery {
    public FindMessageQueryDslPredicate(MessageRepository repository) {
        super(repository);
    }

    @Override
    public void setAuthorName(Optional<String> author) {
        super.addFilter(author.map(MessageQueryFilters::authoredBy));
    }

    @Override
    public void setAuthor(Optional<User> author) {
        super.addFilter(author.map(MessageQueryFilters::authoredBy));
    }

    @Override
    public void setPublishedBefore(Optional<LocalDateTime> localDateTime) {
        super.addFilter(localDateTime.map(MessageQueryFilters::publishedBefore));
    }

    @Override
    public void setPublishedAfter(Optional<LocalDateTime> localDateTime) {
        super.addFilter(localDateTime.map(MessageQueryFilters::publishedAfter));
    }

    public void setContains(Optional<String> text) {
        super.addFilter(text.map(MessageQueryFilters::contains));
    }
}
