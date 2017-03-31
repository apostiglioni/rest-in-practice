package posti.social.ports.rest;

import java.time.LocalDateTime;
import java.util.UUID;

import posti.social.adapters.MessageBuilder;
import posti.social.adapters.UserBuilderVisitor;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.standard.StandardRepresentationFactory;

import static com.theoryinpractise.halbuilder.api.RepresentationFactory.PRETTY_PRINT;
import static java.lang.String.format;

public class MessageRepresentationBuilder implements MessageBuilder {
    private final Representation representation = new StandardRepresentationFactory().withFlag(PRETTY_PRINT).newRepresentation();

    @Override
    public MessageBuilder withId(UUID id) {
        representation.withLink("self", format("/v1/messages/%s", id));
        return this;
    }

    @Override
    public MessageBuilder withBody(String body) {
        representation.withProperty("body", body);
        return this;
    }

    @Override
    public MessageBuilder withPublishTime(LocalDateTime publishTime) {
        representation.withProperty("publishTime", publishTime.toString());
        return this;
    }

    @Override
    public MessageBuilder withAuthor(UserBuilderVisitor visitor) {
        representation.withLink("author", visitor.accept(new UserRepresentationBuilder()).build().getResourceLink().getHref());
        return this;
    }

    public ReadableRepresentation build() {
        return representation;
    }
}
