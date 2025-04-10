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
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.type.Kontoinhaber;
import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.darlehen.entity.Darlehen;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.generator.entities.GesuchGenerator;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.partner.entity.Partner;
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
        GesuchFormular gesuchFormular = setupGesuchFormularWithChildEntities();
        gesuchFormular.setTranche(gesuch.getNewestGesuchTranche().get());
        gesuch.getNewestGesuchTranche().get().setGesuchFormular(gesuchFormular);
        gesuch.getNewestGesuchTranche().get().getGesuchFormular().setId(UUID.randomUUID());
        gesuch.setGesuchNummer(UUID.randomUUID().toString());
        gesuch.getNewestGesuchTranche().get().getGesuchFormular().setPersonInAusbildung(setupValidPersonInAusbildung());
        gesuch.getNewestGesuchTranche().get().setTyp(GesuchTrancheTyp.TRANCHE);

        gesuchFormular.getFamiliensituation().setElternVerheiratetZusammen(false);
        gesuchFormular.getFamiliensituation().setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN);
        gesuchFormular.getFamiliensituation().setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN);

        gesuchFormular.getPartner().setVorname("a");
        gesuchFormular.getPartner().setNachname("a");
        gesuchFormular.getPartner().setGeburtsdatum(LocalDate.of(1990, 1, 1));
        gesuchFormular.getAuszahlung().setVorname("a");
        gesuchFormular.getAuszahlung().setNachname("a");
        gesuchFormular.getAuszahlung().setIban("CH4489144522237167913");
        gesuchFormular.getEinnahmenKosten().setRenten(0);
        gesuchFormular.getEinnahmenKosten().setFahrkosten(0);
        gesuchFormular.getEinnahmenKosten().setSteuerjahr(2023);
        gesuchFormular.getEinnahmenKosten().setVerdienstRealisiert(false);
        gesuchFormular.getEinnahmenKosten().setNettoerwerbseinkommen(0);
        gesuchFormular.getPartner().setSozialversicherungsnummer("756.6523.5720.40");
        gesuchFormular.getAuszahlung().setKontoinhaber(Kontoinhaber.GESUCHSTELLER);
        gesuch.setGesuchNummer("23");

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

    public GesuchFormular setupGesuchFormularWithChildEntities() {
        return new GesuchFormular()
            .setPersonInAusbildung(new PersonInAusbildung().setAdresse(new Adresse()))
            .setFamiliensituation(new Familiensituation())
            .setPartner(new Partner().setAdresse(new Adresse()))
            .setAuszahlung(new Auszahlung().setAdresse(new Adresse()).setIban(""))
            .setEinnahmenKosten(new EinnahmenKosten())
            .setDarlehen(new Darlehen().setWillDarlehen(false));
    }
}
