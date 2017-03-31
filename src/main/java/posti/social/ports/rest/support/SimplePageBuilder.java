package posti.social.ports.rest.support;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;

import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.standard.StandardRepresentationFactory;
import org.springframework.web.util.UriComponentsBuilder;
import posti.social.adapters.PageBuilder;

import static com.theoryinpractise.halbuilder.api.RepresentationFactory.PRETTY_PRINT;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

public class SimplePageBuilder<T> implements PageBuilder<T> {
    private final Representation representation = new StandardRepresentationFactory().withFlag(PRETTY_PRINT).newRepresentation();
    private final String contentRel;
    private long totalElements;
    private int numberOfElements;
    private int number;
    private List<T> content = new ArrayList<>();
    private int size;

    public SimplePageBuilder(String contentRel) {
        this.contentRel = contentRel;
    }

    @Override
    public PageBuilder<T> withTotalElements(long totalElements) {
        this.totalElements = totalElements;
        return this;
    }

    @Override
    public PageBuilder<T> withNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
        return this;
    }

    @Override
    public PageBuilder<T> withNumber(int number) {
        this.number = number;
        return this;
    }

    @Override
    public PageBuilder<T> withElement(T element) {
        content.add(element);
        return this;
    }

    @Override
    public PageBuilder<T> withSize(int size) {
        this.size = size;
        return this;
    }

    public <M> SimplePageBuilder<M> map(Function<T, M> converter) {
        SimplePageBuilder<M> builder = new SimplePageBuilder<>(contentRel);
        builder.withNumber(number);
        builder.withNumberOfElements(numberOfElements);
        builder.withSize(size);
        builder.withTotalElements(totalElements);
        content.forEach(e -> builder.withElement(converter.apply(e)));

        return builder;
    }

    public ReadableRepresentation build(HttpServletRequest request) {
        UriComponentsBuilder base = fromHttpUrl(request.getRequestURL().toString()).query(request.getQueryString());

        int totalPages = numberOfElements == 0 ? 1 : (int) Math.ceil((double) totalElements / (double) size);

        representation.withProperty("totalPages", totalPages);
        representation.withProperty("totalElements", totalElements);
        representation.withProperty("numberOfElements", numberOfElements);
        representation.withProperty("page", number);
        content.forEach(e -> representation.withRepresentation(contentRel, (ReadableRepresentation) e));

        representation.withLink("self", base.toUriString());

        if (number + 1 < totalElements) {
            representation.withLink("next", base.cloneBuilder().replaceQueryParam("page", number + 1).toUriString());
        }

        if (number > 0) {
            representation.withLink("previous", base.cloneBuilder().replaceQueryParam("page", number - 1).toUriString());
        }

        return representation;
    }
}
