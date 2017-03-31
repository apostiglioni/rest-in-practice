package posti.social.ports.rest.resources.users.messages;

import java.util.UUID;

import posti.social.adapters.PublishMessageServiceAdapter;

public class PublishMessageRequestBuilderVisitor implements PublishMessageServiceAdapter.RequestBuilderVisitor {
    private UUID userId;
    private String message;

    @Override
    public PublishMessageServiceAdapter.RequestBuilder accept(PublishMessageServiceAdapter.RequestBuilder builder) {
        return builder
                .withMessage(message)
                .withUserId(userId);
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
