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

package ch.dvbern.stip.api.common.service.seeding;

import java.util.List;

import ch.dvbern.stip.api.land.service.LandService;
import ch.dvbern.stip.api.land.type.WellKnownLand;
import ch.dvbern.stip.api.util.TestConstants;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class LandTestSeeding extends Seeder {
    private final LandService landService;
    private final LandSeeding landSeeding;

    @Override
    protected void seed() {
        landSeeding.seed();

        final var switzerland = landService.getLandByBfsCode(WellKnownLand.CHE.getLaendercodeBfs()).orElseThrow();
        TestConstants.TEST_LAND_SCHWEIZ_ID = switzerland.getId();

        final var iran = landService.getLandByBfsCode(WellKnownLand.IRN.getLaendercodeBfs()).orElseThrow();
        TestConstants.TEST_LAND_NON_EU_EFTA_ID = iran.getId();

        final var stateless = landService.getLandByBfsCode(WellKnownLand.STATELESS.getLaendercodeBfs()).orElseThrow();
        TestConstants.TEST_LAND_STATELESS_ID = stateless.getId();
    }

    protected List<String> getProfiles() {
        return List.of("test");
    }
}
