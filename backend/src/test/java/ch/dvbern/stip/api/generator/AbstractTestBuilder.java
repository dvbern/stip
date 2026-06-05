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

package ch.dvbern.stip.api.generator;

import java.time.LocalDate;
import java.util.UUID;
import java.util.function.Consumer;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import lombok.Getter;

public abstract class AbstractTestBuilder<E extends AbstractEntity, Self extends AbstractTestBuilder<E, Self>> {

    protected final E entity;
    @Getter
    protected final LocalDate referenceDate;;

    protected AbstractTestBuilder(E entity, LocalDate referenceDate) {
        this.entity = entity;
        this.referenceDate = referenceDate;

        this.entity.setId(UUID.randomUUID());
        this.entity.setTimestampErstellt(referenceDate.atStartOfDay());
        this.entity.setTimestampMutiert(referenceDate.atStartOfDay());
        this.entity.setUserErstellt("Test User");
        this.entity.setUserMutiert("Test User");
    }

    @SuppressWarnings("unchecked")
    public Self with(Consumer<E> mutator) {
        mutator.accept(entity);
        return (Self) this;
    }

    public E build() {
        return entity;
    }
}
