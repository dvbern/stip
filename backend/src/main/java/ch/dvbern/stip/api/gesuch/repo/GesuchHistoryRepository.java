package ch.dvbern.stip.api.gesuch.repo;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchHistoryRepository {
    private final EntityManager entityManager;

    public List<Gesuch> getStatusHistory(final UUID gesuchId) {
        final var reader = AuditReaderFactory.get(entityManager);
        @SuppressWarnings("unchecked")
        // Reason: forRevisionsOfEntity with Gesuch.class and selectEntitiesOnly will always return a List<Gesuch>
        final List<Gesuch> revisions = reader
            .createQuery()
            .forRevisionsOfEntity(Gesuch.class, true, true)
            .add(AuditEntity.property("id").eq(gesuchId))
            .add(AuditEntity.property("gesuchStatus").hasChanged())
            .getResultList()
            .stream()
            .map(Gesuch.class::cast)
            .toList();

        return revisions;
    }
}
