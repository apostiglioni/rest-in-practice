package posti.social.ports.rest;

import javax.servlet.http.HttpServletRequest;

import posti.social.adapters.MessageBuilderVisitor;
import posti.social.adapters.PageBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.ResponseEntity.ok;

public class PagedMessagesResponseBuilder implements PageBuilder<MessageBuilderVisitor> {
    private final SimplePageBuilder<MessageBuilderVisitor> pageBuilder;

    public PagedMessagesResponseBuilder() {
        this.pageBuilder = new SimplePageBuilder<>("content");
    }

    public PagedMessagesResponseBuilder(String contentRel) {
        this.pageBuilder = new SimplePageBuilder<>(contentRel);
    }

    @Override
    public PageBuilder<MessageBuilderVisitor> withTotalElements(long totalElements) {
        pageBuilder.withTotalElements(totalElements);
        return this;
    }

    @Override
    public PageBuilder<MessageBuilderVisitor> withNumberOfElements(int numberOfElements) {
        pageBuilder.withNumberOfElements(numberOfElements);
        return this;
    }

    @Override
    public PageBuilder<MessageBuilderVisitor> withNumber(int number) {
        pageBuilder.withNumber(number);
        return this;
    }

    @Override
    public PageBuilder<MessageBuilderVisitor> withElement(MessageBuilderVisitor element) {
        pageBuilder.withElement(element);
        return this;
    }

    @Override
    public PageBuilder<MessageBuilderVisitor> withSize(int size) {
        pageBuilder.withSize(size);
        return this;
    }

    public ResponseEntity<String> build(HttpServletRequest httpRequest, String contentType) {
        return ok()
              .contentType(MediaType.valueOf(contentType))
              .body(pageBuilder.map(v -> v.accept(new MessageRepresentationBuilder()))
                               .map(MessageRepresentationBuilder::build)
                               .build(httpRequest)
                               .toString(contentType));
    }
}
