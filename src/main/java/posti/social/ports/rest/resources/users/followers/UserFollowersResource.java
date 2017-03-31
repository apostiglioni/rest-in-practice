package posti.social.ports.rest.resources.users.followers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import posti.social.adapters.FollowUserServiceAdapter;
import posti.social.application.api.ConstraintViolationException;
import posti.social.application.api.UserNotFoundException;
import posti.social.ports.rest.resources.users.UsersResource;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(path = UserFollowersResource.PATH)
public class UserFollowersResource {
    public static final String PATH = UsersResource.PATH + "/{userId}/followers";

    @RequestMapping(method = POST)
    public ResponseEntity<Void> follow(
            @PathVariable("userId") UUID userId,
            @RequestBody AddFollowerRequestBuilderVisitor visitor,
            @Autowired FollowUserServiceAdapter service) throws UserNotFoundException, ConstraintViolationException {

        visitor.setTargetId(userId);

        service.execute(visitor);

        return noContent().build();
    }

}
