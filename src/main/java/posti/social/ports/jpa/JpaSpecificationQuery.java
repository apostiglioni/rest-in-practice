package posti.social.ports.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import posti.social.application.domain.Query;

public abstract class JpaSpecificationQuery<T> implements Query<T> {
    private final JpaSpecificationExecutor<T> executor;

    public JpaSpecificationQuery(JpaSpecificationExecutor<T> repository) {
        this.executor = repository;
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return executor.findAll(buildSpecifications(), pageable);
    }

    @Override
    public Iterable<T> findAll() {
        return executor.findAll(buildSpecifications());
    }

    @Override
    public List<T> findAll(Sort sort) {
        return executor.findAll(buildSpecifications(), sort);
    }

    @Override
    public Optional<T> findOne() {
        return Optional.ofNullable(executor.findOne(buildSpecifications()));
    }

    @Override
    public long count() {
        return executor.count(buildSpecifications());
    }

    protected abstract Specifications<T> buildSpecifications();
}
