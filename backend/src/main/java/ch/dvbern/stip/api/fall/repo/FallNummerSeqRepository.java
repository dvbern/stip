package ch.dvbern.stip.api.fall.repo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class FallNummerSeqRepository {
    private final EntityManager entityManager;

    @Transactional(TxType.REQUIRES_NEW)
    void createSequenceIfNotExists(final String seqName) {
        final var createSql = String.format("CREATE SEQUENCE IF NOT EXISTS %s AS INTEGER", seqName);
        final var createQuery = entityManager.createNativeQuery(createSql);
        createQuery.executeUpdate();
    }

    @Transactional(TxType.REQUIRES_NEW)
    int getNextValueFromSequence(final String seqName) {
        final var selectSql = "SELECT nextval(:seqName)";
        final var selectQuery = entityManager.createQuery(selectSql);
        selectQuery.setParameter("seqName", seqName);

        final var result = selectQuery.getSingleResult();
        return ((Number) result).intValue();
    }

    public int getNextValue(final String mandant) {
        final var seqName = String.format("fall_nummer_%s_seq", mandant);
        createSequenceIfNotExists(seqName);
        return getNextValueFromSequence(seqName);
    }
}
