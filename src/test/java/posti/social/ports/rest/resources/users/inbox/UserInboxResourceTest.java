package posti.social.ports.rest.resources.users.inbox;

import java.util.List;
import java.util.UUID;

import com.querydsl.core.types.Predicate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import posti.social.Application;
import posti.social.application.domain.Message;
import posti.social.application.domain.MessageRepository;
import posti.social.application.domain.User;
import posti.social.ports.rest.resources.users.messages.UserMessagesResourceTest;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
public class UserInboxResourceTest {
    @Autowired private MockMvc mockMvc;
    @MockBean  private MessageRepository messageRepository;

    @Test
    public void shouldGetInbox() throws Exception {
        final User user = new User("user", "user@mail.com");

        Pageable p = new PageRequest(1, 1);
        List<Message> content = asList(user.publish("Hello world"), user.publish("Here"),user.publish("Bye bye"));
        Page<Message> page = new PageImpl<>(content.subList(p.getOffset(), p.getOffset() + p.getPageSize()), p, content.size());
        when(messageRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(page);

        final String RESOURCE_LOCATION = format("/v1/users/%s/inbox?page=%d&size=%d", user.getId(), p.getPageNumber(), p.getPageSize());
        mockMvc
                .perform(
                    get(RESOURCE_LOCATION)
                    .accept("application/hal+json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonForInbox(user.getId(), page)));
    }

    private static String jsonForInbox(UUID userId, Page<Message> page) throws JSONException {
        List<JSONObject> messages = page.getContent()
                .stream()
                .map(UserMessagesResourceTest::jsonForMessage)
                .collect(toList());

        String self = format("http://localhost/v1/users/%s/inbox?page=%d&size=%d", userId, page.getNumber(), page.getSize());

        return new JSONObject().put(
                "_links", new JSONObject().put(
                     "self", new JSONObject().put(
                        "href", self))).put(
                "_embedded", new JSONObject().put(
                     "inbox", new JSONArray(messages))
        ).toString();
    }
}
