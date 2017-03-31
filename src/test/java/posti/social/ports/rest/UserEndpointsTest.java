package posti.social.ports.rest;

import java.util.List;
import java.util.Optional;
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
import posti.social.application.domain.UserRepository;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
public class UserEndpointsTest {
    @Autowired private MockMvc mockMvc;
    @MockBean  private UserRepository userRepository;
    @MockBean  private MessageRepository messageRepository;

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

    @Test
    public void shouldFollow() throws Exception {
        final User celebrity = new User("celebrity", "celebrity@mail.com");
        final User stalker = spy(new User("stalker", "stalker@mail.com"));
        final String REQUEST_BODY = new JSONObject()
                .put("followerId", stalker.getId())
                .toString();

        when(userRepository.findById(celebrity.getId())).thenReturn(Optional.of(celebrity));
        when(userRepository.findById(stalker.getId())).thenReturn(Optional.of(stalker));

        final String RESOURCE_LOCATION = format("/v1/users/%s/followers", celebrity.getId());
        mockMvc
           .perform(
               post(RESOURCE_LOCATION)
              .contentType(APPLICATION_JSON)
              .content(REQUEST_BODY))
           .andDo(print())
           .andExpect(status().isNoContent());

        verify(stalker).follow(celebrity);
    }

    private static String jsonForInbox(UUID userId, Page<Message> page) throws JSONException {
        List<JSONObject> messages = page.getContent()
                .stream()
                .map(UserEndpointsTest::jsonForMessage)
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

    private static JSONObject jsonForMessage(Message message) {
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
