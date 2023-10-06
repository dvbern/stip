package ch.dvbern.stip.test.gesuch;

import ch.dvbern.oss.stip.contract.test.api.GesuchApiSpec;
import ch.dvbern.stip.test.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.test.util.RequestSpecUtil;
import ch.dvbern.stip.test.util.TestDatabaseEnvironment;
import ch.dvbern.stip.test.util.TestUtil;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import org.instancio.Instancio;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.UUID;

import static ch.dvbern.stip.test.generator.api.GesuchTestSpecGenerator.gesuchUpdateDtoSpecFullModel;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GesuchEinreichenUniqueSVNummerTest {

    public final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());

    @Test
    @Order(1)
    @TestAsGesuchsteller
    void gesuchEinreichtenWithUniqueSvNummerAccepted() {
        UUID gesuchId = createFullGesuch();

        gesuchApiSpec.gesuchEinreichen().gesuchIdPath(gesuchId)
                .execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @Order(2)
    @TestAsGesuchsteller
    void gesuchEinreichenWithNonUniqueSvNummerError() {
        UUID gesuchId = createFullGesuch(); //neues Gesuch mit selber AHV-Nummer wird erstellt

        var response = gesuchApiSpec.gesuchEinreichen().gesuchIdPath(gesuchId)
                .execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @NotNull
    private UUID createFullGesuch() {
        var response = gesuchApiSpec.createGesuch()
                .body(TestUtil.initGesuchCreateDto())
                .execute(ResponseBody::prettyPeek).then();

        response.assertThat()
                .statusCode(Response.Status.CREATED.getStatusCode());

        var gesuchId = TestUtil.extractIdFromResponse(response);
        var gesuchUpdatDTO = Instancio.of(gesuchUpdateDtoSpecFullModel).create();

        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.ACCEPTED.getStatusCode());
        return gesuchId;
    }
}
