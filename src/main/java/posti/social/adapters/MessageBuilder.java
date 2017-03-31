package posti.social.adapters;

import java.time.LocalDateTime;
import java.util.UUID;

public interface MessageBuilder {
    <T extends MessageBuilder> T withId(UUID id);
    <T extends MessageBuilder> T withBody(String body);
    <T extends MessageBuilder> T withPublishTime(LocalDateTime publishTime);
    <T extends MessageBuilder> T withAuthor(UserBuilderVisitor adapt);
}
