package posti.social.ports.rest;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.standard.StandardRepresentationFactory;
import posti.social.adapters.MessageBuilderVisitor;
import posti.social.adapters.UserBuilder;

import static com.theoryinpractise.halbuilder.api.RepresentationFactory.PRETTY_PRINT;
import static java.lang.String.format;

public class UserRepresentationBuilder implements UserBuilder {
    private final Representation representation = new StandardRepresentationFactory().withFlag(PRETTY_PRINT).newRepresentation();

    @Override
    public UserBuilder withId(UUID id) {
        String self = format("/v1/users/%s", id);
        representation.withLink("self", self);
        representation.withLink("inbox", format("%s/inbox", self));
        representation.withLink("followers", format("%s/followers", self));
        representation.withLink("user-messages", format("%s/messages", self));

        return this;
    }

    @Override
    public UserBuilder withUsername(String username) {
        representation.withProperty("username", username);
        return this;
    }

    @Override
    public UserBuilder withEmail(String email) {
        representation.withProperty("email", email);
        return this;
    }

    @Override
    public UserBuilder withMessage(MessageBuilderVisitor visitor) {
        return this;
    }

    public ReadableRepresentation build() {
        return representation;
    }
}
