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

package ch.dvbern.stip.api.geschwister.util;

import java.util.LinkedHashSet;
import java.util.Set;

import ch.dvbern.stip.api.common.util.AbstractFamilieEntityCopyUtil;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GeschwisterCopyUtil {
    public Geschwister createCopy(final Geschwister other) {
        final var copy = new Geschwister();

        AbstractFamilieEntityCopyUtil.copy(other, copy);
        copy.setAusbildungssituation(other.getAusbildungssituation());

        return copy;
    }

    public Set<Geschwister> createCopyOfSet(final Set<Geschwister> geschwisters) {
        final var copy = new LinkedHashSet<Geschwister>();
        for (final var geschwister : geschwisters) {
            copy.add(createCopy(geschwister));
        }

        return copy;
    }

    public void overrideItem(Geschwister toBeReplaced, final Geschwister replacement) {
        AbstractFamilieEntityCopyUtil.copy(replacement, toBeReplaced);
        toBeReplaced.setAusbildungssituation(replacement.getAusbildungssituation());
    }

    public void doOverrideOfSet(Set<Geschwister> toBeReplaced, Set<Geschwister> replacement) {
        for (var item : toBeReplaced) {
            if (replacement.contains(item)) {
                overrideItem(item, replacement.stream().filter(x -> x.equals(item)).findFirst().get());
            } else {
                // new item -> add to list
                toBeReplaced.add(item);
            }
        }
    }
}
