package posti.social.adapters;

public interface MessageBuilderVisitor {
    <T extends MessageBuilder> T accept(T builder);
}
