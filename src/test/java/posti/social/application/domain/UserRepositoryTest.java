package posti.social.application.domain;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
@ContextConfiguration(classes = UserRepositoryTest.class)
public class UserRepositoryTest {
    @Autowired
    private UserRepository repository;

    @Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
    public void shouldRejectDuplicateEmails() {
        repository.save(new User("user1", "email1@email.com"));
        repository.save(new User("user2", "email1@email.com"));
        repository.flush();
    }

    @Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
    public void shouldRejectDuplicateUsernames() {
        repository.save(new User("user", "email1@email.com"));
        repository.save(new User("user", "email2@email.com"));
        repository.flush();
    }

    @Test
    public void shouldCascadeMessages() throws MessageTooLongException {
        User user = new User("username", "email2@email.com");
        Message message = user.publish("Hello world!");

        repository.save(user);
        repository.flush();

        Optional<User> maybeUser = repository.findById(user.getId());
        assertTrue(maybeUser.isPresent());

        User found = maybeUser.get();
        assertEquals(user, found);

        assertTrue(found.getMessages().contains(message));
    }

    @Test
    public void shouldSaveFollowers() {
        User user1 = new User("user1", "email1@email.com");
        User user2 = new User("user2", "email2@email.com");

        user1.follow(user2);

        repository.save(user1);
        repository.save(user2);
        repository.flush();

        Optional<User> maybeUser = repository.findById(user2.getId());
        assertThat(maybeUser.get().getFollowers(), contains(user1));

        user1.unfollow(user2);

        repository.save(user2);
        repository.flush();

        maybeUser = repository.findById(user2.getId());
        assertTrue(maybeUser.get().getFollowers().isEmpty());
    }

    @Test
    public void shouldSaveReplies() {
        User user1 = new User("user1", "email1@email.com");
        User user2 = new User("user2", "email2@email.com");

        Message message1 = user1.publish("Hello world");
        Message message2 = user2.reply("Bye bye", message1);

        repository.save(user2);
        repository.save(user1);
        repository.flush();

        Optional<User> maybeUser1 = repository.findById(user1.getId());
        assertThat(maybeUser1.get().getMessages().first().getReplies(), contains(message2));

        Optional<User> maybeUser2 = repository.findById(user2.getId());
        assertThat(maybeUser2.get().getMessages(), contains(message2));
    }
}
