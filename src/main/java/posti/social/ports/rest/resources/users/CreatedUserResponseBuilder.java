package posti.social.ports.rest.resources.users;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import org.springframework.http.ResponseEntity;
import posti.social.adapters.MessageBuilderVisitor;
import posti.social.adapters.UserBuilder;

import static org.springframework.http.ResponseEntity.created;

public class CreatedUserResponseBuilder implements UserBuilder {
    private final UserRepresentationBuilder userRepresentationBuilder;

    public CreatedUserResponseBuilder() {
        userRepresentationBuilder = new UserRepresentationBuilder();
    }

    @Override
    public CreatedUserResponseBuilder withId(UUID id) {
        userRepresentationBuilder.withId(id);
        return this;
    }

    @Override
    public CreatedUserResponseBuilder withUsername(String username) {
        userRepresentationBuilder.withUsername(username);
        return this;
    }

    @Override
    public CreatedUserResponseBuilder withEmail(String email) {
        userRepresentationBuilder.withEmail(email);
        return this;
    }

    @Override
    public CreatedUserResponseBuilder withMessage(MessageBuilderVisitor messageBuilder) {
        userRepresentationBuilder.withMessage(messageBuilder);
        return this;
    }

    public ResponseEntity<String> build(String contentType) {
        try {
            ReadableRepresentation representation = userRepresentationBuilder.build();
            return created(new URI(representation.getResourceLink().getHref())).body(representation.toString(contentType));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
