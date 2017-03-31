package posti.social.ports.rest.resources.messages.replies;

import java.util.Optional;
import java.util.UUID;

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
import static java.lang.String.valueOf;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static posti.social.ports.rest.resources.messages.MessageEndpointsTest.jsonForMessage;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
public class MessageRepliesResourceTest {
    @Autowired private MockMvc mockMvc;
    @MockBean  private MessageRepository repository;
    @MockBean  private UserRepository userRepository;

    @Test
    public void shouldReply() throws Exception {
        final User user = spy(new User("pepe", "email@email.com"));
        final Message message = user.publish("m1");
        final Message reply = user.publish("This is my reply");

        when(user.reply(reply.getBody(), message)).thenReturn(reply);
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(message));

        final String RESOURCE_LOCATION = format("/v1/messages/%s/replies", message.getId());

        mockMvc
                .perform(
                    post(RESOURCE_LOCATION)
                       .contentType(APPLICATION_JSON)
                       .content(new JSONObject().put(
                           "content", reply.getBody()).put(
                           "authorId", user.getId())
                           .toString()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(valueOf(jsonForMessage(reply))));
    }
}
