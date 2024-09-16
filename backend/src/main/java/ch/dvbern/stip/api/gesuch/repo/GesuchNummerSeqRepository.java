package ch.dvbern.stip.api.gesuch.repo;

import ch.dvbern.stip.api.common.repo.BaseNummerSeqRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchNummerSeqRepository extends BaseNummerSeqRepository {
    private final EntityManager entityManager;

    public String getSequenceString(final String mandant, final int technischesJahr) {
        return String.format("gesuch_nummer_%s_%s_seq", mandant, technischesJahr);
    }

    public int getNextSequenceValue(final String mandant, final int technischesJahr) {
        final String seqName = getSequenceString(mandant, technischesJahr);
        createSequenceIfNotExists(entityManager, seqName);
        return getNextValueFromSequence(entityManager, seqName);
    }
}
