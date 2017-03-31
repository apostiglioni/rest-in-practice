package posti.social.ports.rest.resources.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import posti.social.adapters.CreateUserServiceAdapter;
import posti.social.application.api.ConstraintViolationException;
import posti.social.ports.rest.resources.Resources;

import static com.theoryinpractise.halbuilder.api.RepresentationFactory.HAL_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(path = UsersResource.PATH)
public class UsersResource {
    public static final String PATH = Resources.V1 + "/users";

    @RequestMapping(method = POST)
    public ResponseEntity<String> createUser(
            @RequestBody CreateUserRequestBuilderVisitor visitor,
            @Autowired CreateUserServiceAdapter adapter)
            throws ConstraintViolationException {

        return adapter.execute(visitor)
                      .accept(new CreatedUserResponseBuilder())
                      .build(HAL_JSON);
    }
}
