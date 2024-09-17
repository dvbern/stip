package ch.dvbern.stip.api.common.repo;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

public abstract class BaseNummerSeqRepository {
    @Transactional(TxType.REQUIRES_NEW)
    protected void createSequenceIfNotExists(final EntityManager entityManager, final String seqName) {
        // Native queries with parameters result in a prepared statement, which cannot create a sequence
        // Which means we sadly have to String.format this...
        final var createSql = String.format("CREATE SEQUENCE IF NOT EXISTS %s AS INTEGER", seqName);
        final var createQuery = entityManager.createNativeQuery(createSql);
        createQuery.executeUpdate();
    }

    @Transactional(TxType.REQUIRES_NEW)
    protected int getNextValueFromSequence(final EntityManager entityManager, final String seqName) {
        final var selectSql = "SELECT nextval(:seqName)";
        final var selectQuery = entityManager.createQuery(selectSql);
        selectQuery.setParameter("seqName", seqName);

        final var result = selectQuery.getSingleResult();
        return ((Number) result).intValue();
    }
}
