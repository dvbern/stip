package ch.dvbern.stip.gesuch;

import ch.dvbern.stip.fall.entity.Fall;
import ch.dvbern.stip.gesuch.dto.GesuchDTO;
import ch.dvbern.stip.gesuch.model.Gesuch;
import ch.dvbern.stip.gesuch.model.Gesuchstatus;
import ch.dvbern.stip.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.utils.TestDatabaseEnvironment;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
public class GesuchResourceTest {

    @Test
    void testCreateAndGetEndpoint() {
        GesuchDTO gesuchDTO = createGesuchWithExistingFallandGP();

        given().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(gesuchDTO)
                .when()
                .post("/api/v1/gesuch")
                .then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode());


        given()
                .when()
                .get("/api/v1/gesuch")
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(is(not(empty())));
    }

    private GesuchDTO createGesuchWithExistingFallandGP(){
        final Gesuch gesuch = new Gesuch();
        gesuch.setGesuchNummer(0);
        gesuch.setGesuchStatus(Gesuchstatus.OFFEN);
        Fall fall = new Fall();
        fall.setId(UUID.fromString("4b99f69f-ec53-4ef7-bd1f-0e76e04abe7b"));
        gesuch.setFall(fall);
        Gesuchsperiode gesuchsperiode = new Gesuchsperiode();
        gesuchsperiode.setId(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));
        gesuch.setGesuchsperiode(gesuchsperiode);
        return GesuchDTO.from(gesuch);
    }
}
