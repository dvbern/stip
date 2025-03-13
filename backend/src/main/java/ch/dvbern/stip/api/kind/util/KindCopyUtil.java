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
import ch.dvbern.stip.api.common.util.OverrideUtil;
import ch.dvbern.stip.api.kind.entity.Kind;
import lombok.experimental.UtilityClass;

@UtilityClass
public class KindCopyUtil {
    public Kind createCopy(final Kind source) {
        final var copy = new Kind();
        copyValues(source, copy);
        return copy;
    }

    private void copyValues(final Kind source, final Kind target) {
        AbstractPersonCopyUtil.copy(source, target);
        target.setWohnsitzAnteilPia(source.getWohnsitzAnteilPia());
        target.setAusbildungssituation(source.getAusbildungssituation());
        target.setErhalteneAlimentebeitraege(source.getErhalteneAlimentebeitraege());
    }

    public Set<Kind> createCopySet(final Set<Kind> other) {
        final var copy = new LinkedHashSet<Kind>();
        for (final var kind : other) {
            copy.add(createCopy(kind));
        }

        return copy;
    }

    public void doOverrideOfSet(Set<Kind> targetKinder, Set<Kind> sourceKinder) {
        OverrideUtil.doOverrideOfSet(
            targetKinder,
            sourceKinder,
            KindCopyUtil::copyValues,
            KindCopyUtil::createCopy
        );
    }
}
