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

package ch.dvbern.stip.api.generator.entities.fall;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.entity.FallBuilder;
import ch.dvbern.stip.api.generator.AbstractTestBuilder;
import ch.dvbern.stip.api.generator.entities.auszahlung.AuszahlungTestBuilder;
import ch.dvbern.stip.api.generator.entities.benutzer.BenutzerTestBuilder;

public final class FallTestBuilder extends AbstractTestBuilder<Fall, FallTestBuilder> {

    FallTestBuilder(Fall entity, LocalDate referenceDate) {
        super(entity, referenceDate);
    }

    public static FallTestBuilder empty(LocalDate referenceDate) {
        Fall fall = FallBuilder.fall()
            .gesuchsteller(null)
            .ausbildungs(Set.of())
            .buchhaltungs(new ArrayList<>())
            .freiwilligDarlehens(new ArrayList<>())
            .delegierung(null)
            .auszahlung(null)
            .build();

        return new FallTestBuilder(fall, referenceDate);
    }

    public static FallTestBuilder standardDirectDeps(LocalDate referenceDate) {
        Fall fall = FallBuilder.fall()
            .gesuchsteller(BenutzerTestBuilder.empty(referenceDate).build())
            .ausbildungs(Set.of())
            .buchhaltungs(new ArrayList<>())
            .freiwilligDarlehens(new ArrayList<>())
            .delegierung(null)
            .auszahlung(AuszahlungTestBuilder.empty(referenceDate).build())
            .build();

        return new FallTestBuilder(fall, referenceDate);
    }

    public static FallTestBuilder standardNestedDeps(LocalDate referenceDate) {
        Fall fall = FallBuilder.fall()
            .gesuchsteller(BenutzerTestBuilder.empty(referenceDate).build())
            .ausbildungs(Set.of())
            .buchhaltungs(new ArrayList<>())
            .freiwilligDarlehens(new ArrayList<>())
            .delegierung(null)
            .auszahlung(AuszahlungTestBuilder.standardDirectDeps(referenceDate).build())
            .build();

        return new FallTestBuilder(fall, referenceDate);
    }
}
