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

package ch.dvbern.stip.api.adresse.util;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AdresseCopyUtil {
    public Adresse createCopy(final Adresse other) {
        final var copy = new Adresse();

        copy.setLand(other.getLand());
        copy.setCoAdresse(other.getCoAdresse());
        copy.setStrasse(other.getStrasse());
        copy.setHausnummer(other.getHausnummer());
        copy.setPlz(other.getPlz());
        copy.setOrt(other.getOrt());

        return copy;
    }
}
