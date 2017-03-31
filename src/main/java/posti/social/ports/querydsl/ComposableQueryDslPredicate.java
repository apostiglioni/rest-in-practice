package posti.social.ports.querydsl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import static java.util.Arrays.asList;

public  class ComposableQueryDslPredicate<T> extends QueryDslPredicate<T> {
    private Set<Predicate> filters = new HashSet<>();

    public ComposableQueryDslPredicate(QueryDslPredicateExecutor<T> repository) {
        super(repository);
    }

    protected void addFilter(Optional<Predicate> filter) {
        filter.ifPresent(filters::add);
    }

    protected Predicate buildPredicate() {
        return and(filters);
    }

    public static Predicate and(Predicate... values) {
        return and(asList(values));
    }

    public static Predicate and(Collection<Predicate> values) {
        return values.stream().reduce(new BooleanBuilder(), BooleanBuilder::and, BooleanBuilder::and).getValue();
    }
}
