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

package ch.dvbern.stip.integration.gemeindelookup.adapter.swisstopo.service;

import ch.dvbern.stip.integration.gemeindelookup.adapter.swisstopo.type.SwisstopoApiFindAddrResponse;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SwisstopoGeimeindeDataMapperTest {

    @Inject
    SwisstopoGeimeindeDataMapper swisstopoGeimeindeDataMapper;

    @Test
    void toGemeindeDataShouldMapBfsNummerAndName() {
        final var attributes = new SwisstopoApiFindAddrResponse.SwisstopoApiFindAddrResponseElementAttributes(
            "3000 Bern",
            351,
            "Bern"
        );

        final var result = swisstopoGeimeindeDataMapper.toGemeindeData(attributes);

        assertEquals(351, result.bfsNummer());
        assertEquals("Bern", result.name());
    }
}
