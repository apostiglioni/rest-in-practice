package posti.social.ports.rest;

import java.time.LocalDateTime;
import java.util.UUID;

import posti.social.adapters.MessageBuilder;
import posti.social.adapters.UserBuilderVisitor;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import org.springframework.http.ResponseEntity;

public abstract class SingleMessageResponseBuilder implements MessageBuilder {
    private final MessageRepresentationBuilder messageBuilder;

    public SingleMessageResponseBuilder() {
        messageBuilder = new MessageRepresentationBuilder();
    }

    @Override
    public MessageBuilder withId(UUID id) {
        messageBuilder.withId(id);
        return this;
    }

    @Override
    public MessageBuilder withBody(String body) {
        messageBuilder.withBody(body);
        return this;
    }

    @Override
    public MessageBuilder withPublishTime(LocalDateTime publishTime) {
        messageBuilder.withPublishTime(publishTime);
        return this;
    }

    @Override
    public MessageBuilder withAuthor(UserBuilderVisitor visitor) {
        messageBuilder.withAuthor(visitor);
        return this;
    }

    public ResponseEntity<String> build(String contentType) {
        ReadableRepresentation representation = messageBuilder.build();
        return buildFor(representation, contentType);
    }

    protected abstract ResponseEntity<String> buildFor(ReadableRepresentation representation, String contentType);
}
