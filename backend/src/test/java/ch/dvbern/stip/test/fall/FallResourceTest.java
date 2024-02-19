package ch.dvbern.stip.test.fall;

import ch.dvbern.stip.generated.test.api.FallApiSpec;
import ch.dvbern.stip.generated.test.dto.FallDtoSpec;
import ch.dvbern.stip.test.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.test.util.RequestSpecUtil;
import ch.dvbern.stip.test.util.TestConstants;
import ch.dvbern.stip.test.util.TestDatabaseEnvironment;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;

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
        var fall = fallApiSpec.getFallForBenutzer().benutzerIdPath(TestConstants.GESUCHSTELLER_TEST_ID).execute(ResponseBody::prettyPeek)
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
