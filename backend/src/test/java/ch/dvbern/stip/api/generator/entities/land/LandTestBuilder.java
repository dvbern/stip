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

package ch.dvbern.stip.api.generator.entities.land;

import java.time.LocalDate;

import ch.dvbern.stip.api.generator.AbstractTestBuilder;
import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.land.entity.LandBuilder;

public final class LandTestBuilder extends AbstractTestBuilder<Land, LandTestBuilder> {
    LandTestBuilder(Land entity, LocalDate referenceDate) {
        super(entity, referenceDate);
    }

    public static LandTestBuilder swiss(LocalDate referenceDate) {
        Land land = LandBuilder.land()
            .isEuEfta(true)
            .laendercodeBfs("8100")
            .deKurzform("Schweiz")
            .frKurzform("Suisse")
            .itKurzform("Svizzera")
            .enKurzform("Switzerland")
            .gueltig(true)
            .build();

        return new LandTestBuilder(land, referenceDate);
    }
}
