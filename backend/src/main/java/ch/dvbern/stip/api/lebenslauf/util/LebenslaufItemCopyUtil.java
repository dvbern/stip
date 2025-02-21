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

package ch.dvbern.stip.api.lebenslauf.util;

import java.util.LinkedHashSet;
import java.util.Set;

import ch.dvbern.stip.api.common.util.OverrideUtil;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LebenslaufItemCopyUtil {
    public LebenslaufItem createCopy(final LebenslaufItem source) {
        final LebenslaufItem copy = new LebenslaufItem();
        copyValues(source, copy);
        return copy;
    }

    private void copyValues(final LebenslaufItem source, final LebenslaufItem target) {
        target.setBildungsart(source.getBildungsart());
        target.setVon(source.getVon());
        target.setBis(source.getBis());
        target.setTaetigkeitsart(source.getTaetigkeitsart());
        target.setTaetigkeitsBeschreibung(source.getTaetigkeitsBeschreibung());
        target.setBerufsbezeichnung(source.getBerufsbezeichnung());
        target.setFachrichtung(source.getFachrichtung());
        target.setTitelDesAbschlusses(source.getTitelDesAbschlusses());
        target.setAusbildungAbgeschlossen(source.isAusbildungAbgeschlossen());
        target.setWohnsitz(source.getWohnsitz());
    }

    public Set<LebenslaufItem> createCopyOfSet(final Set<LebenslaufItem> other) {
        final var copy = new LinkedHashSet<LebenslaufItem>();
        for (final var lebenslaufItem : other) {
            copy.add(createCopy(lebenslaufItem));
        }

        return copy;
    }

    public void doOverrideOfSet(Set<LebenslaufItem> targetItems, Set<LebenslaufItem> sourceItems) {
        OverrideUtil.doOverrideOfSet(
            targetItems,
            sourceItems,
            LebenslaufItemCopyUtil::copyValues
        );
    }
}
