package ch.dvbern.stip.api.gesuch.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.generator.api.model.gesuch.AdresseSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.ElternUpdateDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.ElternTypDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.LandDtoSpec;
import ch.dvbern.stip.generated.dto.ValidationReportDto;
import ch.dvbern.stip.generated.dto.ValidationReportDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static ch.dvbern.stip.api.util.TestConstants.AHV_NUMMER_VALID_PERSON_IN_AUSBILDUNG_2;
import static ch.dvbern.stip.api.util.TestConstants.GUELTIGKEIT_PERIODE_23_24;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

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
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecAusbildung;
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
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecAusbildung;
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
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecPersonInAusbildung;
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchUpdateDTO.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setSozialversicherungsnummer(AHV_NUMMER_VALID_PERSON_IN_AUSBILDUNG_2);
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());

        validatePage();
    }

    @Test
    @TestAsGesuchsteller
    @Order(8)
    void testUpdateGesuchEndpointFamiliensituation() {
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecFamiliensituation;
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());

        validatePage();
    }

    @Test
    @TestAsGesuchsteller
    @Order(9)
    void testUpdateGesuchEndpointPartner() {
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecPartner;
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());

        validatePage();
    }

    @Test
    @TestAsGesuchsteller
    @Order(10)
    void testUpdateGesuchEndpointAuszahlung() {
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecAuszahlung;
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());

        validatePage();
    }

    @Test
    @TestAsGesuchsteller
    @Order(11)
    void testUpdateGesuchEndpointAddGeschwister() {
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecGeschwister;
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());

        validatePage();
    }

    @Test
    @TestAsGesuchsteller
    @Order(12)
    void testUpdateGesuchEndpointUpdateGeschwister() {
        var gesuch =
            gesuchApiSpec.getGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek).then().extract()
                .body()
                .as(GesuchDtoSpec.class);
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecGeschwister;
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular()
            .getGeschwisters()
            .get(0)
            .setId(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getGeschwisters().get(0).getId());
        gesuchUpdateDTO.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getGeschwisters()
            .get(0)
            .setNachname(geschwisterNameUpdateTest);
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());

        validatePage();
    }

    @Test
    @TestAsGesuchsteller
    @Order(13)
    void testUpdateGesuchAddLebenslaufEndpoint() {
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecLebenslauf;
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());

        validatePage();
    }

    @Test
    @TestAsGesuchsteller
    @Order(14)
    void testUpdateGesuchAddElternEndpoint() {
        final var vater = ElternUpdateDtoSpecModel.elternUpdateDtoSpecs().get(0);
        final var mutter = ElternUpdateDtoSpecModel.elternUpdateDtoSpecs().get(0);
        mutter.setElternTyp(ElternTypDtoSpec.MUTTER);
        mutter.setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_MUTTER);

        var gesuchUpdatDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecElterns;
        gesuchUpdatDTO.getGesuchTrancheToWorkWith().getGesuchFormular().setElterns(new ArrayList<>() {{
            add(vater);
            add(mutter);
        }});
        gesuchUpdatDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());

        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());

        validatePage();
    }

    @Test
    @TestAsGesuchsteller
    @Order(14)
    void testUpdateGesuchAddForeignEltern() {
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecElterns;
        final var elternteile = gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns();

        for (final var elternteil : elternteile) {
            elternteil.setSozialversicherungsnummer(null);
            final var newAdresse = AdresseSpecModel.adresseDtoSpec;
            newAdresse.setLand(LandDtoSpec.DE);
            elternteil.setAdresse(newAdresse);
        }

        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().setElterns(elternteile);

        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());

        validatePage();
    }

    @Test
    @TestAsGesuchsteller
    @Order(15)
    void testUpdateGesuchEndpointAddEinnahmenKoster() {
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecEinnahmenKosten;
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());

        validatePage();
    }

    @Test
    @TestAsGesuchsteller
    @Order(16)
    void testUpdateGesuchEndpointAddKind() {
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecKinder;
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());

        validatePage();
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
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().size(), is(2));
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getKinds().size(), is(1));

        validatePage();
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
    void testGesuchEinreichenNoValidationError() {
        var validationReportFromService = gesuchApiSpec.gesuchEinreichenValidieren().gesuchIdPath(gesuchId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(ValidationReportDtoSpec.class);

        assertThat(
            validationReportFromService.getValidationErrors().toString(),
            validationReportFromService.getValidationErrors().size(),
            not(greaterThan(0))
        );

        gesuchApiSpec.gesuchEinreichen().gesuchIdPath(gesuchId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.ACCEPTED.getStatusCode());
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

    private void validatePage() {
        final var report = gesuchApiSpec
            .validateGesuchPages()
            .gesuchIdPath(gesuchId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(ValidationReportDto.class);

        assertThat(report.getValidationErrors(), is(empty()));
    }

    private Optional<GesuchDtoSpec> findGesuchWithId(GesuchDtoSpec[] gesuche, UUID gesuchId) {
        return Arrays.stream(gesuche)
            .filter(gesuchDtoSpec -> gesuchDtoSpec.getId().equals(gesuchId))
            .findFirst();
    }
}
