package ch.dvbern.stip.api.fall.repo;

import ch.dvbern.stip.api.common.repo.BaseNummerSeqRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class FallNummerSeqRepository extends BaseNummerSeqRepository {
    private final EntityManager entityManager;

    public int getNextValue(final String mandant) {
        final var seqName = String.format("fall_nummer_%s_seq", mandant);
        createSequenceIfNotExists(entityManager, seqName);
        return getNextValueFromSequence(entityManager, seqName);
    }
}
