package ch.dvbern.stip.api.bildungskategorie.resource;

import java.util.Arrays;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.generated.api.BildungskategorieApiSpec;
import ch.dvbern.stip.generated.dto.BildungskategorieDtoSpec;
import com.github.javaparser.utils.Log;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@Slf4j
class BildungskategorieResourceTest {
    private final BildungskategorieApiSpec api = BildungskategorieApiSpec.bildungskategorie(RequestSpecUtil.quarkusSpec());

    @Test
    @TestAsGesuchsteller
    void testGetBildungsarten() {
        final var bildungskategorien = api.getBildungskategorien()
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(BildungskategorieDtoSpec[].class);

        assertThat(bildungskategorien.length, is(greaterThan(0)));
        assertThat(
            Arrays.stream(bildungskategorien).anyMatch(
                bildungskategorie -> bildungskategorie.getId().equals(TestConstants.TEST_BILDUNGSKATEGORIE_ID)
            ),
            is(true)
        );
        Log.info(bildungskategorien.toString());
    }
}
