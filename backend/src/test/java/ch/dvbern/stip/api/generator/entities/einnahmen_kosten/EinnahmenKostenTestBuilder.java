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

package ch.dvbern.stip.api.generator.entities.einnahmen_kosten;

import java.time.LocalDate;

import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKostenBuilder;
import ch.dvbern.stip.api.generator.AbstractTestBuilder;
import ch.dvbern.stip.api.util.TestConstants;

public final class EinnahmenKostenTestBuilder extends AbstractTestBuilder<EinnahmenKosten, EinnahmenKostenTestBuilder> {
    EinnahmenKostenTestBuilder(EinnahmenKosten entity, LocalDate referenceDate) {
        super(entity, referenceDate);
    }

    public static EinnahmenKostenTestBuilder empty(LocalDate referenceDate) {
        EinnahmenKosten einnahmenKosten = EinnahmenKostenBuilder.einnahmenKosten()
            .nettoerwerbseinkommen(7500)
            .fahrkosten(600)
            .wohnkosten(1000)
            .wgWohnend(false)
            .wgAnzahlPersonen(null)
            .alternativeWohnformWohnend(false)
            .unterhaltsbeitraege(null)
            .zulagen(null)
            .renten(null)
            .eoLeistungen(null)
            .ergaenzungsleistungen(null)
            .beitraege(null)
            .ausbildungskosten(500)
            .auswaertigeMittagessenProWoche(5)
            .verpflegungskosten(null)
            .veranlagungsStatus(TestConstants.VERANLAGUNGSSTATUS_EXAMPLE_VALUE)
            .steuerjahr(referenceDate.minusYears(2).getDayOfYear())
            .steuern(null)
            .vermoegen(null)
            .einnahmenBGSA(null)
            .taggelderAHVIV(null)
            .andereEinnahmen(null)
            .arbeitspensumProzent(100)
            .build();

        return new EinnahmenKostenTestBuilder(einnahmenKosten, referenceDate);
    }
}
