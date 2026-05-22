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

package ch.dvbern.stip.api.generator.entities.steuerdaten;

import java.time.LocalDate;

import ch.dvbern.stip.api.generator.AbstractTestBuilder;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.entity.SteuerdatenBuilder;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;

public final class SteuerdatenTestBuilder extends AbstractTestBuilder<Steuerdaten, SteuerdatenTestBuilder> {
    SteuerdatenTestBuilder(Steuerdaten entity, LocalDate referenceDate) {
        super(entity, referenceDate);
    }

    public static SteuerdatenTestBuilder empty(LocalDate referenceDate) {
        Steuerdaten steuerdaten = SteuerdatenBuilder.steuerdaten()
            .steuerdatenTyp(SteuerdatenTyp.FAMILIE)
            .totalEinkuenfte(0)
            .eigenmietwert(0)
            .isArbeitsverhaeltnisSelbstaendig(false)
            .saeule3a(0)
            .saeule2(0)
            .vermoegen(0)
            .steuernKantonGemeinde(0)
            .steuernBund(0)
            .fahrkosten(0)
            .fahrkostenPartner(0)
            .verpflegung(0)
            .verpflegungPartner(0)
            .steuerjahr(referenceDate.minusYears(2).getYear())
            .veranlagungsStatus("23")
            .build();

        return new SteuerdatenTestBuilder(steuerdaten, referenceDate);
    }
}
