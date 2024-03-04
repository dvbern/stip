package ch.dvbern.stip.api.common.repo;

import java.util.UUID;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.ws.rs.NotFoundException;

public interface BaseRepository<T extends AbstractEntity> extends PanacheRepositoryBase<T, UUID> {

    default T requireById(UUID id) {
        return findByIdOptional(id).orElseThrow(NotFoundException::new);
    }
}
