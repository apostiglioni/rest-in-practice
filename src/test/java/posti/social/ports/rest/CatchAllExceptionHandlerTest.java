package posti.social.ports.rest;

import posti.social.application.domain.MessageRepository;
import posti.social.application.domain.User;
import posti.social.application.domain.UserRepository;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserEndpoints.class)
public class CatchAllExceptionHandlerTest {
    @Autowired private MockMvc mockMvc;
     @MockBean private UserRepository userRepository;
     @MockBean private MessageRepository messageRepository;

    @Test
    public void shouldHandle() throws Exception {
        final String regex = "\\{\"errors\":\\[\"Internal server error: [a-f0-9]{8}(-[a-f0-9]{4}){3}-[a-f0-9]{12}\"\\]\\}";

        final String email    = "email@email.com";
        final String username = "pepe";

        final String REQUEST_BODY = new JSONObject()
                .put("username", username)
                .put("email", email)
                .toString();

        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("BOOM!"));

        final String RESOURCE_LOCATION = "/v1/users";
        mockMvc.perform(
            post(RESOURCE_LOCATION)
           .contentType(APPLICATION_JSON)
           .content(REQUEST_BODY))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andExpect(content().string(matches(regex)));
    }

    private Matcher<String> matches(String regex) {
        return new BaseMatcher<String>() {
            @Override
            public void describeTo(Description description) {
                description.appendText(regex);
            }

            @Override
            public boolean matches(Object item) {
                return ((String) item).matches(regex);
            }
        };
    }

    private JSONObject jsonForErrors(String... messages) throws JSONException {
        return new JSONObject().put("errors", new JSONArray(messages));
    }
}

