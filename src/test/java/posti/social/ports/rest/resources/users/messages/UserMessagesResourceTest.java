package posti.social.ports.rest.resources.users.messages;

import java.util.Optional;

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
import posti.social.application.domain.Message;
import posti.social.application.domain.MessageRepository;
import posti.social.application.domain.User;
import posti.social.application.domain.UserRepository;

import static java.lang.String.format;
import static org.mockito.Mockito.spy;
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
public class UserMessagesResourceTest {
    @Autowired private MockMvc mockMvc;
    @MockBean  private UserRepository userRepository;
    @MockBean  private MessageRepository messageRepository;

    @Test
    public void shouldPublish() throws Exception {
        final User user = spy(new User("pepe", "pepe@mail.com"));
        final Message message = user.publish("hello world");
        final String REQUEST_BODY = new JSONObject()
                .put("message", message.getBody())
                .toString();

        when(user.publish(message.getBody())).thenReturn(message);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));


        final String RESOURCE_LOCATION = format("/v1/users/%s/messages", user.getId());
        mockMvc
           .perform(
               post(RESOURCE_LOCATION)
              .contentType(APPLICATION_JSON)
              .content(REQUEST_BODY))
           .andDo(print())
           .andExpect(status().isCreated())
           .andExpect(content().json(jsonForMessage(message).toString()))
           .andExpect(header().string("Location", format("/v1/messages/%s", message.getId())));
    }

    public static JSONObject jsonForMessage(Message message) {
        try {
            return new JSONObject().put(
                    "body", message.getBody()).put(
                    "_links", new JSONObject().put(
                         "self", new JSONObject().put(
                            "href", format("/v1/messages/%s", message.getId()))).put(
                         "author", new JSONArray().put(new JSONObject().put(
                            "href", format("/v1/users/%s", message.getAuthor().getId())))));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
