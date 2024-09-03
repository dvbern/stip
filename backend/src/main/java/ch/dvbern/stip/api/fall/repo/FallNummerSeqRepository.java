package ch.dvbern.stip.api.fall.repo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class FallNummerSeqRepository {

    private final EntityManager entityManager;

    public int getNextValue() {
        String sql = "SELECT nextval('fall_nummer_seq')";
        Query query = entityManager.createNativeQuery(sql);
        Object result = query.getSingleResult();
        return ((Number) result).intValue();
    }
}
