package posti.social.adapters;

import posti.social.application.domain.Message;

public class MessageAdapter implements MessageBuilderVisitor {
    private final Message message;

    public MessageAdapter(Message message) {
        this.message = message;
    }

    @Override
    public <T extends MessageBuilder> T accept(T builder) {
        return builder
                 .withId(message.getId())
                 .withBody(message.getBody())
                 .withPublishTime(message.getPublishTime())
                 .withAuthor(new UserAdapter(message.getAuthor()));
    }
}
