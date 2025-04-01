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

package ch.dvbern.stip.api.bildungskategorie.resource;

import java.util.Arrays;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.BildungskategorieApiSpec;
import ch.dvbern.stip.generated.dto.BildungskategorieDtoSpec;
import com.github.javaparser.utils.Log;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@Slf4j
class BildungskategorieResourceTest {
    private final BildungskategorieApiSpec api =
        BildungskategorieApiSpec.bildungskategorie(RequestSpecUtil.quarkusSpec());

    @Test
    @TestAsGesuchsteller
    void testGetBildungsarten() {
        final var bildungskategorien = api.getBildungskategorien()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(BildungskategorieDtoSpec[].class);

        assertThat(bildungskategorien.length, is(greaterThan(0)));
        assertThat(
            Arrays.stream(bildungskategorien)
                .anyMatch(
                    bildungskategorie -> bildungskategorie.getId().equals(TestConstants.TEST_BILDUNGSKATEGORIE_ID)
                ),
            is(true)
        );
        Log.info(bildungskategorien.toString());
    }
}
