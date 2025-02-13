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

package ch.dvbern.stip.api.gesuch.entity;

import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static ch.dvbern.stip.api.util.TestConstants.GUELTIGKEIT_PERIODE_23_24;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GesuchEinnahmenKostenSteuerjahrNonNullValueTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());

    private static UUID gesuchId;
    private static GesuchDtoSpec gesuch;
    private static UUID trancheId;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void testCreateEndpoint() {
        final var gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        gesuchId = gesuch.getId();
        trancheId = gesuch.getGesuchTrancheToWorkWith().getId();
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void gesuchTrancheCreated() {
        gesuch = gesuchApiSpec.getGesuchGS()
            .gesuchTrancheIdPath(trancheId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec.class);
        assertThat(gesuch.getGesuchTrancheToWorkWith(), notNullValue());
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGueltigAb(), is(GUELTIGKEIT_PERIODE_23_24.getGueltigAb()));
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGueltigBis(), is(GUELTIGKEIT_PERIODE_23_24.getGueltigBis()));
    }

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void testUpdateGesuchEinnahmenKostenSteuerjahrNonNullValue() {
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecEinnahmenKosten();
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setSteuerjahr(2020);
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());

        gesuchApiSpec.updateGesuch()
            .gesuchIdPath(gesuchId)
            .body(gesuchUpdateDTO)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());
        gesuch = gesuchApiSpec.getGesuchGS()
            .gesuchTrancheIdPath(trancheId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec.class);
        Integer vorjahrGesuchsjahr = gesuch.getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() - 1;
        assertThat(
            gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().getSteuerjahr(),
            is(vorjahrGesuchsjahr)
        );
    }

    @Test
    @TestAsGesuchsteller
    @Order(5)
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuchId);
    }
}
