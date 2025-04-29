/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.common.repo;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.ws.rs.NotFoundException;

public interface BaseRepository<T extends AbstractEntity> extends PanacheRepositoryBase<T, UUID> {

    default T requireById(UUID id) {
        return findByIdOptional(id).orElseThrow(NotFoundException::new);
    }

    default Stream<T> requireAllById(final List<UUID> ids) {
        return list("id in ?1", ids).stream();
    }
}
