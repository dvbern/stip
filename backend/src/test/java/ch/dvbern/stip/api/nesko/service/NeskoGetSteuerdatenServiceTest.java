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

package ch.dvbern.stip.api.nesko.service;

import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.xml.ws.WebServiceException;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import static ch.dvbern.stip.api.util.TestConstants.AHV_NUMMER_VALID_MUTTER;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.oneOf;

@QuarkusTest
class NeskoGetSteuerdatenServiceTest {
    @Inject
    NeskoGetSteuerdatenService neskoGetSteuerdatenService;

    @Test
    @TestAsSachbearbeiter
    void testNeskoGetSteuerdatenInvalidTokenFail() {
        try {
            neskoGetSteuerdatenService.getSteuerdatenResponse("asd", AHV_NUMMER_VALID_MUTTER, 2020);
        } catch (Exception e) {
            MatcherAssert.assertThat(e.getClass(), is(oneOf(UnauthorizedException.class, WebServiceException.class)));
        }
    }
}
