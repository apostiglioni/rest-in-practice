package posti.social.application.domain;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class PersistentObject implements Identifiable<UUID> {
    @Id @Column(columnDefinition = "CHAR(32)")
    private UUID id = UUID.randomUUID();

    @Override
    public final UUID getId() {
        return id;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || !(obj instanceof PersistentObject)) {
            return false;
        }

        return id.equals(((PersistentObject) obj).getId());
    }

    @Override
    public final int hashCode() {
        return id.hashCode();
    }
}
