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

package ch.dvbern.stip.api.generator.entities.service;

import java.util.UUID;

import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.land.type.WellKnownLand;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LandGenerator {
    public Land initSwitzerland() {
        return initForWellKnownLand(WellKnownLand.CHE).setIsEuEfta(true);
    }

    public Land initGermany() {
        return initForWellKnownLand(WellKnownLand.DEU).setIsEuEfta(true);
    }

    public Land initIran() {
        return initForWellKnownLand(WellKnownLand.IRN).setIsEuEfta(false);
    }

    private Land initForWellKnownLand(final WellKnownLand wellKnownLand) {
        return (Land) new Land()
            .setDeKurzform("TEST Land")
            .setFrKurzform("TEST Land")
            .setItKurzform("TEST Land")
            .setEnKurzform("TEST Land")
            .setIso3code("TSL")
            .setGueltig(true)
            .setLaendercodeBfs(wellKnownLand.getLaendercodeBfs())
            .setId(UUID.randomUUID());
    }
}
