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

package ch.dvbern.stip.api.personinausbildung.util;

import ch.dvbern.stip.api.common.util.AbstractFamilieEntityCopyUtil;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PersonInAusbildungCopyUtil {
    /**
     * Creates a copy ignoring properties that are a reference to another entity
     */
    public PersonInAusbildung createCopyIgnoreReferences(final PersonInAusbildung other) {
        final var copy = new PersonInAusbildung();
        AbstractFamilieEntityCopyUtil.copy(other, copy);

        copy.setSozialversicherungsnummer(other.getSozialversicherungsnummer());
        copy.setAnrede(other.getAnrede());
        copy.setIdentischerZivilrechtlicherWohnsitz(other.isIdentischerZivilrechtlicherWohnsitz());
        copy.setIdentischerZivilrechtlicherWohnsitzOrt(other.getIdentischerZivilrechtlicherWohnsitzOrt());
        copy.setIdentischerZivilrechtlicherWohnsitzPLZ(other.getIdentischerZivilrechtlicherWohnsitzPLZ());
        copy.setEmail(other.getEmail());
        copy.setTelefonnummer(other.getTelefonnummer());
        copy.setNationalitaet(other.getNationalitaet());
        copy.setHeimatort(other.getHeimatort());
        copy.setNiederlassungsstatus(other.getNiederlassungsstatus());
        copy.setZustaendigerKanton(other.getZustaendigerKanton());
        copy.setEinreisedatum(other.getEinreisedatum());
        copy.setZivilstand(other.getZivilstand());
        copy.setSozialhilfebeitraege(other.isSozialhilfebeitraege());
        copy.setVormundschaft(other.isVormundschaft());
        copy.setKorrespondenzSprache(other.getKorrespondenzSprache());

        return copy;
    }
}
