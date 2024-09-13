package ch.dvbern.stip.api.gesuch.repo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchNummerSeqRepository {
    private final EntityManager entityManager;

    public String getSequenceString(final String mandant, final int technischesJahr) {
        return String.format("gesuch_nummer_%s_%s_seq", mandant, technischesJahr);
    }

    @Transactional(TxType.REQUIRES_NEW)
    void createSequenceIfNotExists(final String seqName) {
        // Native queries with parameters result in a prepared statement, which cannot create a sequence
        // Which means we sadly have to String.format this...
        String createSql = String.format("CREATE SEQUENCE IF NOT EXISTS %s AS INTEGER", seqName);
        Query createQuery = entityManager.createNativeQuery(createSql);
        createQuery.executeUpdate();
    }

    @Transactional(TxType.REQUIRES_NEW)
    int getNextValueFromSequence(final String seqName) {
        String selectSql = "SELECT nextval(:seqName)";
        Query selectQuery = entityManager.createQuery(selectSql);
        selectQuery.setParameter("seqName", seqName);
        Object result = selectQuery.getSingleResult();
        return ((Number) result).intValue();
    }

    public int getNextSequenceValue(final String mandant, final int technischesJahr) {
        final String seqName = getSequenceString(mandant, technischesJahr);
        createSequenceIfNotExists(seqName);
        return getNextValueFromSequence(seqName);
    }
}
