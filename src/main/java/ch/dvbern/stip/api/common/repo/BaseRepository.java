package ch.dvbern.stip.api.common.repo;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.UUID;

public interface BaseRepository<T extends AbstractEntity> extends PanacheRepositoryBase<T, UUID> {
}
