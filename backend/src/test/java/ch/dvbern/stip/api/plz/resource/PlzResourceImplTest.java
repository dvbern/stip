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

package ch.dvbern.stip.api.plz.resource;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.generated.api.PlzResource;
import io.quarkus.security.ForbiddenException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class PlzResourceImplTest {
    @Inject
    PlzResource plzResource;

    @TestAsGesuchsteller
    @Test
    void getPlzAsGS() {
        assertEquals(HttpStatus.SC_OK, plzResource.getPlz().getStatus());
    }

    @TestAsSachbearbeiter
    @Test
    void getPlzAsSB() {
        assertThrows(ForbiddenException.class, () -> plzResource.getPlz());

    }
}
