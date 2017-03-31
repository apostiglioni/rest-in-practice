package posti.social.ports.rest;

import java.util.UUID;

import posti.social.adapters.PublishMessageServiceAdapter.RequestBuilder;
import posti.social.adapters.PublishMessageServiceAdapter.RequestBuilderVisitor;

public class PublishMessageRequestBuilderVisitor implements RequestBuilderVisitor {
    private UUID userId;
    private String message;

    @Override
    public RequestBuilder accept(RequestBuilder builder) {
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
