package ch.dvbern.stip.api.gesuch.entity;

import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.instancio.Instancio;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator.gesuchUpdateDtoSpecFullModel;
import static ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator.gesuchUpdateDtoSpecKinderModel;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GesuchStatusTest {

    private static final String UNIQUE_GUELTIGE_AHV_NUMMER = "756.3333.3333.35";
    public final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private UUID gesuchId;
    private UUID gesuchTrancheId;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void testCreateGesuchStatusNeu() {
        var gesuchDTO = TestUtil.initGesuchCreateDto();

        var response = gesuchApiSpec.createGesuch().body(gesuchDTO).execute(ResponseBody::prettyPeek).then();
        response.assertThat()
            .statusCode(Response.Status.CREATED.getStatusCode());
        gesuchId = TestUtil.extractIdFromResponse(response);

        var gesuch = gesuchApiSpec.getGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec.class);

        gesuchTrancheId = gesuch.getGesuchTrancheToWorkWith().getId();

        assertThat(gesuch.getGesuchStatus()).isEqualTo(GesuchstatusDtoSpec.OFFEN);
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void testFillGesuchUndEinreichenDokumentFehlt() {
        var gesuchUpdatDTO = Instancio.of(gesuchUpdateDtoSpecFullModel).create();
        gesuchUpdatDTO.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setSozialversicherungsnummer(UNIQUE_GUELTIGE_AHV_NUMMER);
        gesuchUpdatDTO.getGesuchTrancheToWorkWith().setId(gesuchTrancheId);
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());

        gesuchApiSpec.gesuchEinreichen().gesuchIdPath(gesuchId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.ACCEPTED.getStatusCode());
        var gesuch =
            gesuchApiSpec.getGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek).then().extract()
                .body()
                .as(GesuchDtoSpec.class);
        assertThat(gesuch.getGesuchStatus()).isEqualTo(GesuchstatusDtoSpec.NICHT_KOMPLETT_EINGEREICHT);
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void testGesuchEinreichenDokumentFehltUpdateFailed() {
        var gesuchUpdatDTO = Instancio.of(gesuchUpdateDtoSpecKinderModel).create();
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void testGesuchEinreichenNachfristVerlangen() {
        gesuchApiSpec.gesuchNachfristBeantragen().gesuchIdPath(gesuchId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.ACCEPTED.getStatusCode());
        var gesuch =
            gesuchApiSpec.getGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek).then().extract()
                .body()
                .as(GesuchDtoSpec.class);
        assertThat(gesuch.getGesuchStatus()).isEqualTo(GesuchstatusDtoSpec.NICHT_KOMPLETT_EINGEREICHT_NACHFRIST);
    }

    @Test
    @TestAsGesuchsteller
    @Order(5)
    void testGesuchEinreichenDokumentNachfristVerlangenUpdateFailed() {
        var gesuchUpdatDTO = Instancio.of(gesuchUpdateDtoSpecKinderModel).create();
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }
}