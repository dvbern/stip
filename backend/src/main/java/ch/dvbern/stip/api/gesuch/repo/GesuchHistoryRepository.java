package ch.dvbern.stip.api.gesuch.repo;

import java.util.ArrayList;
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
        final List<Gesuch> revisions = reader
            .createQuery()
            .forRevisionsOfEntity(Gesuch.class, true, true)
            .add(AuditEntity.property("id").eq(gesuchId))
            .getResultList()
            .stream()
            .filter(Gesuch.class::isInstance)
            .map(Gesuch.class::cast)
            .toList();

        Gesuch oldStatus = null;
        final var changes = new ArrayList<Gesuch>();
        for (final var revision : revisions) {
            if (oldStatus == null || oldStatus.getGesuchStatus() != revision.getGesuchStatus()) {
                changes.add(revision);
                oldStatus = revision;
            }
        }

        return changes;
    }
}
