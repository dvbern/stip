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

package ch.dvbern.stip.api.darlehen.util;

import ch.dvbern.stip.api.darlehen.entity.Darlehen;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DarlehenCopyUtil {
    public Darlehen createCopy(Darlehen other) {
        if (other == null) {
            return null;
        }
        final var copy = new Darlehen();
        copyValues(other, copy);
        return copy;
    }

    public static void copyValues(final Darlehen source, final Darlehen target) {
        target.setWillDarlehen(source.getWillDarlehen());
        target.setBetragDarlehen(source.getBetragDarlehen());
        target.setBetragBezogenKanton(source.getBetragBezogenKanton());
        target.setSchulden(source.getSchulden());
        target.setAnzahlBetreibungen(source.getAnzahlBetreibungen());
        target.setGrundNichtBerechtigt(source.getGrundNichtBerechtigt());
        target.setGrundAusbildungZwoelfJahre(source.getGrundAusbildungZwoelfJahre());
        target.setGrundHoheGebuehren(source.getGrundHoheGebuehren());
        target.setGrundAnschaffungenFuerAusbildung(source.getGrundAnschaffungenFuerAusbildung());
        target.setGrundZweitausbildung(source.getGrundZweitausbildung());
    }
}
