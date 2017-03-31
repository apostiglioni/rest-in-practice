package posti.social.ports.rest;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

import posti.social.adapters.FindMessageCommandAdapter;
import posti.social.adapters.GetMessageByIdServiceAdapter;
import posti.social.adapters.ReplyMessageServiceAdapter;
import posti.social.application.api.ConstraintViolationException;
import posti.social.application.api.MessageNotFoundException;
import posti.social.application.api.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.theoryinpractise.halbuilder.api.RepresentationFactory.HAL_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(path = ApiEndpoints.V1 + "/messages")
public class MessageEndpoints {
    @RequestMapping(method = GET, path = "/{messageId}")
    public ResponseEntity<String> get(
            @PathVariable("messageId") UUID messageId,
            @Autowired GetMessageByIdServiceAdapter service)
            throws MessageNotFoundException, ConstraintViolationException {

        return service.execute(builder -> builder.withMessageId(messageId))
                      .map(visitor -> visitor.accept(new SuccessMessageResponseBuilder()))
                      .map(b -> b.build(HAL_JSON))
                      .orElseThrow(MessageNotFoundException::new);
    }

    @RequestMapping(method = GET)
    public ResponseEntity<String> getMessages(
            @RequestParam(required = false, value = "authorName") String authorName,
            @RequestParam(required = false, value = "publishedBefore") LocalDateTime publishedBefore,
            @RequestParam(required = false, value = "publishedAfter") LocalDateTime publishedAfter,
            @RequestParam(required = false, value = "contains") String contains,
            Pageable maybePageable,
            HttpServletRequest httpRequest,
            @Autowired FindMessageCommandAdapter command) throws ConstraintViolationException {
        Pageable pageable = Optional.ofNullable(maybePageable).orElse(new PageRequest(0, 20));

        return command.execute(builder -> builder
                               .withPageable(pageable)
                               .withAuthorName(Optional.ofNullable(authorName))
                               .withPublishedBefore(Optional.ofNullable(publishedBefore))
                               .withPublishedAfter(Optional.ofNullable(publishedAfter))
                               .withContains(Optional.ofNullable(contains)))
                      .accept(new PagedMessagesResponseBuilder("messages"))
                      .build(httpRequest, HAL_JSON);    //TODO: Should not pass the entire request
    }

    @RequestMapping(path = "/{messageId}/replies", method = POST)
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
