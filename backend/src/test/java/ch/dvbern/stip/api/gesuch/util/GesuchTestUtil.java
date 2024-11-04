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

package ch.dvbern.stip.api.gesuch.util;

import java.time.LocalDate;
import java.util.UUID;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.generator.entities.GesuchGenerator;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.stammdaten.type.Land;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GesuchTestUtil {
    public Gesuch setupValidGesuch() {
        Gesuch gesuch = GesuchGenerator.initGesuch();
        gesuch.setId(UUID.randomUUID());
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        gesuch.getNewestGesuchTranche().get().setGesuchFormular(new GesuchFormular());
        gesuch.getNewestGesuchTranche().get().getGesuchFormular().setId(UUID.randomUUID());
        gesuch.setGesuchNummer(UUID.randomUUID().toString());
        gesuch.getNewestGesuchTranche().get().getGesuchFormular().setPersonInAusbildung(setupValidPersonInAusbildung());
        gesuch.getNewestGesuchTranche().get().setTyp(GesuchTrancheTyp.TRANCHE);

        return gesuch;
    }

    public Gesuch setupValidGesuchInState(Gesuchstatus status) {
        Gesuch gesuch = setupValidGesuch();
        return gesuch.setGesuchStatus(status);
    }

    public PersonInAusbildung setupValidPersonInAusbildung() {
        PersonInAusbildung personInAusbildung = new PersonInAusbildung();
        personInAusbildung.setSozialversicherungsnummer("756.1119.8398.42");
        personInAusbildung.setVorname("A");
        personInAusbildung.setNachname("B");
        Adresse adresse = new Adresse();
        adresse.setStrasse("a");
        adresse.setHausnummer("1");
        adresse.setId(UUID.randomUUID());
        adresse.setPlz("3333");
        adresse.setLand(Land.CH);
        adresse.setOrt("b");
        personInAusbildung.setHeimatort("B");
        personInAusbildung.setAdresse(adresse);
        personInAusbildung.setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        personInAusbildung.setTelefonnummer(UUID.randomUUID().toString());
        personInAusbildung.setGeburtsdatum(LocalDate.now());
        personInAusbildung.setKorrespondenzSprache(Sprache.DEUTSCH);
        personInAusbildung.setAnrede(Anrede.FRAU);
        personInAusbildung.setEmail("test@test.com");
        personInAusbildung.setZivilstand(Zivilstand.LEDIG);

        return personInAusbildung;
    }
}
