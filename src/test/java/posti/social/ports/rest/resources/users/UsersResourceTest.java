package posti.social.ports.rest.resources.users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import posti.social.Application;
import posti.social.application.domain.User;
import posti.social.application.domain.UserRepository;

import static java.lang.String.format;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
public class UsersResourceTest {
    @Autowired private MockMvc mockMvc;
    @MockBean  private UserRepository userRepository;

    @Test
    public void shouldCreate() throws Exception {
        final User dummyUser = new User("pepe", "email@email.com");

        final String REQUEST_BODY = new JSONObject()
                                        .put("username", dummyUser.getUsername())
                                        .put("email", dummyUser.getEmail())
                                        .toString();

        when(userRepository.save(any(User.class))).thenReturn(dummyUser);

        final String RESOURCE_LOCATION = "/v1/users";
        mockMvc
                .perform(
                    post(RESOURCE_LOCATION)
                   .contentType(APPLICATION_JSON)
                   .content(REQUEST_BODY))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", format("/v1/users/%s", dummyUser.getId())))
                .andExpect(content().json(jsonForUser(dummyUser).toString()));
    }

    @Test
    public void shouldReject() throws Exception {
        final String[] expectedErrors = { "User name must not be null or empty", "Email must not be null or empty" };

        final String REQUEST_BODY = new JSONObject()
                .put("email","")
                .toString();

        when(userRepository.save(any(User.class))).then(returnsFirstArg());

        final String RESOURCE_LOCATION = "/v1/users";
        mockMvc
           .perform(
               post(RESOURCE_LOCATION)
              .contentType(APPLICATION_JSON)
              .content(REQUEST_BODY))
           .andDo(print())
           .andExpect(status().isBadRequest())
           .andExpect(content().json(jsonForErrors(expectedErrors).toString()));
    }

    private JSONObject jsonForErrors(String... messages) throws JSONException {
        return new JSONObject().put("errors", new JSONArray(messages));
    }

    public static JSONObject jsonForUser(User user) {
        try {
            return new JSONObject().put(
                    "email", user.getEmail()).put(
                    "username", user.getUsername()).put(
                    "_links", new JSONObject().put(
                       "self", new JSONObject().put(
                          "href", format("/v1/users/%s", user.getId()))).put(
                       "user-messages", new JSONArray().put(new JSONObject().put(
                          "href", format("/v1/users/%s/messages", user.getId())))).put(
                       "inbox", new JSONArray().put(new JSONObject().put(
                          "href", format("/v1/users/%s/inbox", user.getId())))));
        } catch(JSONException e) {
           throw new RuntimeException(e);
        }
    }
}
