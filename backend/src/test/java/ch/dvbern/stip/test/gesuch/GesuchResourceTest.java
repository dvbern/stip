package ch.dvbern.stip.test.gesuch;

import ch.dvbern.stip.generated.test.api.GesuchApiSpec;
import ch.dvbern.stip.generated.test.dto.*;
import ch.dvbern.stip.test.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.test.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.test.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.test.generator.api.model.gesuch.AdresseSpecModel;
import ch.dvbern.stip.test.util.RequestSpecUtil;
import ch.dvbern.stip.test.util.TestConstants;
import ch.dvbern.stip.test.util.TestDatabaseEnvironment;
import ch.dvbern.stip.test.util.TestUtil;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static ch.dvbern.stip.test.util.TestConstants.GUELTIGKEIT_PERIODE_23_24;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.instancio.Select.field;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GesuchResourceTest {

    public final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final String geschwisterNameUpdateTest = "UPDATEDGeschwister";
    private UUID gesuchId;
    private GesuchDtoSpec gesuch;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void testCreateEndpoint() {
        var gesuchDTO = new GesuchCreateDtoSpec();
        gesuchDTO.setFallId(UUID.fromString(TestConstants.FALL_TEST_ID));
        gesuchDTO.setGesuchsperiodeId(TestConstants.GESUCHSPERIODE_TEST_ID);
        var response = gesuchApiSpec.createGesuch().body(gesuchDTO).execute(ResponseBody::prettyPeek)
            .then();

        response.assertThat()
            .statusCode(Response.Status.CREATED.getStatusCode());

        gesuchId = TestUtil.extractIdFromResponse(response);
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void testDontFindGesuchWithNoFormularToWorkWith() {
        var gesuche = gesuchApiSpec.getGesuche().execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec[].class);

        assertThat(findGesuchWithId(gesuche, gesuchId).isPresent(), is(false));
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void gesuchTrancheCreated() {
        gesuch = gesuchApiSpec.getGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec.class);

        assertThat(gesuch.getGesuchTrancheToWorkWith(), notNullValue());
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGueltigAb(), is(GUELTIGKEIT_PERIODE_23_24.getGueltigAb()));
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGueltigBis(), is(GUELTIGKEIT_PERIODE_23_24.getGueltigBis()));
    }

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void updateWithNotExistingGesuchTranche() {
        var gesuchUpdateDTO = Instancio.of(GesuchTestSpecGenerator.gesuchUpdateDtoSpecAusbildungModel).create();
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(UUID.randomUUID());
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(5)
    void testUpdateGesuchEndpointAusbildung() {
        var gesuchUpdateDTO = Instancio.of(GesuchTestSpecGenerator.gesuchUpdateDtoSpecAusbildungModel).create();
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(6)
    void testDontFindGesuchWithNoPersonInAusbildung() {
        var gesuche = gesuchApiSpec.getGesuche().execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec[].class);

        assertThat(findGesuchWithId(gesuche, gesuchId).isPresent(), is(false));
    }

    @Test
    @TestAsGesuchsteller
    @Order(7)
    void testUpdateGesuchEndpointPersonInAusbildung() {
        var gesuchUpdatDTO = Instancio.of(GesuchTestSpecGenerator.gesuchUpdateDtoSpecPersonInAusbildungModel).create();
        gesuchUpdatDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(8)
    void testUpdateGesuchEndpointFamiliensituation() {
        var gesuchUpdatDTO = Instancio.of(GesuchTestSpecGenerator.gesuchUpdateDtoSpecFamiliensituationModel).create();
        gesuchUpdatDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(9)
    void testUpdateGesuchEndpointPartner() {
        var gesuchUpdatDTO = Instancio.of(GesuchTestSpecGenerator.gesuchUpdateDtoSpecPartnerModel).create();
        gesuchUpdatDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(10)
    void testUpdateGesuchEndpointAuszahlung() {
        var gesuchUpdatDTO = Instancio.of(GesuchTestSpecGenerator.gesuchUpdateDtoSpecAuszahlungModel).create();
        gesuchUpdatDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(11)
    void testUpdateGesuchEndpointAddGeschwister() {
        var gesuchUpdatDTO = Instancio.of(GesuchTestSpecGenerator.gesuchUpdateDtoSpecGeschwisterModel).create();
        gesuchUpdatDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(12)
    void testUpdateGesuchEndpointUpdateGeschwister() {
        var gesuch =
            gesuchApiSpec.getGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek).then().extract()
                .body()
                .as(GesuchDtoSpec.class);
        var gesuchUpdatDTO = Instancio.of(GesuchTestSpecGenerator.gesuchUpdateDtoSpecGeschwisterModel).create();
        gesuchUpdatDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchUpdatDTO.getGesuchTrancheToWorkWith().getGesuchFormular()
            .getGeschwisters()
            .get(0)
            .setId(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getGeschwisters().get(0).getId());
        gesuchUpdatDTO.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getGeschwisters()
            .get(0)
            .setNachname(geschwisterNameUpdateTest);
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(13)
    void testUpdateGesuchAddLebenslaufEndpoint() {
        var gesuchUpdatDTO = Instancio.of(GesuchTestSpecGenerator.gesuchUpdateDtoSpecLebenslaufModel).create();
        gesuchUpdatDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(14)
    void testUpdateGesuchAddElternEndpoint() {
        var gesuchUpdatDTO = Instancio.of(GesuchTestSpecGenerator.gesuchUpdateDtoSpecElternsModel).create();
        gesuchUpdatDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(14)
    void testUpdateGesuchAddForeignEltern() {
        var gesuchUpdateDTO = Instancio.of(GesuchTestSpecGenerator.gesuchUpdateDtoSpecElternsModel).create();
        final var elternteile = gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns();

        for (final var elternteil : elternteile) {
            elternteil.setSozialversicherungsnummer(null);
            final var newAdresse = Instancio.of(AdresseSpecModel.adresseSpecModel)
                .set(field(AdresseDtoSpec::getLand), LandDtoSpec.DE)
                .create();
            elternteil.setAdresse(newAdresse);
        }

        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().setElterns(elternteile);

        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(15)
    void testUpdateGesuchEndpointAddEinnahmenKoster() {
        var gesuchUpdatDTO = Instancio.of(GesuchTestSpecGenerator.gesuchUpdateDtoSpecEinnahmenKostenModel).create();
        gesuchUpdatDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(16)
    void testUpdateGesuchEndpointAddKind() {
        var gesuchUpdatDTO = Instancio.of(GesuchTestSpecGenerator.gesuchUpdateDtoSpecKinderModel).create();
        gesuchUpdatDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(17)
    void testAllFormularPresent() {
        var gesuch =
            gesuchApiSpec.getGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek).then().extract()
                .body()
                .as(GesuchDtoSpec.class);
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung(), is(notNullValue()));
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getAusbildung(), is(notNullValue()));
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation(), is(notNullValue()));
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getPartner(), is(notNullValue()));
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getAuszahlung(), is(notNullValue()));
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten(), is(notNullValue()));
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getGeschwisters().size(), is(1));
        assertThat(
            gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getGeschwisters().get(0).getNachname(),
            is(geschwisterNameUpdateTest)
        );
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getLebenslaufItems().size(), is(1));
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().size(), is(1));
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getKinds().size(), is(1));
    }

    @Test
    @TestAsGesuchsteller
    @Order(18)
    void testGetGesucheForBenutzende() {
        var gesuche = gesuchApiSpec.getGesucheForBenutzer()
            .benutzerIdPath(TestConstants.GESUCHSTELLER_TEST_ID)
            .execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec[].class);
        assertThat(findGesuchWithId(gesuche, gesuchId).isPresent(), is(true));
    }

    @Test
    @TestAsGesuchsteller
    @Order(19)
    void test_get_gesuch_for_fall() {
        var gesuche = gesuchApiSpec.getGesucheForFall()
            .fallIdPath(TestConstants.FALL_TEST_ID)
            .execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec[].class);
        assertThat(findGesuchWithId(gesuche, gesuchId).isPresent(), is(true));
    }

    @Test
    @TestAsGesuchsteller
    @Order(20)
    void testGesuchEinreichenValidationError() {
        var validationReport = gesuchApiSpec.gesuchEinreichen().gesuchIdPath(gesuchId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.BAD_REQUEST.getStatusCode())
            .extract()
            .body()
            .as(ValidationReportDtoSpec.class);
        assertThat(validationReport.getValidationErrors().size(), greaterThan(0));

        var validationReportFromService = gesuchApiSpec.gesuchEinreichenValidieren().gesuchIdPath(gesuchId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(ValidationReportDtoSpec.class);

        Assertions.assertThat(validationReport)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(validationReportFromService);
    }

    @Test
    @TestAsGesuchsteller
    @Order(21)
    void testFindGesuche() {
        var gesuche = gesuchApiSpec.getGesuche().execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec[].class);

        var gesuchOpt = findGesuchWithId(gesuche, gesuchId);
        assertThat(gesuchOpt.isPresent(), is(true));
        assertThat(gesuchOpt.get().getFall().getId(), is(UUID.fromString(TestConstants.FALL_TEST_ID)));
        assertThat(gesuchOpt.get().getGesuchsperiode().getId(), is(TestConstants.GESUCHSPERIODE_TEST_ID));
        assertThat(gesuchOpt.get().getAenderungsdatum(), notNullValue());
        assertThat(gesuchOpt.get().getBearbeiter(), is("John Doe"));
    }

    @Test
    @TestAsGesuchsteller
    @Order(22)
    void testDeleteGesuch() {
        gesuchApiSpec.deleteGesuch()
            .gesuchIdPath(gesuchId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    private Optional<GesuchDtoSpec> findGesuchWithId(GesuchDtoSpec[] gesuche, UUID gesuchId) {
        return Arrays.stream(gesuche)
            .filter(gesuchDtoSpec -> gesuchDtoSpec.getId().equals(gesuchId))
            .findFirst();
    }
}
