package ch.dvbern.stip.api.gesuchsperiode;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

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
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
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

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class GesuchsperiodeResourceTest {

    private final GesuchsperiodeApiSpec api = GesuchsperiodeApiSpec.gesuchsperiode(RequestSpecUtil.quarkusSpec());
    private GesuchsperiodeDtoSpec gesuchsperiode;

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

        newPeriode.setAufschaltdatum(LocalDate.now().with(firstDayOfYear()));
        newPeriode.setEinreichfrist(LocalDate.now().with(lastDayOfYear()));
        newPeriode.setGueltigAb(LocalDate.now().with(firstDayOfYear()));
        newPeriode.setGueltigBis(LocalDate.now().with(lastDayOfYear()));
        newPeriode.setFiskaljahr(LocalDate.now().getYear());
        newPeriode.setGesuchsjahr(LocalDate.now().getYear());

        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        gesuchsperiode = api.createGesuchsperiode().body(newPeriode)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(GesuchsperiodeDtoSpec.class);
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

        // This makes the Gesuchsperiode readonly
        updateDto.setGesuchsperiodeStart(LocalDate.now().plusMonths(1));

        final var updated = api.updateGesuchsperiode()
            .gesuchsperiodeIdPath(gesuchsperiode.getId())
            .body(updateDto)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(GesuchsperiodeDtoSpec.class);

        assertThat(updated.getBezeichnungDe(), is(updateBezeichnungDe));
        gesuchsperiode = updated;
    }

    @Test
    @TestAsAdmin
    @Order(6)
    void updateReadonlyTest() {
        final GesuchsperiodeUpdateDtoSpec updateDto;
        try {
            updateDto = GesuchsperiodeTestSpecGenerator.gesuchsperiodeUpdateDtoSpec();
        } catch (InvocationTargetException | IllegalAccessException e) {
            assertThat(String.format("Failed to create periode: %s", e), false, is(true));
            return;
        }

        final var updateBezeichnungDe = gesuchsperiode.getBezeichnungDe() + "UPDATED";
        updateDto.setBezeichnungDe(updateBezeichnungDe);

        api.updateGesuchsperiode()
            .gesuchsperiodeIdPath(gesuchsperiode.getId())
            .body(updateDto)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }
}
