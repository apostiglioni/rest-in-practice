package posti.social.adapters;

import posti.social.application.domain.User;

public class UserAdapter implements UserBuilderVisitor {
    private final User user;

    public UserAdapter(User user) {
        this.user = user;
    }

    @Override
    public <T extends UserBuilder> T accept(T builder) {
        builder
           .withId(user.getId())
           .withUsername(user.getUsername())
           .withEmail(user.getEmail());

        user.getMessages().forEach(message -> {
            builder.withMessage(new MessageAdapter(message));
        });

        return builder;
    }
}
