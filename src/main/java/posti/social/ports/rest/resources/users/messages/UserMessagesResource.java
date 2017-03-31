package posti.social.ports.rest.resources.users.messages;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import posti.social.adapters.PublishMessageServiceAdapter;
import posti.social.application.api.ConstraintViolationException;
import posti.social.application.api.UserNotFoundException;
import posti.social.ports.rest.resources.messages.CreatedMessageResponseBuilder;
import posti.social.ports.rest.resources.users.UsersResource;

import static com.theoryinpractise.halbuilder.api.RepresentationFactory.HAL_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(path = UserMessagesResource.PATH)
public class UserMessagesResource {
    public static final String PATH = UsersResource.PATH + "/{userId}/messages";

    @RequestMapping(method = POST)
    public ResponseEntity<String> publishMessage(
            @PathVariable("userId") UUID userId,
            @RequestBody PublishMessageRequestBuilderVisitor visitor,
            @Autowired PublishMessageServiceAdapter service)
            throws UserNotFoundException, ConstraintViolationException {

        visitor.setUserId(userId);

        return service.execute(visitor)
                .accept(new CreatedMessageResponseBuilder())
                .build(HAL_JSON);
    }

}
