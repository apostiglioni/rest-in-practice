package posti.social.application.domain;

import java.util.Random;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {
    @Test
    public void shouldPublish() throws InterruptedException {
        String firstMsg = "First message";
        String secondMsg = "Second message";

        User user = new User("user", "mail@mail.com");

        Message message1 = user.publish(firstMsg);
        assertEquals(user, message1.getAuthor());
        assertEquals(firstMsg, message1.getBody());
        assertEquals(user.getMessages().first(), message1);

        Thread.sleep(1);    // Prevent publish time conflicts

        Message message2 = user.publish(secondMsg);
        assertEquals(user, message2.getAuthor());
        assertEquals(secondMsg, message2.getBody());
        assertEquals(user.getMessages().first(), message2);
    }

    @Test(expected = MessageTooLongException.class)
    public void shouldRejectLongMessages()  {
        User user = new User("user", "mail@mail.com");
        user.publish(generateLongMessage(141));
    }

    private String generateLongMessage(int length) {
        final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        StringBuffer randStr = new StringBuffer();
        for(int i=0; i < length; i++){
            int number = getRandomNumber(CHAR_LIST.length());
            char ch = CHAR_LIST.charAt(number);
            randStr.append(ch);
        }

        return randStr.toString();
    }


    private int getRandomNumber(int max) {
        int randomInt = 0;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(max);
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }
}
