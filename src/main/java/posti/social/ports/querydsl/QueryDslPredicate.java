package posti.social.ports.querydsl;

import java.util.Optional;

import posti.social.application.domain.Query;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public abstract class QueryDslPredicate<T> implements Query<T> {
    private final QueryDslPredicateExecutor<T> executor;

    public QueryDslPredicate(QueryDslPredicateExecutor<T> repository) {
        this.executor = repository;
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return executor.findAll(buildPredicate(), pageable);
    }

    @Override
    public Iterable<T> findAll() {
        return executor.findAll(buildPredicate());
    }

    @Override
    public Iterable<T> findAll(Sort sort) {
        return executor.findAll(buildPredicate(), sort);
    }

    @Override
    public Optional<T> findOne() {
        return Optional.ofNullable(executor.findOne(buildPredicate()));
    }

    @Override
    public long count() {
        return executor.count(buildPredicate());
    }

    protected abstract Predicate buildPredicate();
}
