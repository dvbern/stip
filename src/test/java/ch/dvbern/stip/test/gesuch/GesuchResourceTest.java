package ch.dvbern.stip.test.gesuch;

import ch.dvbern.stip.generated.test.api.GesuchApiSpec;
import ch.dvbern.stip.generated.test.dto.*;
import ch.dvbern.stip.test.util.RequestSpecUtil;
import ch.dvbern.stip.test.utils.TestDatabaseEnvironment;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import java.util.UUID;

import static ch.dvbern.stip.test.utils.DTOGenerator.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GesuchResourceTest {

    public final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());

    private UUID gesuchId;

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

        gesuchId = gesuche[0].getId();

        assertThat(gesuche.length, is(1));
        assertThat(gesuche[0].getFall().getId(), is(UUID.fromString("4b99f69f-ec53-4ef7-bd1f-0e76e04abe7b")));
        assertThat(gesuche[0].getGesuchsperiode().getId(), is(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6")));
    }

    @Test
    @Order(3)
    void testUpdateGesuchPersonInAusbildungEndpoint() {
        var gesuchUpdatDTO = prepareGesuchUpdateForPersonInAusbildung();
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @Order(4)
    void testUpdateGesuchAusbildungEndpoint() {
        var gesuchUpdatDTO = prepareGesuchUpdateForAusbildung();
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @Order(5)
    void testUpdateGesuchFamiliensituationEndpoint() {
        var gesuchUpdatDTO = prepareGesuchUpdateForFamiliensituation();
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @Order(6)
    void testUpdateGesuchPartnerEndpoint() {
        var gesuchUpdatDTO = prepareGesuchUpdateForPartner();
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @Order(7)
    void testUpdateGesuchAuszahlungEndpoint() {
        var gesuchUpdatDTO = prepareGesuchUpdateForAuszahlung();
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @Order(8)
    void testAllFormularPresent() {
        var gesuch = gesuchApiSpec.getGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek).then().extract()
                .body()
                .as(GesuchDtoSpec.class);
        assertThat(gesuch.getGesuchFormularToWorkWith().getPersonInAusbildung(), is(notNullValue()));
        assertThat(gesuch.getGesuchFormularToWorkWith().getAusbildung(), is(notNullValue()));
        assertThat(gesuch.getGesuchFormularToWorkWith().getFamiliensituation(), is(notNullValue()));
        assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), is(notNullValue()));
        assertThat(gesuch.getGesuchFormularToWorkWith().getAuszahlung(), is(notNullValue()));

    }

    // Not working, cannot override mapping in collection with mapstruct
    //@Test
    //@Order(4)
    void testUpdateGesuchAddLebenslaufEndpoint() {
        var gesuchUpdatDTO = prepareGesuchUpdateForLebenslauf();
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.ACCEPTED.getStatusCode());

        var gesuche = gesuchApiSpec.getGesuche().execute(ResponseBody::prettyPeek)
                .then()
                .extract()
                .body()
                .as(GesuchDtoSpec[].class);

        gesuchId = gesuche[0].getId();

        assertThat(gesuche.length, is(1));
        assertThat(gesuche[0].getGesuchFormularToWorkWith().getPersonInAusbildung(), is(notNullValue()));
        assertThat(gesuche[0].getGesuchFormularToWorkWith().getLebenslaufItems().size(), is(1));
    }


}
