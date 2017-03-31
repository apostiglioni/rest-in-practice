package posti.social.adapters;

public interface UserBuilderVisitor {
    <T extends UserBuilder> T accept(T builder);
}
