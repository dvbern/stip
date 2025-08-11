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

package ch.dvbern.stip.api.ausbildung.util;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AusbildungCopyUtil {
    /**
     * Creates a copy including Stammdaten references
     */
    public Ausbildung createCopyIncludingStammdatenReferences(final Ausbildung other) {
        final var copy = new Ausbildung();

        copy.setAusbildungsgang(other.getAusbildungsgang());
        copy.setAlternativeAusbildungsgang(other.getAlternativeAusbildungsgang());
        copy.setAlternativeAusbildungsstaette(other.getAlternativeAusbildungsstaette());
        copy.setFachrichtungBerufsbezeichnung(other.getFachrichtungBerufsbezeichnung());
        copy.setAusbildungNichtGefunden(other.isAusbildungNichtGefunden());
        copy.setAusbildungBegin(other.getAusbildungBegin());
        copy.setAusbildungEnd(other.getAusbildungEnd());
        copy.setPensum(other.getPensum());
        copy.setAusbildungsort(other.getAusbildungsort());
        copy.setIsAusbildungAusland(other.getIsAusbildungAusland());

        return copy;
    }
}
