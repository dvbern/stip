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

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.common.entity.AbstractPerson;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AbstractFamilieEntityCopyUtil {
    /**
     * Copies all properties from a given source {@link AbstractFamilieEntity} to a target {@link AbstractFamilieEntity}
     * Including all properties from the base {@link AbstractPerson}
     */
    public void copy(final AbstractFamilieEntity source, final AbstractFamilieEntity target) {
        AbstractPersonCopyUtil.copy(source, target);
        target.setWohnsitz(source.getWohnsitz());
        target.setWohnsitzAnteilMutter(source.getWohnsitzAnteilMutter());
        target.setWohnsitzAnteilVater(source.getWohnsitzAnteilVater());
    }

    public void doOverrideOfSet(Set<AbstractFamilieEntity> toBeReplaced, Set<AbstractFamilieEntity> replacement) {
        for (var item : toBeReplaced) {
            if (replacement.contains(item)) {
                copy(item, replacement.stream().filter(x -> x.equals(item)).findFirst().get());
            } else {
                // new item -> add to list
                toBeReplaced.add(item);
            }
        }
    }
}
