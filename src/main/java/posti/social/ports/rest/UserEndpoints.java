package posti.social.ports.rest;

import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

import posti.social.adapters.CreateUserServiceAdapter;
import posti.social.adapters.FollowUserServiceAdapter;
import posti.social.adapters.GetInboxCommandAdapter;
import posti.social.adapters.PublishMessageServiceAdapter;
import posti.social.application.api.ConstraintViolationException;
import posti.social.application.api.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.theoryinpractise.halbuilder.api.RepresentationFactory.HAL_JSON;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(path = ApiEndpoints.V1 + "/users")
public class UserEndpoints {
    @RequestMapping(method = POST)
    public ResponseEntity<String> createUser(
            @RequestBody CreateUserRequestBuilderVisitor visitor,
            @Autowired CreateUserServiceAdapter adapter)
            throws URISyntaxException, ConstraintViolationException {

        return adapter.execute(visitor)
                      .accept(new CreatedUserResponseBuilder())
                      .build(HAL_JSON);
    }

    @RequestMapping(method = POST, path = "/{userId}/messages")
    public ResponseEntity<String> publishMessage(
            @PathVariable("userId") UUID userId,
            @RequestBody PublishMessageRequestBuilderVisitor visitor,
            @Autowired PublishMessageServiceAdapter service)
            throws URISyntaxException, UserNotFoundException, ConstraintViolationException {

        visitor.setUserId(userId);

        return service.execute(visitor)
                      .accept(new CreatedMessageResponseBuilder())
                      .build(HAL_JSON);
    }

    @RequestMapping(method = GET, path = "/{userId}/inbox")
    public ResponseEntity<String> inbox(
            @PathVariable("userId") UUID uuid,
            Pageable maybePageable,
            HttpServletRequest httpRequest,
            @Autowired GetInboxCommandAdapter service
    ) throws ConstraintViolationException {
        Pageable pageable = Optional.ofNullable(maybePageable).orElse(new PageRequest(0, 20));

        return service.execute(builder -> builder
                               .withUserId(uuid)
                               .withPageable(pageable))
                .accept(new PagedMessagesResponseBuilder("inbox"))
                .build(httpRequest, HAL_JSON);
    }

    @RequestMapping(method = POST, path = "/{userId}/followers")
    public ResponseEntity<Void> follow(
            @PathVariable("userId") UUID userId,
            @RequestBody FollowUserRequestBuilderVisitor visitor,
            @Autowired FollowUserServiceAdapter service) throws UserNotFoundException, ConstraintViolationException {

        visitor.setTargetId(userId);

        service.execute(visitor);

        return noContent().build();
    }
}
