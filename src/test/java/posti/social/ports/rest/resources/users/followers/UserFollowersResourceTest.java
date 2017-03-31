package posti.social.ports.rest.resources.users.followers;

import java.util.Optional;

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
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
public class UserFollowersResourceTest {
    @Autowired private MockMvc mockMvc;
    @MockBean  private UserRepository userRepository;

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
}
