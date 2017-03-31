package posti.social.adapters;

public interface PageBuilderVisitor<T> {
    <B extends PageBuilder<T>> B accept(B builder);
}
