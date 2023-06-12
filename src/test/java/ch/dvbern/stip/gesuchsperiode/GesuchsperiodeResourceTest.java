package ch.dvbern.stip.gesuchsperiode;

import ch.dvbern.stip.utils.TestDatabaseEnvironment;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
public class GesuchsperiodeResourceTest {


    @Test
    void testGetAllEndpoint() {
        given()
                .when()
                .get("/api/v1/gesuchsperiode/all")
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(is(not(empty())));
    }
}
