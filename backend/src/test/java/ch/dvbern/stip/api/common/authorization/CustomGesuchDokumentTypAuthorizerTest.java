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

package ch.dvbern.stip.api.common.authorization;

import java.util.UUID;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import io.quarkus.security.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CustomGesuchDokumentTypAuthorizerTest {
    private CustomGesuchDokumentTypAuthorizer authorizer;
    private GesuchRepository gesuchRepository;
    private Gesuch gesuch;

    @BeforeEach
    void setUp() {
        gesuch = new Gesuch();
        gesuchRepository = Mockito.mock(GesuchRepository.class);
        authorizer = new CustomGesuchDokumentTypAuthorizer(gesuchRepository);
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
    }

    @Test
    void canDeleteShouldFail() {
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        assertThrows(ForbiddenException.class, () -> {
            authorizer.canDelete(UUID.randomUUID());
        });
    }

    @Test
    void canDeleteShouldSuccess() {
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        assertDoesNotThrow(() -> {
            authorizer.canDelete(UUID.randomUUID());
        });
    }
}
