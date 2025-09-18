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
        copyValues(other, copy);
        return copy;
    }

    public void copyValues(final PersonInAusbildung source, final PersonInAusbildung target) {
        AbstractFamilieEntityCopyUtil.copy(source, target);
        target.setSozialversicherungsnummer(source.getSozialversicherungsnummer());
        target.setAnrede(source.getAnrede());
        target.setIdentischerZivilrechtlicherWohnsitz(source.isIdentischerZivilrechtlicherWohnsitz());
        target.setIdentischerZivilrechtlicherWohnsitzOrt(source.getIdentischerZivilrechtlicherWohnsitzOrt());
        target.setIdentischerZivilrechtlicherWohnsitzPLZ(source.getIdentischerZivilrechtlicherWohnsitzPLZ());
        target.setEmail(source.getEmail());
        target.setTelefonnummer(source.getTelefonnummer());
        target.setNationalitaet(source.getNationalitaet());
        target.setHeimatort(source.getHeimatort());
        target.setHeimatortPLZ(source.getHeimatortPLZ());
        target.setNiederlassungsstatus(source.getNiederlassungsstatus());
        target.setEinreisedatum(source.getEinreisedatum());
        target.setZivilstand(source.getZivilstand());
        target.setSozialhilfebeitraege(source.isSozialhilfebeitraege());
        target.setVormundschaft(source.isVormundschaft());
        target.setZustaendigeKESB(source.getZustaendigeKESB());
        target.setKorrespondenzSprache(source.getKorrespondenzSprache());
    }
}
