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

package ch.dvbern.stip.api.generator.entities.zahlungsverbindung;

import java.time.LocalDate;

import ch.dvbern.stip.api.generator.AbstractTestBuilder;
import ch.dvbern.stip.api.generator.entities.adresse.AdresseTestBuilder;
import ch.dvbern.stip.api.zahlungsverbindung.entity.Zahlungsverbindung;
import ch.dvbern.stip.api.zahlungsverbindung.entity.ZahlungsverbindungBuilder;

public final class ZahlungsverbindungTestBuilder
extends AbstractTestBuilder<Zahlungsverbindung, ZahlungsverbindungTestBuilder> {
    private static final String swissIban = "CH93 0076 2011 6238 5295 7";

    ZahlungsverbindungTestBuilder(Zahlungsverbindung entity, LocalDate referenceDate) {
        super(entity, referenceDate);
    }

    public static ZahlungsverbindungTestBuilder empty(LocalDate referenceDate) {
        Zahlungsverbindung zahlungsverbindung = ZahlungsverbindungBuilder.zahlungsverbindung()
            .vorname("a")
            .nachname("b")
            .adresse(null)
            .iban(swissIban)
            .institution("Test Institute")
            .build();

        return new ZahlungsverbindungTestBuilder(zahlungsverbindung, referenceDate);
    }

    public static ZahlungsverbindungTestBuilder standardDirectDeps(LocalDate referenceDate) {
        Zahlungsverbindung zahlungsverbindung = ZahlungsverbindungBuilder.zahlungsverbindung()
            .vorname("a")
            .nachname("b")
            .adresse(AdresseTestBuilder.empty(referenceDate).build())
            .iban(swissIban)
            .institution("Test Institute")
            .build();

        return new ZahlungsverbindungTestBuilder(zahlungsverbindung, referenceDate);
    }

    public static ZahlungsverbindungTestBuilder standardNestedDeps(LocalDate referenceDate) {
        Zahlungsverbindung zahlungsverbindung = ZahlungsverbindungBuilder.zahlungsverbindung()
            .vorname("a")
            .nachname("b")
            .adresse(AdresseTestBuilder.standardDirectDeps(referenceDate).build())
            .iban(swissIban)
            .institution("Test Institute")
            .build();

        return new ZahlungsverbindungTestBuilder(zahlungsverbindung, referenceDate);
    }
}
