package ch.dvbern.stip.api.fall;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.dto.FallDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FallResourceTest {

    public final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void testFindFallEndpoint() {
        var fall = fallApiSpec.getFall().fallIdPath(TestConstants.FALL_TEST_ID).execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(FallDtoSpec.class);

        assertThat(fall, notNullValue());
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void testGetFallForBenutzer() {
        var fall = fallApiSpec.getFallForBenutzer()
            .benutzerIdPath(TestConstants.GESUCHSTELLER_TEST_ID)
            .execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(FallDtoSpec[].class);

        assertThat(fall, notNullValue());
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void testCreateFallForBenutzer() {
        fallApiSpec.createFall().benutzerIdPath(TestConstants.GESUCHSTELLER_2_TEST_ID).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.CREATED.getStatusCode());
    }
}
