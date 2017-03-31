package posti.social.application.domain;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FindMessageQuery extends Query<Message> {
    void setAuthorName(Optional<String> username);
    void setAuthor(Optional<User> author);
    void setPublishedBefore(Optional<LocalDateTime> localDateTime);
    void setPublishedAfter(Optional<LocalDateTime> localDateTime);
    void setContains(Optional<String> text);
}
