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

package ch.dvbern.stip.api.gesuchsjahr.resource;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.generator.api.model.GesuchsjahrTestSpecGenerator;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.generated.api.GesuchsjahrApiSpec;
import ch.dvbern.stip.generated.dto.GesuchsjahrDtoSpec;
import ch.dvbern.stip.generated.dto.GueltigkeitStatusDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(Lifecycle.PER_CLASS)
class GesuchsjahrResourceTest {
    private final GesuchsjahrApiSpec apiSpec = GesuchsjahrApiSpec.gesuchsjahr(RequestSpecUtil.quarkusSpec());
    private GesuchsjahrDtoSpec gesuchsjahr;

    @Test
    @Order(1)
    @TestAsSachbearbeiter
    void createAsNotAdminFailsTest() {
        final var createDto = GesuchsjahrTestSpecGenerator.gesuchsjahrCreateDtoSpec;
        apiSpec.createGesuchsjahr()
            .body(createDto)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @Order(2)
    @TestAsAdmin
    void createTest() {
        final var createDto = GesuchsjahrTestSpecGenerator.gesuchsjahrCreateDtoSpec;
        gesuchsjahr = apiSpec.createGesuchsjahr()
            .body(createDto)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(GesuchsjahrDtoSpec.class);
    }

    @Test
    @Order(3)
    @TestAsAdmin
    void readTest() {
        read();
    }

    @Test
    @Order(3)
    @TestAsGesuchsteller
    void readAsGsTest() {
        read();
    }

    private void read() {
        final var read = apiSpec.getGesuchsjahr()
            .gesuchsjahrIdPath(gesuchsjahr.getId())
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(GesuchsjahrDtoSpec.class);

        assertThat(read.getId(), is(gesuchsjahr.getId()));
    }

    @Test
    @Order(4)
    @TestAsAdmin
    void readAllTest() {
        final var read = apiSpec.getGesuchsjahre()
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(GesuchsjahrDtoSpec[].class);

        assertThat(read.length, is(greaterThan(0)));
    }

    @Test
    @Order(5)
    @TestAsAdmin
    void updateTest() {
        final var updateDto = GesuchsjahrTestSpecGenerator.gesuchsjahrUpdateDtoSpec;
        final var updatedBezeichnungDe = gesuchsjahr.getBezeichnungDe() + "UPDATED";
        updateDto.setBezeichnungDe(updatedBezeichnungDe);
        final var updated = apiSpec.updateGesuchsjahr()
            .gesuchsjahrIdPath(gesuchsjahr.getId())
            .body(updateDto)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(GesuchsjahrDtoSpec.class);

        assertThat(updated.getId(), is(gesuchsjahr.getId()));
        assertThat(updated.getBezeichnungDe(), is(updatedBezeichnungDe));
    }

    @Test
    @Order(6)
    @TestAsAdmin
    void publishTest() {
        final var published = apiSpec.publishGesuchsjahr()
            .gesuchsjahrIdPath(gesuchsjahr.getId())
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(GesuchsjahrDtoSpec.class);

        assertThat(published.getGueltigkeitStatus(), is(GueltigkeitStatusDtoSpec.PUBLIZIERT));
    }

    @Test
    @Order(7)
    @TestAsAdmin
    void readonlyUpdateFailsTest() {
        final var updateDto = GesuchsjahrTestSpecGenerator.gesuchsjahrUpdateDtoSpec;
        final var updatedBezeichnungDe = gesuchsjahr.getBezeichnungDe() + "UPDATED";
        updateDto.setBezeichnungDe(updatedBezeichnungDe);
        apiSpec.updateGesuchsjahr()
            .gesuchsjahrIdPath(gesuchsjahr.getId())
            .body(updateDto)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    @Test
    @Order(8)
    @TestAsAdmin
    void readonlyDeleteFailsTest() {
        apiSpec.deleteGesuchsjahr()
            .gesuchsjahrIdPath(gesuchsjahr.getId())
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());

        apiSpec.getGesuchsjahr()
            .gesuchsjahrIdPath(gesuchsjahr.getId())
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }
}
