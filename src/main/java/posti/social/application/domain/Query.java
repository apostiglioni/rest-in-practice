package posti.social.application.domain;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static java.util.stream.StreamSupport.stream;

public interface Query<T> {
    Page<T> findAll(Pageable pageable);
    Iterable<T> findAll();
    Iterable<T> findAll(Sort sort);
    Optional<T> findOne();
    long count();

    default Stream<T> streamAll(boolean parallel) {
        return stream(findAll().spliterator(), parallel);
    }
    default Stream<T> streamAll(Sort sort, boolean parallel) {
        return stream(findAll(sort).spliterator(), parallel);
    }
}
