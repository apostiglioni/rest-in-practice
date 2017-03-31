package posti.social.ports.rest;

import java.net.URI;
import java.net.URISyntaxException;

import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.ResponseEntity.created;

public class CreatedMessageResponseBuilder  extends SingleMessageResponseBuilder {
    public ResponseEntity<String> buildFor(ReadableRepresentation representation, String contentType) {
        try {
            return created(new URI(representation.getResourceLink().getHref()))
                  .contentType(MediaType.valueOf(contentType))
                  .body(representation.toString(contentType));
        } catch(URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
