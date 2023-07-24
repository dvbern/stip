package ch.dvbern.stip.test.gesuchsperiode;

import ch.dvbern.stip.generated.test.api.GesuchsperiodeApiSpec;
import ch.dvbern.stip.generated.test.dto.GesuchsperiodeCreateDtoSpec;
import ch.dvbern.stip.generated.test.dto.GesuchsperiodeDtoSpec;
import ch.dvbern.stip.test.util.RequestSpecUtil;
import ch.dvbern.stip.test.util.TestDatabaseEnvironment;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GesuchsperiodeResourceTest {

    private final GesuchsperiodeApiSpec api = GesuchsperiodeApiSpec.gesuchsperiode(RequestSpecUtil.quarkusSpec());

    @Test
    @Order(1)
    void test_create_gesuchsperiode() {
        var newPeriode = new GesuchsperiodeCreateDtoSpec();
        newPeriode.setAufschaltdatum(LocalDate.of(2023, 1, 1));
        newPeriode.setEinreichfrist(LocalDate.of(2023, 12, 1));
        newPeriode.setGueltigAb(LocalDate.of(2023, 1, 1));
        newPeriode.setGueltigBis(LocalDate.of(2023, 12, 1));

        api.createGesuchsperiode().body(newPeriode)
                .execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }


    @Test
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
