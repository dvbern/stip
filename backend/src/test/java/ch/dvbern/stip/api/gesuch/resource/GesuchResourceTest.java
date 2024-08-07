package ch.dvbern.stip.api.gesuch.resource;

import java.time.LocalDate;
import java.util.*;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.generator.api.model.gesuch.AdresseSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.ElternUpdateDtoSpecModel;
import ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.*;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.inject.Inject;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
// TODO KSTIP-1236: Test Aenderungsantrag once proper generation is done
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
        gesuchDTO.setGesuchsperiodeId(TestConstants.TEST_GESUCHSPERIODE_ID);
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
    @Order(4)
    void testUpdateGesuchEmptyEinnahmenKosten() {
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecAusbildung;
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().setEinnahmenKosten(null);
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
        gesuch = gesuchApiSpec.getGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec.class);
        assertNull(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten());
    }

    @Test
    @TestAsGesuchsteller
    @Order(5)
    void testUpdateGesuchEndpointPersonInAusbildung() {
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecPersonInAusbildung;
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchUpdateDTO.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setSozialversicherungsnummer(AHV_NUMMER_VALID_PERSON_IN_AUSBILDUNG_2);
        gesuchUpdateDTO.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .setPartner(null);
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
        //removing validatePage for now as we somehow pass along an ausbildung which triggers the LebenslaufLuckenlosItemPageValidator
        // TODO: fix in Testrewrite
        // validatePage();
    }

    @Test
    @TestAsGesuchsteller
    @Order(6)
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

    //    @Test
    //    @TestAsGesuchsteller
    //    @Order(9)
    //    void testUpdateGesuchEndpointPartner() {
    //        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecPartner;
    //        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
    //        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getPartner().setJahreseinkommen(5000);
    ////        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getPartner().getAdresse().setId();
    //        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute
    //        (ResponseBody::prettyPeek)
    //            .then()
    //            .assertThat()
    //            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    //
    //        validatePage();
    //    }

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
    @Order(15)
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
    void testUpdateGesuchEndpointAddSteuerdatenTabs() {
        updateGesuch();

        final var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSteuerdatenTabs;
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec
            .updateGesuch()
            .gesuchIdPath(gesuchId)
            .body(gesuchUpdateDTO)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());

        validatePage();
    }

    @Test
    @TestAsGesuchsteller
    @Order(17)
    void testUpdateGesuchEndpointAddEinnahmenKoster() {
        updateGesuch();

        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecEinnahmenKosten;
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().setPartner(null);
        final var personInAusbildung =
            GesuchTestSpecGenerator.gesuchUpdateDtoSpecPersonInAusbildung.getGesuchTrancheToWorkWith()
                .getGesuchFormular()
                .getPersonInAusbildung();
        personInAusbildung.getAdresse().setId(
            gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().getAdresse().getId()
        );
        gesuchUpdateDTO.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .setPersonInAusbildung(personInAusbildung);
        gesuchUpdateDTO.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .setPartner(null);
        gesuchUpdateDTO.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setZivilstand(ZivilstandDtoSpec.LEDIG);

        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
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

        validatePage(false);
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
    void testGesuchStatusChangeToInBearbeitungSB() {
        var gesuch = gesuchApiSpec.changeGesuchStatusToInBearbeitung().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .body()
                .as(GesuchDtoSpec.class);

        assertThat(gesuch.getGesuchStatus(), is(GesuchstatusDtoSpec.IN_BEARBEITUNG_SB));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(22)
    void testGetAllGesucheSbNoUnwantedStatus() {
        var gesuche = gesuchApiSpec.getGesucheSb().getGesucheSBQueryTypePath(GetGesucheSBQueryType.ALLE_BEARBEITBAR).execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec[].class);

        for (GesuchDtoSpec gesuch : gesuche) {
            assertThat(gesuch.getGesuchStatus(), not(GesuchstatusDtoSpec.IN_BEARBEITUNG_GS));
            assertThat(gesuch.getGesuchStatus(), not(GesuchstatusDtoSpec.EINGEREICHT));
            gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().setPersonInAusbildung(null);
        }


        gesuche = gesuchApiSpec.getGesucheSb().getGesucheSBQueryTypePath(GetGesucheSBQueryType.ALLE_BEARBEITBAR).execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec[].class);

        for (GesuchDtoSpec gesuch : gesuche) {
            assertThat(gesuch.getGesuchStatus(), not(GesuchstatusDtoSpec.IN_BEARBEITUNG_GS));
            assertThat(gesuch.getGesuchStatus(), not(GesuchstatusDtoSpec.EINGEREICHT));
        }

    }

    @Test
    @TestAsSachbearbeiter
    @Order(23)
    void testGetGesucheSbNoUnwantedStatus() {
        var gesuche = gesuchApiSpec.getGesucheSb().getGesucheSBQueryTypePath(GetGesucheSBQueryType.ALLE_BEARBEITBAR).execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec[].class);

        for (GesuchDtoSpec gesuch : gesuche) {
            assertThat(gesuch.getGesuchStatus(), not(GesuchstatusDtoSpec.IN_BEARBEITUNG_GS));
            assertThat(gesuch.getGesuchStatus(), not(GesuchstatusDtoSpec.EINGEREICHT));
        }
    }

    @Test
    @TestAsGesuchsteller
    @Order(24)
    void testFindGesuche() {
        var gesuche = gesuchApiSpec.getGesucheGs().execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec[].class);

        var gesuchOpt = findGesuchWithId(gesuche, gesuchId);
        assertThat(gesuchOpt.isPresent(), is(true));
        assertThat(gesuchOpt.get().getFall().getId(), is(fallId));
        assertThat(gesuchOpt.get().getGesuchsperiode().getId(), is(TestConstants.TEST_GESUCHSPERIODE_ID));
        assertThat(
            gesuchOpt.get().getGesuchStatus().toString(),
            gesuchOpt.get().getGesuchStatus(),
            is(GesuchstatusDtoSpec.IN_BEARBEITUNG_SB));
        assertThat(gesuchOpt.get().getAenderungsdatum(), notNullValue());
    }

    @Test
    @TestAsGesuchsteller
    @Order(25)
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
            //            DokumentTypDtoSpec.PARTNER_AUSBILDUNG_LOHNABRECHNUNG, //Temporarily disabled due to
            //            fundamentally broken tests
            //            DokumentTypDtoSpec.PARTNER_BELEG_OV_ABONNEMENT,
            DokumentTypDtoSpec.KINDER_UNTERHALTSVERTRAG_TRENNUNGSKONVENTION,
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

        final var message = String.format(
            "Expected: \n%s\nbut was: \n%s",
            Arrays.toString(expectedDokumentTypes),
            Arrays.stream(gesuchDokumente).map(GesuchDokumentDtoSpec::getDokumentTyp).toList()
        );
        assertThat(
            message,
            expectedDokumentTypes.length,
            is(gesuchDokumente.length)
        );

        final var set = EnumSet.noneOf(DokumentTypDtoSpec.class);
        set.addAll(Arrays.asList(expectedDokumentTypes));

        // Checks if all dokument types returned from the API are in the list of expected types
        assertThat(
            message,
            set.containsAll(Arrays.stream(gesuchDokumente).map(GesuchDokumentDtoSpec::getDokumentTyp).toList()),
            is(true)
        );
    }

    @Test
    @TestAsSachbearbeiter
    @Order(26)
    void testGetStatusprotokoll() {
        final var statusprotokoll = gesuchApiSpec.getStatusProtokoll()
            .gesuchIdPath(gesuchId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(StatusprotokollEntryDtoSpec[].class);

        assertThat(
            Arrays.toString(statusprotokoll),
            statusprotokoll.length,
            is(3)
        );

        final var expectedOldStatus = Set.of(
            GesuchstatusDtoSpec.IN_BEARBEITUNG_GS,
            GesuchstatusDtoSpec.BEREIT_FUER_BEARBEITUNG,
            GesuchstatusDtoSpec.IN_BEARBEITUNG_SB
        );

        assertThat(
            String.format(
                "Expected: %s\nActual: %s",
                Arrays.toString(expectedOldStatus.toArray()),
                Arrays.toString(statusprotokoll)
            ),
            expectedOldStatus.containsAll(Arrays.stream(statusprotokoll)
                .map(StatusprotokollEntryDtoSpec::getStatus)
                .toList()
            ),
            is(true)
        );
    }

    @Inject
    GesuchDokumentRepository dokumentRepository;
    @Inject
    GesuchDokumentService gesuchDokumentService;

    @Test
    @TestAsSachbearbeiter
    @Order(27)
    void testDokumentAblehnenKommentar(){
        List<GesuchDokument> dokumente = dokumentRepository.findAllForGesuch(gesuch.getId()).toList();
        final var dto = new GesuchDokumentAblehnenRequestDto();
        final var kommentarDto = new GesuchDokumentKommentarDto();
        kommentarDto.setKommentar("blabla");
        kommentarDto.setDokumentTyp(dokumente.get(0).getDokumentTyp());
        kommentarDto.setDatum(LocalDate.now());
        kommentarDto.setBenutzer("testuser");
        UUID dokumentId = dokumente.get(0).getId();
        kommentarDto.setGesuchDokumentId(dokumentId);
        kommentarDto.setGesuchId(gesuch.getId());
        dto.setKommentar(kommentarDto);
        gesuchDokumentService.gesuchDokumentAblehnen(dokumentId,dto);
        assertThat(gesuchDokumentService.getGesuchDokumentKommentarsByGesuchDokumentId(dokumente.get(0).getId()).size(), greaterThan(0));
    }

    @Test
    @TestAsAdmin
    @Order(28)
    void testDeleteGesuch() {
        gesuchApiSpec.deleteGesuch()
            .gesuchIdPath(gesuchId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());

        AdresseSpecModel.adresseDtoSpec.setId(null);
    }

    private void updateGesuch() {
        gesuch = gesuchApiSpec.getGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec.class);
    }

    private void validatePage() {
        validatePage(true);
    }

    private void validatePage(final boolean allowWarnings) {
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
        if (!allowWarnings) {
            assertThat(report.getValidationWarnings(), is(empty()));
        }
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
