package posti.social.ports.rest.resources.users.inbox;

import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import posti.social.adapters.GetInboxCommandAdapter;
import posti.social.application.api.ConstraintViolationException;
import posti.social.ports.rest.resources.messages.PagedMessagesResponseBuilder;
import posti.social.ports.rest.resources.users.UsersResource;

import static com.theoryinpractise.halbuilder.api.RepresentationFactory.HAL_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(path = UserInboxResource.PATH)
public class UserInboxResource {
    public static final String PATH = UsersResource.PATH + "/{userId}/inbox";

    @RequestMapping(method = GET)
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
}
