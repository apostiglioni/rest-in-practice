package posti.social.application.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import static java.util.Objects.requireNonNull;

@Entity
public class Message extends PersistentObject implements Comparable<Message> {
    @ManyToOne(optional = false)
    private User author;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private LocalDateTime publishTime;

    private Message() { /* required by JPA */ }

    @OneToMany
    private List<Message> replies;

    Message(User author, String body, LocalDateTime publishTime) {
        this.replies = new ArrayList<>();
        this.author = requireNonNull(author, "author must be not null");
        this.body = requireNonNull(body, "body must be not null");
        this.publishTime = requireNonNull(publishTime, "publishTime must be not null");
    }

    public User getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    @Override
    public int compareTo(Message o) {
        return -1 * publishTime.compareTo(o.getPublishTime()); // Descending order
    }

    public void addReply(Message message) {
        replies.add(message);
    }

    public List<Message> getReplies() {
        return Collections.unmodifiableList(replies);
    }
}
