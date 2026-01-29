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

package ch.dvbern.stip.api.demo.util;

import java.util.Objects;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.entity.AbstractPerson;
import ch.dvbern.stip.api.demo.entity.DemoData;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DemoDataAnonymizerUtil {
    public void anonymizeGesuch(DemoData demoData, Gesuch gesuch) {
        anonymizeZahlungsverbindung(gesuch);
        anonymizePersonInAusbildung(demoData, gesuch);
        anonymizePartner(demoData, gesuch);
        anonymizeEltern(demoData, gesuch);
        anonymizeGeschwisters(demoData, gesuch);
        anonymizeKinds(demoData, gesuch);
    }

    private void anonymizeZahlungsverbindung(Gesuch gesuch) {
        final var zahlungsverbindung = gesuch.getAusbildung().getFall().getAuszahlung().getZahlungsverbindung();
        final var gesuchSuffix = getLastGesuchNummerPart(gesuch);
        zahlungsverbindung.setVorname("Vorname-%s".formatted(gesuchSuffix));
        zahlungsverbindung.setNachname("Nachname-%s".formatted(gesuchSuffix));
        anonymizeAdresse(gesuch, zahlungsverbindung.getAdresse());
    }

    private void anonymizeAbstractPerson(DemoData demoData, AbstractPerson person, String prefix, String suffix) {
        person.setVorname("%s-%s".formatted(prefix, suffix));
        person.setNachname("%s-%s".formatted(demoData.getTestFall(), suffix));
    }

    private void anonymizePersonInAusbildung(DemoData demoData, Gesuch gesuch) {
        final var personInAusbildung = gesuch.getLatestGesuchTranche().getGesuchFormular().getPersonInAusbildung();
        final var gesuchSuffix = getLastGesuchNummerPart(gesuch);
        anonymizeAbstractPerson(demoData, personInAusbildung, "PiA", gesuchSuffix);
        anonymizeAdresse(gesuch, personInAusbildung.getAdresse());
    }

    private void anonymizePartner(DemoData demoData, Gesuch gesuch) {
        final var partner = gesuch.getLatestGesuchTranche().getGesuchFormular().getPartner();
        if (Objects.isNull(partner)) {
            return;
        }
        final var gesuchSuffix = getLastGesuchNummerPart(gesuch);
        anonymizeAbstractPerson(demoData, partner, "Partner", gesuchSuffix);
        anonymizeAdresse(gesuch, partner.getAdresse());
    }

    private void anonymizeEltern(DemoData demoData, Gesuch gesuch) {
        final var elterns = gesuch.getLatestGesuchTranche().getGesuchFormular().getElterns();
        final var gesuchSuffix = getLastGesuchNummerPart(gesuch);
        elterns.forEach(eltern -> {
            anonymizeAbstractPerson(demoData, eltern, eltern.getElternTyp().name(), gesuchSuffix);
            anonymizeAdresse(gesuch, eltern.getAdresse());
        });
    }

    private void anonymizeGeschwisters(DemoData demoData, Gesuch gesuch) {
        final var geschwisters = gesuch.getLatestGesuchTranche().getGesuchFormular().getGeschwisters();
        final var gesuchSuffix = getLastGesuchNummerPart(gesuch);
        var count = 0;
        for (var geschwister : geschwisters) {
            anonymizeAbstractPerson(demoData, geschwister, "Geschwister-%d".formatted(++count), gesuchSuffix);
        }
    }

    private void anonymizeKinds(DemoData demoData, Gesuch gesuch) {
        final var kinds = gesuch.getLatestGesuchTranche().getGesuchFormular().getKinds();
        final var gesuchSuffix = getLastGesuchNummerPart(gesuch);
        var count = 0;
        for (var geschwister : kinds) {
            anonymizeAbstractPerson(demoData, geschwister, "Kind-%d".formatted(++count), gesuchSuffix);
        }
    }

    private void anonymizeAdresse(Gesuch gesuch, Adresse adresse) {
        final var gesuchSuffix = getLastGesuchNummerPart(gesuch);
        adresse.setStrasse("Strasse");
        adresse.setHausnummer(gesuchSuffix);
    }

    private String getLastGesuchNummerPart(Gesuch gesuch) {
        final var gesuchNummer = gesuch.getGesuchNummer();
        return gesuchNummer.substring(gesuchNummer.lastIndexOf('.') + 1);
    }
}
