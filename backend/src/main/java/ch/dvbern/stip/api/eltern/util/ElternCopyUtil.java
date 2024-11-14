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

package ch.dvbern.stip.api.eltern.util;

import java.util.LinkedHashSet;
import java.util.Set;

import ch.dvbern.stip.api.common.util.AbstractPersonCopyUtil;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ElternCopyUtil {
    public Eltern createCopyWithoutReferences(final Eltern other) {
        final var copy = new Eltern();

        AbstractPersonCopyUtil.copy(other, copy);
        copy.setAdresse(other.getAdresse());
        copy.setSozialversicherungsnummer(other.getSozialversicherungsnummer());
        copy.setSozialhilfebeitraege(other.isSozialhilfebeitraege());
        copy.setErgaenzungsleistungen(other.getErgaenzungsleistungen());
        copy.setElternTyp(other.getElternTyp());
        copy.setTelefonnummer(other.getTelefonnummer());
        copy.setAusweisbFluechtling(other.getAusweisbFluechtling());
        copy.setIdentischerZivilrechtlicherWohnsitz(other.isIdentischerZivilrechtlicherWohnsitz());
        copy.setIdentischerZivilrechtlicherWohnsitzOrt(other.getIdentischerZivilrechtlicherWohnsitzOrt());
        copy.setIdentischerZivilrechtlicherWohnsitzPLZ(other.getIdentischerZivilrechtlicherWohnsitzPLZ());
        copy.setWohnkosten(other.getWohnkosten());

        return copy;
    }

    public Set<Eltern> createCopyOfSetWithoutReferences(final Set<Eltern> other) {
        final var copy = new LinkedHashSet<Eltern>();
        for (final var eltern : other) {
            copy.add(createCopyWithoutReferences(eltern));
        }

        return copy;
    }
}
