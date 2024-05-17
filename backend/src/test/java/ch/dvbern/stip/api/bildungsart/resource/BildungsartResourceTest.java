package ch.dvbern.stip.api.bildungsart.resource;

import java.util.Arrays;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.generated.api.BildungsartApiSpec;
import ch.dvbern.stip.generated.dto.BildungsartDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@QuarkusTestResource(TestDatabaseEnvironment.class)
class BildungsartResourceTest {
    private final BildungsartApiSpec api = BildungsartApiSpec.bildungsart(RequestSpecUtil.quarkusSpec());

    @Test
    @TestAsGesuchsteller
    void testGetBildungsarten() {
        final var bildungsarten = api.getBildungsarten()
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(BildungsartDtoSpec[].class);

        assertThat(bildungsarten.length, is(greaterThan(0)));
        assertThat(
            Arrays.stream(bildungsarten).anyMatch(x -> x.getId().equals(TestConstants.TEST_BILDUNGSART_ID)),
            is(true)
        );
    }
}
