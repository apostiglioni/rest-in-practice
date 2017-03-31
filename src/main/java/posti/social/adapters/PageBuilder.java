package posti.social.adapters;

public interface PageBuilder<T> {
    PageBuilder<T> withTotalElements(long totalElements);
    PageBuilder<T> withNumberOfElements(int numberOfElements);
    PageBuilder<T> withNumber(int number);
    PageBuilder<T> withElement(T element);
    PageBuilder<T> withSize(int size);
}
