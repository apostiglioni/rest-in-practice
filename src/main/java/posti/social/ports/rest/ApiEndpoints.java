package posti.social.ports.rest;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import com.theoryinpractise.halbuilder.standard.StandardRepresentationFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.theoryinpractise.halbuilder.api.RepresentationFactory.HAL_JSON;
import static com.theoryinpractise.halbuilder.api.RepresentationFactory.PRETTY_PRINT;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(path = ApiEndpoints.V1)
public class ApiEndpoints {
    public static final String V1 = "/v1";

    @RequestMapping(method = GET)
    public ResponseEntity<String> entrtypoint() {
        RepresentationFactory representationFactory = new StandardRepresentationFactory().withFlag(PRETTY_PRINT);

        Representation representation = representationFactory.newRepresentation(ApiEndpoints.V1);
        representation.withLink("user-messages", ApiEndpoints.V1 + "/users/{userId}/messages");
        representation.withLink("inbox", ApiEndpoints.V1 + "/users/{userId}/inbox");
        representation.withLink("users", ApiEndpoints.V1 + "/users");
        representation.withLink("messages", ApiEndpoints.V1 + "/messages{?authorName,publishedBefore,publishedAfter,contains,page,size}");
        representation.withLink("replies", ApiEndpoints.V1 + "/messages/{messageId}/replies");
        representation.withLink("followers", ApiEndpoints.V1 + "/users/{userId}/followers");

       return ok()
             .contentType(MediaType.valueOf(HAL_JSON))
             .body(representation.toString(HAL_JSON));
    }
}
