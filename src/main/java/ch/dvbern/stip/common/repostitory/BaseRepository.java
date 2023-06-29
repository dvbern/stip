package ch.dvbern.stip.common.repostitory;

import ch.dvbern.stip.persistence.AbstractEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.UUID;

public interface BaseRepository<T extends AbstractEntity> extends PanacheRepositoryBase<T, UUID> {
}
