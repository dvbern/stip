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

    @Transactional(TxType.REQUIRES_NEW)
    public void createSequenceIfNotExists(String seqName) {
        String createSql = String.format("CREATE SEQUENCE IF NOT EXISTS %s AS INTEGER", seqName);
        Query createQuery = entityManager.createNativeQuery(createSql);
        createQuery.executeUpdate();
    }

    @Transactional(TxType.REQUIRES_NEW)
    public int getNextValueFromCurrentSeq(String seqName) {
        String selectSql = String.format("SELECT nextval('%s')", seqName);
        Query selectQuery = entityManager.createNativeQuery(selectSql);
        Object result = selectQuery.getSingleResult();
        return ((Number) result).intValue();
    }
}
