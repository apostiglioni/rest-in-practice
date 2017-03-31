package posti.social.application.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import static java.util.Objects.requireNonNull;

@Entity
public class User extends PersistentObject {
    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(cascade= CascadeType.ALL, mappedBy="author")
    @OrderBy("publishTime DESC")
    private SortedSet<Message> messages;

    @ManyToMany
    private Set<User> followers;

    private User() { /* required by JPA */}

    public User(String username, String email) {
        this.followers = new HashSet<>();
        this.messages = new TreeSet<>();
        this.username = requireNonNull(username, "username must be not null");
        this.email = requireNonNull(email, "email must be not null");
    }

    public Message publish(String body) {
        requireNonNull("Message must not be null");

        if (body.length() > 140) {
            throw new MessageTooLongException(body);
        }

        Message message = new Message(this, body, LocalDateTime.now());
        messages.add(message);

        return message;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public SortedSet<Message> getMessages() {
        return Collections.unmodifiableSortedSet(messages);
    }

    public void follow(User user) {
        requireNonNull(user, "Can't follow a null user");
        user.addFollower(this);
    }

    private void addFollower(User user) {
        requireNonNull(user, "Can't add null as a follower");
        followers.add(user);
    }

    public void unfollow(User user) {
        requireNonNull("Can't unfollow a null user");
        user.removeFollower(this);
    }

    private void removeFollower(User user) {
        followers.remove(user);
    }

    public Set<User> getFollowers() {
        return Collections.unmodifiableSet(followers);
    }

    public Message reply(String content, Message message) {
        Message reply = publish(content);
        message.addReply(reply);

        return reply;
    }

    public void unpublish(Message message) {
        messages.remove(message);
    }
}
