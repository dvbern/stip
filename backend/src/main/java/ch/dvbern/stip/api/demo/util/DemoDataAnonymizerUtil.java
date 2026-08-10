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

import java.security.SecureRandom;
import java.util.Objects;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.entity.AbstractPerson;
import ch.dvbern.stip.api.demo.entity.DemoData;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DemoDataAnonymizerUtil {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String CHARS =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

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
        zahlungsverbindung.setVorname("%s-Vorname".formatted(getRandomNamePrefix()));
        zahlungsverbindung.setNachname("%s-Nachname".formatted(getRandomNamePrefix()));
        anonymizeAdresse(gesuch, zahlungsverbindung.getAdresse());
    }

    private void anonymizeAbstractPerson(DemoData demoData, AbstractPerson person, String type) {
        person.setVorname("%s-%s".formatted(getRandomNamePrefix(), type));
        person.setNachname("%s-%s".formatted(getRandomNamePrefix(), demoData.getTestFall()));
    }

    private void anonymizePersonInAusbildung(DemoData demoData, Gesuch gesuch) {
        final var personInAusbildung = gesuch.getLatestGesuchTranche().getGesuchFormular().getPersonInAusbildung();
        anonymizeAbstractPerson(demoData, personInAusbildung, "PiA");
        anonymizeAdresse(gesuch, personInAusbildung.getAdresse());
    }

    private void anonymizePartner(DemoData demoData, Gesuch gesuch) {
        final var partner = gesuch.getLatestGesuchTranche().getGesuchFormular().getPartner();
        if (Objects.isNull(partner)) {
            return;
        }
        anonymizeAbstractPerson(demoData, partner, "Partner");
        anonymizeAdresse(gesuch, partner.getAdresse());
    }

    private void anonymizeEltern(DemoData demoData, Gesuch gesuch) {
        final var elterns = gesuch.getLatestGesuchTranche().getGesuchFormular().getElterns();
        elterns.forEach(eltern -> {
            anonymizeAbstractPerson(demoData, eltern, eltern.getElternTyp().name());
            anonymizeAdresse(gesuch, eltern.getAdresse());
        });
    }

    private void anonymizeGeschwisters(DemoData demoData, Gesuch gesuch) {
        final var geschwisters = gesuch.getLatestGesuchTranche().getGesuchFormular().getGeschwisters();
        var count = 0;
        for (var geschwister : geschwisters) {
            anonymizeAbstractPerson(demoData, geschwister, "Geschwister-%d".formatted(++count));
        }
    }

    private void anonymizeKinds(DemoData demoData, Gesuch gesuch) {
        final var kinds = gesuch.getLatestGesuchTranche().getGesuchFormular().getKinds();
        var count = 0;
        for (var geschwister : kinds) {
            anonymizeAbstractPerson(demoData, geschwister, "Kind-%d".formatted(++count));
        }
    }

    private void anonymizeAdresse(Gesuch gesuch, Adresse adresse) {
        adresse.setStrasse("Strasse");
        adresse.setHausnummer(getLastGesuchNummerPart(gesuch));
    }

    private String getLastGesuchNummerPart(Gesuch gesuch) {
        final var gesuchNummer = gesuch.getGesuchNummer();
        return gesuchNummer.substring(gesuchNummer.lastIndexOf('.') + 1);
    }

    public String getRandomNamePrefix() {
        StringBuilder id = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int index = SECURE_RANDOM.nextInt(CHARS.length());
            id.append(CHARS.charAt(index));
        }

        return id.toString();
    }
}
