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

package ch.dvbern.stip.api.ausbildung.service;

import java.util.UUID;

import ch.dvbern.stip.api.fall.service.FallService;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import jakarta.ws.rs.BadRequestException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AusbildungServiceTest {
    @Test
    void createAusbildungWithAlreadyAktiveFails() {
        // Arrange
        final var fallId = UUID.randomUUID();
        final var createDto = new AusbildungUpdateDto().fallId(fallId);

        final var service = getAusbildungService(mockFallService(true));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> service.createAusbildung(createDto));
    }

    private AusbildungService getAusbildungService(final FallService fallService) {
        return new AusbildungService(
            null,
            null,
            null,
            null,
            null,
            null,
            fallService,
            null
        );
    }

    private FallService mockFallService(final boolean toReturn) {
        final var fallService = Mockito.mock(FallService.class);
        Mockito.when(fallService.hasAktiveAusbildung(Mockito.any())).thenReturn(toReturn);

        return fallService;
    }
}
