package posti.social.ports.rest;

import posti.social.adapters.CreateUserServiceAdapter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserRequestBuilderVisitor implements CreateUserServiceAdapter.RequestBuilderVisitor {
    private final String username;
    private final String email;

    @JsonCreator
    public CreateUserRequestBuilderVisitor(
            @JsonProperty("username") String username,
            @JsonProperty("email") String email) {
        this.username = username;
        this.email = email;
    }

    @Override
    public CreateUserServiceAdapter.RequestBuilder accept(CreateUserServiceAdapter.RequestBuilder builder) {
        return builder
                .withUsername(username)
                .withEmail(email);
    }
}
