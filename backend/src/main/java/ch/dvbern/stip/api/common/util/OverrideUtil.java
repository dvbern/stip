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

package ch.dvbern.stip.api.common.util;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OverrideUtil {
    public <T extends AbstractEntity> void doOverrideOfSet(
        final Set<T> targetEntities,
        final Set<T> sourceEntities,
        final BiConsumer<T, T> ifPresent,
        final Function<T, T> ifAbsent
    ) {
        // Remove all entities that were added and now need to be reset
        targetEntities.removeIf(targetEntity -> !sourceEntities.contains(targetEntity));

        for (var source : sourceEntities) {
            if (targetEntities.contains(source)) {
                // If target contains an entity with the same ID, then copy the source values over
                final var replacement =
                    targetEntities.stream().filter(entity -> source.getId().equals(entity.getId())).findFirst();
                replacement.ifPresent(replacementItem -> ifPresent.accept(source, replacementItem));
            } else {
                targetEntities.add(ifAbsent.apply(source));
            }
        }
    }
}
