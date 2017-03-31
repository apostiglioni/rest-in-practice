package posti.social.application.domain;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface MessageRepository extends JpaRepository<Message, UUID>, JpaSpecificationExecutor<Message>, QueryDslPredicateExecutor {
    Optional<Message> findById(UUID id);
}
