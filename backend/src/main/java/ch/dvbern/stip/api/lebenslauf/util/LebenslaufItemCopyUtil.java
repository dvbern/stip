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
    public LebenslaufItem createCopy(final LebenslaufItem other) {
        final LebenslaufItem copy = new LebenslaufItem();

        copy.setBildungsart(other.getBildungsart());
        copy.setVon(other.getVon());
        copy.setBis(other.getBis());
        copy.setTaetigkeitsart(other.getTaetigkeitsart());
        copy.setTaetigkeitsBeschreibung(other.getTaetigkeitsBeschreibung());
        copy.setBerufsbezeichnung(other.getBerufsbezeichnung());
        copy.setFachrichtung(other.getFachrichtung());
        copy.setTitelDesAbschlusses(other.getTitelDesAbschlusses());
        copy.setAusbildungAbgeschlossen(other.isAusbildungAbgeschlossen());
        copy.setWohnsitz(other.getWohnsitz());

        return copy;
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
            LebenslaufItemCopyUtil::createCopy
        );
    }
}
