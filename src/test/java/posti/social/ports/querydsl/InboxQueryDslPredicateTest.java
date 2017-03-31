package posti.social.ports.querydsl;

import java.util.ArrayList;
import javax.transaction.Transactional;

import posti.social.application.domain.InboxQuery;
import posti.social.application.domain.JpaTestConfiguration;
import posti.social.application.domain.Message;
import posti.social.application.domain.MessageRepository;
import posti.social.application.domain.User;
import posti.social.application.domain.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static java.lang.Thread.sleep;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
@ContextConfiguration(classes = JpaTestConfiguration.class)
public class InboxQueryDslPredicateTest {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findAll() throws Exception {
        User follower = new User("username", "email@email.com");
        User celebrity1 = new User("celebrity1", "celebrity1@email.com");
        User celebrity2 = new User("celebrity2", "celebrity2@email.com");

        Message message1 = celebrity1.publish("I'm celebrity 1");
        sleep(100);

        Message greeting = celebrity1.publish("Hello fans");
        sleep(100);

        Message message2 = celebrity2.publish("I'm celebrity 2");
        sleep(100);

        Message message = follower.publish("I'm a follower");
        sleep(100);

        follower.follow(celebrity1);

        userRepository.save(follower);
        userRepository.save(celebrity1);
        userRepository.save(celebrity2);

        InboxQuery inboxQuery = new InboxQueryDslPredicate(messageRepository);
        inboxQuery.setUserId(follower.getId());

        ArrayList<Message> messages = new ArrayList<>();
        inboxQuery.findAll().forEach(messages::add);

        assertEquals(asList(message, greeting, message1), messages);

        Page<Message> page1 = inboxQuery.findAll(new PageRequest(0, 2));

        assertEquals(3, page1.getTotalElements());
        assertEquals(2, page1.getTotalPages());

        assertEquals(asList(message, greeting), page1.getContent());

        Page<Message> page2 = inboxQuery.findAll(new PageRequest(1, 2));
        assertEquals(asList(message1), page2.getContent());
    }
}
