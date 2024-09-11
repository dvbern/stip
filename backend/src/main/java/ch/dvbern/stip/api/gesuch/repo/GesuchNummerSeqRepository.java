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
    public void createSequenceIfNotExists(final String seqName) {
        // It seems like nativeQueries (Which we need for 'CREATE SEQUENCE') does not support parameters
        // (both positional and named) which means we need to use string formatting
        //        String createSql = "CREATE SEQUENCE IF NOT EXISTS :seqName AS INTEGER";
        //        Query createQuery = entityManager.createNativeQuery(createSql);
        //        createQuery.setParameter("seqName", seqName);
        //        createQuery.executeUpdate();
        // TODO: ???? Fix this as soon as possible
        String createSql = String.format("CREATE SEQUENCE IF NOT EXISTS %s AS INTEGER", seqName);
        Query createQuery = entityManager.createNativeQuery(createSql);
        createQuery.executeUpdate();
    }

    @Transactional(TxType.REQUIRES_NEW)
    public int getNextValueFromSequence(final String seqName) {
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
