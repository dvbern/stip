package ch.dvbern.stip.api.gesuch.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.generator.api.model.gesuch.AdresseSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.ElternUpdateDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.DokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.ElternTypDtoSpec;
import ch.dvbern.stip.generated.dto.FallDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDokumentDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.LandDtoSpec;
import ch.dvbern.stip.generated.dto.ValidationReportDto;
import ch.dvbern.stip.generated.dto.ValidationReportDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
class GesuchResourceTest {
    public final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    public final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    public final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final String geschwisterNameUpdateTest = "UPDATEDGeschwister";
	private UUID fallId;
    private UUID gesuchId;
    private GesuchDtoSpec gesuch;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void createFall() {
        var fall = fallApiSpec.createFallForGs()
				.execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Status.OK.getStatusCode())
				.extract()
                .body()
                .as(FallDtoSpec.class);

        fallId = fall.getId();
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void testCreateEndpoint() {
        var gesuchDTO = new GesuchCreateDtoSpec();
        gesuchDTO.setFallId(fallId);
        gesuchDTO.setGesuchsperiodeId(TestConstants.GESUCHSPERIODE_TEST_ID);
        var response = gesuchApiSpec.createGesuch().body(gesuchDTO).execute(ResponseBody::prettyPeek)
            .then();

        response.assertThat()
            .statusCode(Response.Status.CREATED.getStatusCode());

        gesuchId = TestUtil.extractIdFromResponse(response);
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

//    @Test
//    @TestAsGesuchsteller
//    @Order(8)
//    void testUpdatePersonCreatedZuordnung() throws InterruptedException {
//        Thread.sleep(5000);
//        final var response = gesuchApiSpec.getGesucheForBenutzer()
//            .benutzerIdPath(UUID.fromString(TestConstants.GESUCHSTELLER_TEST_ID))
//            .execute(ResponseBody::prettyPeek)
//            .then()
//            .assertThat()
//            .statusCode(Response.Status.OK.getStatusCode());
//
//        final var myGesuche = response
//            .extract()
//            .body()
//            .as(GesuchDtoSpec[].class);
//
//        assertThat(myGesuche.length, is(greaterThan(0)));
//        assertThat(Arrays.stream(myGesuche).allMatch(x -> x.getBearbeiter().equals("Philipp Schärer")), is(true));
//    }

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
    @Order(16)
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
        var gesuche = gesuchApiSpec.getGesucheGs()
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
            .fallIdPath(fallId)
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
    void uploadAllDocumentTypes() {
        final var dokTypes = DokumentTypDtoSpec.values();
        for (final var dokType : dokTypes) {
            uploadDocumentWithType(dokType);
        }
    }

    @Test
    @TestAsGesuchsteller
    @Order(20)
    void testRemoveSuperfluousDocuments() {
        // getGesuchDokumente also removes superfluous documents from the Gesuch
        // This is needed so the follow check if only necessary documents are saved works
        gesuchApiSpec.getGesuchDokumente()
            .gesuchIdPath(gesuchId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
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
    @TestAsSachbearbeiter
    @Order(21)
    void testGetAllGesucheSbNoUnwantedStatus() {
        var gesuche = gesuchApiSpec.getAllGesucheSb().execute(ResponseBody::prettyPeek)
                .then()
                .extract()
                .body()
                .as(GesuchDtoSpec[].class);

        for(GesuchDtoSpec gesuch : gesuche) {
            assertThat(gesuch.getGesuchStatus(), not(GesuchstatusDtoSpec.IN_BEARBEITUNG_GS));
            assertThat(gesuch.getGesuchStatus(), not(GesuchstatusDtoSpec.KOMPLETT_EINGEREICHT));
        }
    }

    @Test
    @TestAsSachbearbeiter
    @Order(21)
    void testGetGesucheSbNoUnwantedStatus() {
        var gesuche = gesuchApiSpec.getGesucheSb().execute(ResponseBody::prettyPeek)
                .then()
                .extract()
                .body()
                .as(GesuchDtoSpec[].class);

        for(GesuchDtoSpec gesuch : gesuche) {
            assertThat(gesuch.getGesuchStatus(), not(GesuchstatusDtoSpec.IN_BEARBEITUNG_GS));
            assertThat(gesuch.getGesuchStatus(), not(GesuchstatusDtoSpec.KOMPLETT_EINGEREICHT));
        }
    }

    @Test
    @TestAsGesuchsteller
    @Order(22)
    void testFindGesuche() {
        var gesuche = gesuchApiSpec.getGesucheGs().execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec[].class);

        var gesuchOpt = findGesuchWithId(gesuche, gesuchId);
        assertThat(gesuchOpt.isPresent(), is(true));
        assertThat(gesuchOpt.get().getFall().getId(), is(fallId));
        assertThat(gesuchOpt.get().getGesuchsperiode().getId(), is(TestConstants.GESUCHSPERIODE_TEST_ID));
        assertThat(gesuchOpt.get().getGesuchStatus().toString(), gesuchOpt.get().getGesuchStatus(), is(GesuchstatusDtoSpec.BEREIT_FUER_BEARBEITUNG));
        assertThat(gesuchOpt.get().getAenderungsdatum(), notNullValue());
    }

    @Test
    @TestAsGesuchsteller
    @Order(23)
    void testGetGesuchDokumente() {
        final var expectedDokumentTypes = new DokumentTypDtoSpec[] {
            DokumentTypDtoSpec.PERSON_SOZIALHILFEBUDGET,
            DokumentTypDtoSpec.PERSON_AUSWEIS,
            DokumentTypDtoSpec.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER,
            DokumentTypDtoSpec.ELTERN_ERGAENZUNGSLEISTUNGEN_VATER,
            DokumentTypDtoSpec.ELTERN_SOZIALHILFEBUDGET_VATER,
            DokumentTypDtoSpec.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER,
            DokumentTypDtoSpec.ELTERN_ERGAENZUNGSLEISTUNGEN_MUTTER,
            DokumentTypDtoSpec.ELTERN_SOZIALHILFEBUDGET_MUTTER,
            DokumentTypDtoSpec.GESCHWISTER_BESTAETIGUNG_AUSBILDUNGSSTAETTE,
            DokumentTypDtoSpec.PARTNER_AUSBILDUNG_LOHNABRECHNUNG,
            DokumentTypDtoSpec.PARTNER_BELEG_OV_ABONNEMENT,
            DokumentTypDtoSpec.AUSZAHLUNG_ABTRETUNGSERKLAERUNG,
            DokumentTypDtoSpec.EK_BELEG_KINDERZULAGEN,
            DokumentTypDtoSpec.EK_VERFUEGUNG_GEMEINDE_INSTITUTION,
            DokumentTypDtoSpec.EK_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN,
            DokumentTypDtoSpec.EK_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO,
            DokumentTypDtoSpec.EK_BELEG_OV_ABONNEMENT,
            DokumentTypDtoSpec.EK_MIETVERTRAG,
            DokumentTypDtoSpec.EK_BELEG_BETREUUNGSKOSTEN_KINDER,
            DokumentTypDtoSpec.EK_LOHNABRECHNUNG
        };

        var gesuchDokumente = gesuchApiSpec.getGesuchDokumente()
            .gesuchIdPath(gesuchId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDokumentDtoSpec[].class);

        assertThat(
            // Print a nice list of expected vs actual dokument types returned
            String.format(
                "Expected: \n%s\nbut was: \n%s",
                Arrays.toString(expectedDokumentTypes),
                Arrays.stream(gesuchDokumente).map(GesuchDokumentDtoSpec::getDokumentTyp).toList()
            ),
            expectedDokumentTypes.length, is(gesuchDokumente.length)
        );

        final var set = EnumSet.noneOf(DokumentTypDtoSpec.class);
        set.addAll(Arrays.asList(expectedDokumentTypes));

        // Checks if all dokument types returned from the API are in the list of expected types
        assertThat(
            set.containsAll(Arrays.stream(gesuchDokumente).map(GesuchDokumentDtoSpec::getDokumentTyp).toList()),
            is(true)
        );
    }

    @Test
    @TestAsAdmin
    @Order(24)
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

    private void uploadDocumentWithType(final DokumentTypDtoSpec dokTyp) {
        final var file = TestUtil.getTestPng();
        TestUtil.uploadFile(dokumentApiSpec, gesuchId, dokTyp, file);
    }

    private Optional<GesuchDtoSpec> findGesuchWithId(GesuchDtoSpec[] gesuche, UUID gesuchId) {
        return Arrays.stream(gesuche)
            .filter(gesuchDtoSpec -> gesuchDtoSpec.getId().equals(gesuchId))
            .findFirst();
    }
}
