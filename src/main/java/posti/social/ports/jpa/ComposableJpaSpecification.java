package posti.social.ports.jpa;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import static java.util.Arrays.asList;
import static org.springframework.data.jpa.domain.Specifications.where;

public  class ComposableJpaSpecification<T> extends JpaSpecificationQuery<T> {
    private Set<Specification<T>> filters = new HashSet<>();

    public ComposableJpaSpecification(JpaSpecificationExecutor<T> repository) {
        super(repository);
    }

    protected void addFilter(Optional<Specification<T>> filter) {
        filter.ifPresent(filters::add);
    }

    protected Specifications<T> buildSpecifications() {
        return and(filters);
    }

    public static <T> Specifications<T> and(Specification<T>... values) {
        return and(asList(values));
    }

    public static <T> Specifications<T> and(Collection<Specification<T>> values) {
        Specification<T> start = (root, query, cb) -> cb.isTrue(cb.literal(true));
        return values.stream().reduce(where(start), Specifications::and, Specifications::and);
    }
}
