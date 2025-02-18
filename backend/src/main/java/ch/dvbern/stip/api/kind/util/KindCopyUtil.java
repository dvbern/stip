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

package ch.dvbern.stip.api.kind.util;

import java.util.LinkedHashSet;
import java.util.Set;

import ch.dvbern.stip.api.common.util.AbstractPersonCopyUtil;
import ch.dvbern.stip.api.kind.entity.Kind;
import lombok.experimental.UtilityClass;

@UtilityClass
public class KindCopyUtil {
    public Kind createCopy(final Kind other) {
        final Kind copy = new Kind();

        AbstractPersonCopyUtil.copy(other, copy);
        copy.setWohnsitzAnteilPia(other.getWohnsitzAnteilPia());
        copy.setAusbildungssituation(other.getAusbildungssituation());
        copy.setErhalteneAlimentebeitraege(other.getErhalteneAlimentebeitraege());

        return copy;
    }

    public Set<Kind> createCopySet(final Set<Kind> other) {
        final var copy = new LinkedHashSet<Kind>();
        for (final var kind : other) {
            copy.add(createCopy(kind));
        }

        return copy;
    }

    public void overrideItem(Kind toBeReplaced, final Kind replacement) {
        AbstractPersonCopyUtil.copy(replacement, toBeReplaced);
        toBeReplaced.setWohnsitzAnteilPia(replacement.getWohnsitzAnteilPia());
        toBeReplaced.setAusbildungssituation(replacement.getAusbildungssituation());
        toBeReplaced.setErhalteneAlimentebeitraege(replacement.getErhalteneAlimentebeitraege());
        toBeReplaced.setAusbildungssituation(replacement.getAusbildungssituation());
    }

    public void doOverrideOfSet(Set<Kind> toBeReplaced, Set<Kind> replacement) {
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
