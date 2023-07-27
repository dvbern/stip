package ch.dvbern.stip.api.common.repo;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.ws.rs.NotFoundException;

import java.util.UUID;

public interface BaseRepository<T extends AbstractEntity> extends PanacheRepositoryBase<T, UUID> {

    default T requireById(UUID id) {
        return findByIdOptional(id).orElseThrow(NotFoundException::new);
    }

}
