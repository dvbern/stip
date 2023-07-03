package ch.dvbern.stip.test.gesuch;

import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.Gesuchstatus;
import ch.dvbern.stip.api.gesuch.service.GesuchMapper;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.test.utils.TestDatabaseEnvironment;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
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

    @Inject
    GesuchMapper gesuchMapper;

    @Test
    void testCreateAndGetEndpoint() {
        GesuchDto gesuchDTO = createGesuchWithExistingFallandGP();

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

    private GesuchDto createGesuchWithExistingFallandGP(){
        final Gesuch gesuch = new Gesuch();
        gesuch.setGesuchNummer(0);
        gesuch.setGesuchStatus(Gesuchstatus.OFFEN);
        Fall fall = new Fall();
        fall.setId(UUID.fromString("4b99f69f-ec53-4ef7-bd1f-0e76e04abe7b"));
        gesuch.setFall(fall);
        Gesuchsperiode gesuchsperiode = new Gesuchsperiode();
        gesuchsperiode.setId(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));
        gesuch.setGesuchsperiode(gesuchsperiode);
        return gesuchMapper.toDto(gesuch);
    }
}
