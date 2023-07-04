package ch.dvbern.stip.test.gesuch;

import ch.dvbern.stip.generated.test.api.GesuchApiSpec;
import ch.dvbern.stip.generated.test.dto.GesuchCreateDtoSpec;
import ch.dvbern.stip.generated.test.dto.GesuchDtoSpec;
import ch.dvbern.stip.test.util.RequestSpecUtil;
import ch.dvbern.stip.test.utils.TestDatabaseEnvironment;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GesuchResourceTest {

    public final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());

    @Test
    @Order(1)
    void testCreateEndpoint() {
        var gesuchDTO = new GesuchCreateDtoSpec();
        gesuchDTO.setFallId(UUID.fromString("4b99f69f-ec53-4ef7-bd1f-0e76e04abe7b"));
        gesuchDTO.setGesuchsperiodeId(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));

        gesuchApiSpec.createGesuch().body(gesuchDTO).execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @Order(2)
    void testFindGesuchEndpoint() {
        var gesuche = gesuchApiSpec.getGesuche().execute(ResponseBody::prettyPeek)
                .then()
                .extract()
                .body()
                .as(GesuchDtoSpec[].class);

        assertThat(gesuche.length, is(1));
        assertThat(gesuche[0].getFall().getId(), is(UUID.fromString("4b99f69f-ec53-4ef7-bd1f-0e76e04abe7b")));
        assertThat(gesuche[0].getGesuchsperiode().getId(), is(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6")));
    }
}
