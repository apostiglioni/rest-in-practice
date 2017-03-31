package posti.social.ports.rest.resources.messages;

import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.ResponseEntity.ok;

public class SuccessMessageResponseBuilder extends SingleMessageResponseBuilder {
    @Override
    protected ResponseEntity<String> buildFor(ReadableRepresentation representation, String contentType) {
        return ok()
              .contentType(MediaType.valueOf(contentType))
              .body(representation.toString(contentType));
    }
}
