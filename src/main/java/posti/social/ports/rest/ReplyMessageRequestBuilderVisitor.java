package posti.social.ports.rest;

import java.util.UUID;

import posti.social.adapters.ReplyMessageServiceAdapter;

public class ReplyMessageRequestBuilderVisitor implements ReplyMessageServiceAdapter.RequestBuilderVisitor {
    private UUID authorId;
    private UUID messageId;
    private String content;

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public ReplyMessageServiceAdapter.RequestBuilder accept(ReplyMessageServiceAdapter.RequestBuilder builder) {
        return builder
                .withAuthorId(authorId)
                .withContent(content)
                .withMessageId(messageId);
    }
}
