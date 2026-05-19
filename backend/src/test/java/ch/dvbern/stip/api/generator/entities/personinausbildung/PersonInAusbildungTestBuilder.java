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

package ch.dvbern.stip.api.generator.entities.personinausbildung;

import java.time.LocalDate;

import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.generator.AbstractTestBuilder;
import ch.dvbern.stip.api.generator.entities.adresse.AdresseTestBuilder;
import ch.dvbern.stip.api.generator.entities.land.LandTestBuilder;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildungBuilder;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.util.TestConstants;

public final class PersonInAusbildungTestBuilder
extends AbstractTestBuilder<PersonInAusbildung, PersonInAusbildungTestBuilder> {
    PersonInAusbildungTestBuilder(PersonInAusbildung entity, LocalDate referenceDate) {
        super(entity, referenceDate);
    }

    public static PersonInAusbildungTestBuilder standardNestedDeps(LocalDate referenceDate) {
        PersonInAusbildung personInAusbildung = PersonInAusbildungBuilder.personInAusbildung()
            .adresse(AdresseTestBuilder.standardDirectDeps(referenceDate).build())
            .sozialversicherungsnummer("")
            .anrede(Anrede.HERR)
            .identischerZivilrechtlicherWohnsitz(true)
            .identischerZivilrechtlicherWohnsitzOrt(null)
            .identischerZivilrechtlicherWohnsitzPLZ(null)
            .email("test@tester.ch")
            .telefonnummer("079 123 45 67")
            .nationalitaet(LandTestBuilder.swiss(referenceDate).build())
            .heimatort("Bern")
            .heimatortPLZ("3000")
            .niederlassungsstatus(null)
            .einreisedatum(null)
            .zivilstand(Zivilstand.LEDIG)
            .sozialhilfebeitraege(false)
            .vormundschaft(false)
            .korrespondenzSprache(Sprache.DEUTSCH)
            .zustaendigeKESB(null)
            .build();

        personInAusbildung.setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        personInAusbildung.setGeburtsdatum(referenceDate.minusYears(18));
        personInAusbildung.setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID);

        return new PersonInAusbildungTestBuilder(personInAusbildung, referenceDate);
    }
}
