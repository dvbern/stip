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

package ch.dvbern.stip.api.gesuchsperiode;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.generator.api.GesuchsperiodeTestSpecGenerator;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.GesuchsperiodeApiSpec;
import ch.dvbern.stip.generated.dto.GesuchsperiodeCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchsperiodeDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchsperiodeUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchsperiodeWithDatenDtoSpec;
import ch.dvbern.stip.generated.dto.GueltigkeitStatusDtoSpec;
import ch.dvbern.stip.generated.dto.NullableGesuchsperiodeWithDatenDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class GesuchsperiodeResourceTest {
    private final GesuchsperiodeApiSpec api = GesuchsperiodeApiSpec.gesuchsperiode(RequestSpecUtil.quarkusSpec());
    private GesuchsperiodeWithDatenDtoSpec gesuchsperiode;

    @Test
    @TestAsAdmin
    @Order(1)
    void createTest() {
        GesuchsperiodeCreateDtoSpec newPeriode;
        try {
            newPeriode = GesuchsperiodeTestSpecGenerator.gesuchsperiodeCreateDtoSpec();
        } catch (InvocationTargetException | IllegalAccessException e) {
            assertThat(String.format("Failed to create periode: %s", e), false, is(true));
            return;
        }

        newPeriode.setAufschaltterminStart(LocalDate.now().with(firstDayOfYear()));
        newPeriode.setEinreichfrist(LocalDate.now().with(lastDayOfYear()));
        newPeriode.setGesuchsperiodeStart(LocalDate.now().with(firstDayOfYear()));
        newPeriode.setGesuchsperiodeStopp(LocalDate.now().with(lastDayOfYear()));
        newPeriode.setFiskaljahr(LocalDate.now().getYear());
        newPeriode.setGesuchsjahrId(TestConstants.TEST_GESUCHSJAHR_ID);

        gesuchsperiode = api.createGesuchsperiode()
            .body(newPeriode)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(GesuchsperiodeWithDatenDtoSpec.class);
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void getAllTest() {
        var gesuchsperioden = api.getGesuchsperioden()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(GesuchsperiodeDtoSpec[].class);

        assertThat(gesuchsperioden.length, is(4));
    }

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void getByIdTest() {
        var got = api.getGesuchsperiode()
            .gesuchsperiodeIdPath(gesuchsperiode.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(GesuchsperiodeWithDatenDtoSpec.class);

        assertThat(got.getBezeichnungDe(), is(gesuchsperiode.getBezeichnungDe()));
    }

    @Test
    @TestAsAdmin
    @Order(5)
    void updateTest() {
        final GesuchsperiodeUpdateDtoSpec updateDto;
        try {
            updateDto = GesuchsperiodeTestSpecGenerator.gesuchsperiodeUpdateDtoSpec();
        } catch (InvocationTargetException | IllegalAccessException e) {
            assertThat(String.format("Failed to create periode: %s", e), false, is(true));
            return;
        }

        final var updateBezeichnungDe = gesuchsperiode.getBezeichnungDe() + "UPDATED";
        updateDto.setBezeichnungDe(updateBezeichnungDe);
        updateDto.setGesuchsjahrId(TestConstants.TEST_GESUCHSJAHR_ID);

        final var updated = api.updateGesuchsperiode()
            .gesuchsperiodeIdPath(gesuchsperiode.getId())
            .body(updateDto)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(GesuchsperiodeWithDatenDtoSpec.class);

        assertThat(updated.getBezeichnungDe(), is(updateBezeichnungDe));
        gesuchsperiode = updated;
    }

    @Test
    @TestAsAdmin
    @Order(6)
    void publishTest() {
        final var updated = api.publishGesuchsperiode()
            .gesuchsperiodeIdPath(gesuchsperiode.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(GesuchsperiodeWithDatenDtoSpec.class);

        assertThat(updated.getGueltigkeitStatus(), is(GueltigkeitStatusDtoSpec.PUBLIZIERT));
        gesuchsperiode = updated;
    }

    @Test
    @TestAsGesuchsteller
    @Order(7)
    void getLatestTest() {
        final var got = api.getLatest()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .and()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(NullableGesuchsperiodeWithDatenDtoSpec.class);

        assertThat(got.getValue().getId(), is(gesuchsperiode.getId()));
    }

    @Test
    @TestAsAdmin
    @Order(8)
    void readonlyUpdateFailsTest() {
        final GesuchsperiodeUpdateDtoSpec updateDto;
        try {
            updateDto = GesuchsperiodeTestSpecGenerator.gesuchsperiodeUpdateDtoSpec();
        } catch (InvocationTargetException | IllegalAccessException e) {
            assertThat(String.format("Failed to create periode: %s", e), false, is(true));
            return;
        }

        final var updateBezeichnungDe = gesuchsperiode.getBezeichnungDe() + "UPDATED";
        updateDto.setBezeichnungDe(updateBezeichnungDe);
        updateDto.setGesuchsjahrId(TestConstants.TEST_GESUCHSJAHR_ID);

        api.updateGesuchsperiode()
            .gesuchsperiodeIdPath(gesuchsperiode.getId())
            .body(updateDto)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @TestAsAdmin
    @Order(9)
    void readonlyDeleteFailsTest() {
        api.deleteGesuchsperiode()
            .gesuchsperiodeIdPath(gesuchsperiode.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.FORBIDDEN.getStatusCode());

        api.getGesuchsperiode()
            .gesuchsperiodeIdPath(gesuchsperiode.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }
}
