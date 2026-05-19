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

package ch.dvbern.stip.api.generator.entities.auszahlung;

import java.time.LocalDate;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.entity.AuszahlungBuilder;
import ch.dvbern.stip.api.generator.AbstractTestBuilder;
import ch.dvbern.stip.api.generator.entities.zahlungsverbindung.ZahlungsverbindungTestBuilder;

public final class AuszahlungTestBuilder extends AbstractTestBuilder<Auszahlung, AuszahlungTestBuilder> {
    AuszahlungTestBuilder(Auszahlung entity, LocalDate referenceDate) {
        super(entity, referenceDate);
    }

    public static AuszahlungTestBuilder empty(LocalDate referenceDate) {
        Auszahlung auszahlung = AuszahlungBuilder.auszahlung()
            .zahlungsverbindung(null)
            .auszahlungAnSozialdienst(false)
            .sapBusinessPartnerId(null)
            .buchhaltung(null)
            .build();

        return new AuszahlungTestBuilder(auszahlung, referenceDate);
    }

    public static AuszahlungTestBuilder standardDirectDeps(LocalDate referenceDate) {
        Auszahlung auszahlung = AuszahlungBuilder.auszahlung()
            .zahlungsverbindung(ZahlungsverbindungTestBuilder.empty(referenceDate).build())
            .auszahlungAnSozialdienst(false)
            .sapBusinessPartnerId(null)
            .buchhaltung(null)
            .build();

        return new AuszahlungTestBuilder(auszahlung, referenceDate);
    }

    public static AuszahlungTestBuilder standardNestDeps(LocalDate referenceDate) {
        Auszahlung auszahlung = AuszahlungBuilder.auszahlung()
            .zahlungsverbindung(ZahlungsverbindungTestBuilder.standardDirectDeps(referenceDate).build())
            .auszahlungAnSozialdienst(false)
            .sapBusinessPartnerId(null)
            .buchhaltung(null)
            .build();

        return new AuszahlungTestBuilder(auszahlung, referenceDate);
    }
}
