package posti.social.adapters;

import java.util.UUID;

public interface UserBuilder {
    <T extends UserBuilder> T withId(UUID id);
    <T extends UserBuilder> T withUsername(String username);
    <T extends UserBuilder> T withEmail(String email);
    <T extends UserBuilder> T withMessage(MessageBuilderVisitor messageBuilder);
}
