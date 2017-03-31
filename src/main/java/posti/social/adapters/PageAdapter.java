package posti.social.adapters;

import java.util.function.Function;

import org.springframework.data.domain.Page;

public class PageAdapter<T> implements PageBuilderVisitor<T> {
    private final Page<T> page;

    public PageAdapter(Page<T> page) {
        this.page = page;
    }

    @Override
    public <B extends PageBuilder<T>> B accept(B builder) {
        builder
                .withTotalElements(page.getTotalElements())
                .withNumberOfElements(page.getNumberOfElements())
                .withSize(page.getSize())
                .withNumber(page.getNumber());

        page.forEach(builder::withElement);

        return builder;
    }

    public <X> PageAdapter<X> map(Function<T, X> contentAdapter) {
        return new PageAdapter<X>(page.map(contentAdapter::apply));
    }
}
