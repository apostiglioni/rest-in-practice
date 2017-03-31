package posti.social.ports.rest.resources.messages.replies;

import java.net.URISyntaxException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import posti.social.adapters.ReplyMessageServiceAdapter;
import posti.social.application.api.ConstraintViolationException;
import posti.social.application.api.MessageNotFoundException;
import posti.social.application.api.UserNotFoundException;
import posti.social.ports.rest.resources.messages.CreatedMessageResponseBuilder;
import posti.social.ports.rest.resources.messages.MessagesResource;

import static com.theoryinpractise.halbuilder.api.RepresentationFactory.HAL_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(path = MessageRepliesResource.PATH)
public class MessageRepliesResource {
    public static final String PATH = MessagesResource.PATH + "/{messageId}/replies";

    @RequestMapping(method = POST)
    public ResponseEntity<String> reply(
            @PathVariable UUID messageId,
            @RequestBody ReplyMessageRequestBuilderVisitor visitor,
            @Autowired ReplyMessageServiceAdapter service)
            throws MessageNotFoundException, UserNotFoundException, URISyntaxException, ConstraintViolationException {

        visitor.setMessageId(messageId);

        return service.execute(visitor)
                .accept(new CreatedMessageResponseBuilder())
                .build(HAL_JSON);
    }
}
