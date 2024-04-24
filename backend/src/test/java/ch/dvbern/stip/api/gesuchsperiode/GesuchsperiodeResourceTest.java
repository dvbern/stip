package ch.dvbern.stip.api.gesuchsperiode;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.generator.api.GesuchsperiodeTestSpecGenerator;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.generated.api.GesuchsperiodeApiSpec;
import ch.dvbern.stip.generated.dto.GesuchsperiodeCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchsperiodeDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchsperiodeUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchsperiodeWithDatenDtoSpec;
import ch.dvbern.stip.generated.dto.GueltigkeitStatusDtoSpec;
import ch.dvbern.stip.generated.dto.NullableGesuchsperiodeWithDatenDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
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
import static org.hamcrest.Matchers.nullValue;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class GesuchsperiodeResourceTest {
    private static final UUID GESUCHSJAHR_ID = UUID.fromString("9596bb3e-10ea-4493-8aed-a6ef510f806b");

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
        newPeriode.setAufschaltterminStopp(LocalDate.now().with(lastDayOfYear()));
        newPeriode.setEinreichfrist(LocalDate.now().with(lastDayOfYear()));
        newPeriode.setGesuchsperiodeStart(LocalDate.now().with(firstDayOfYear()));
        newPeriode.setGesuchsperiodeStopp(LocalDate.now().with(lastDayOfYear()));
        newPeriode.setFiskaljahr(LocalDate.now().getYear());
        newPeriode.setGesuchsjahr(GESUCHSJAHR_ID);

        gesuchsperiode = api.createGesuchsperiode().body(newPeriode)
            .execute(ResponseBody::prettyPeek)
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
            .execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchsperiodeDtoSpec[].class);

        assertThat(gesuchsperioden.length, is(2));
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void getAktiveTest() {
        var gesuchsperioden = api.getAktiveGesuchsperioden()
            .execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchsperiodeDtoSpec[].class);

        assertThat(gesuchsperioden.length, is(2));
    }

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void getByIdTest() {
        var got = api.getGesuchsperiode()
            .gesuchsperiodeIdPath(gesuchsperiode.getId())
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(GesuchsperiodeWithDatenDtoSpec.class);

        assertThat(got.getBezeichnungDe(), is(gesuchsperiode.getBezeichnungDe()));
    }

    @Test
    @TestAsGesuchsteller
    @Order(5)
    void getLatestWithNoneTest() {
        final var got = api.getLatestPublished()
            .execute(ResponseBody::prettyPeek)
            .then()
            .and()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(NullableGesuchsperiodeWithDatenDtoSpec.class);

        assertThat(got.getValue(), is(nullValue()));
    }

    @Test
    @TestAsAdmin
    @Order(6)
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
        updateDto.setGesuchsjahr(GESUCHSJAHR_ID);

        final var updated = api.updateGesuchsperiode()
            .gesuchsperiodeIdPath(gesuchsperiode.getId())
            .body(updateDto)
            .execute(ResponseBody::prettyPeek)
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
    @Order(7)
    void publishTest() {
        final var updated = api.publishGesuchsperiode()
            .gesuchsperiodeIdPath(gesuchsperiode.getId())
            .execute(ResponseBody::prettyPeek)
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
    @Order(8)
    void getLatestTest() {
        final var got = api.getLatestPublished()
            .execute(ResponseBody::prettyPeek)
            .then()
            .and()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(NullableGesuchsperiodeWithDatenDtoSpec.class);

        assertThat(got.getValue().getId(), is(gesuchsperiode.getId()));
    }

    @Test
    @TestAsAdmin
    @Order(9)
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
        updateDto.setGesuchsjahr(GESUCHSJAHR_ID);

        api.updateGesuchsperiode()
            .gesuchsperiodeIdPath(gesuchsperiode.getId())
            .body(updateDto)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    @Test
    @TestAsAdmin
    @Order(10)
    void readonlyDeleteFailsTest() {
        api.deleteGesuchsperiode()
            .gesuchsperiodeIdPath(gesuchsperiode.getId())
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());

        api.getGesuchsperiode()
            .gesuchsperiodeIdPath(gesuchsperiode.getId())
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }
}
