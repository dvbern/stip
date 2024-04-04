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
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GesuchsperiodeResourceTest {

    private final GesuchsperiodeApiSpec api = GesuchsperiodeApiSpec.gesuchsperiode(RequestSpecUtil.quarkusSpec());

    @Test
    @TestAsAdmin
    @Order(1)
    void test_create_gesuchsperiode() {
        GesuchsperiodeCreateDtoSpec newPeriode;
		try {
			newPeriode = GesuchsperiodeTestSpecGenerator.gesuchsperiodeCreateDtoSpec();
		} catch (InvocationTargetException | IllegalAccessException e) {
			assertThat(String.format("Failed to create periode: %s", e), false, is(true));
            return;
		}

		newPeriode.setAufschaltdatum(LocalDate.of(2023, 1, 1));
        newPeriode.setEinreichfrist(LocalDate.of(2023, 12, 1));
        newPeriode.setGueltigAb(LocalDate.of(2023, 1, 1));
        newPeriode.setGueltigBis(LocalDate.of(2023, 12, 1));
        newPeriode.setFiskaljahr("2024");
        newPeriode.setGesuchsjahr("2024");

        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        api.createGesuchsperiode().body(newPeriode)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.CREATED.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void test_get_all() {
        var gesuchperioden = api.getGesuchsperioden()
            .execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchsperiodeDtoSpec[].class);

        assertThat(gesuchperioden.length, is(2));
    }
}
