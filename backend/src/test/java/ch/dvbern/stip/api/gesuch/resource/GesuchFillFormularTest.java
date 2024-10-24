package ch.dvbern.stip.api.gesuch.resource;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.generator.api.model.gesuch.AusbildungUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.AuszahlungUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.EinnahmenKostenUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.ElternUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.FamiliensituationUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.GeschwisterUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.LebenslaufItemUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.PersonInAusbildungUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.SteuerdatenUpdateTabsDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.StepwiseExtension.AlwaysRun;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.*;
import ch.dvbern.stip.generated.dto.*;
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
import org.junit.jupiter.api.extension.ExtendWith;

import static ch.dvbern.stip.api.util.TestConstants.GUELTIGKEIT_PERIODE_23_24;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
    // TODO KSTIP-1303: Test Aenderungsantrag once proper generation is done
class GesuchFillFormularTest {
    public final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    public final GesuchTrancheApiSpec gesuchTrancheApiSpec = GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());
    public final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    public final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    public final NotificationApiSpec notificationApiSpec = NotificationApiSpec.notification(RequestSpecUtil.quarkusSpec());
    private UUID fallId;
    private UUID gesuchId;
    private UUID gesuchTrancheId;
    private final GesuchFormularUpdateDtoSpec currentFormular = new GesuchFormularUpdateDtoSpec();
    private GesuchTrancheUpdateDtoSpec trancheUpdateDtoSpec;
    private NotificationDtoSpec notification;
    private NotificationDto notificationDto;
    @Test
    @TestAsGesuchsteller
    @Order(1)
    void createFall() {
        fallId = TestUtil.getOrCreateFall(fallApiSpec).getId();
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void testCreateEndpoint() {
        var gesuchDTO = new GesuchCreateDtoSpec();
        gesuchDTO.setFallId(fallId);
        gesuchDTO.setGesuchsperiodeId(TestConstants.TEST_GESUCHSPERIODE_ID);
        var response = gesuchApiSpec.createGesuch()
            .body(gesuchDTO)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.CREATED.getStatusCode());

        gesuchId = TestUtil.extractIdFromResponse(response);
        gesuchTrancheId = gesuchApiSpec.getCurrentGesuch()
            .gesuchIdPath(gesuchId)
            .execute(ResponseBody::prettyPeek).then().extract()
            .body()
            .as(GesuchDtoSpec.class)
            .getGesuchTrancheToWorkWith().getId();
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void gesuchTrancheCreated() {
        final var gesuch = gesuchApiSpec.getCurrentGesuch()
            .gesuchIdPath(gesuchId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec.class);

        trancheUpdateDtoSpec = new GesuchTrancheUpdateDtoSpec();
        trancheUpdateDtoSpec.setId(gesuch.getGesuchTrancheToWorkWith().getId());
        trancheUpdateDtoSpec.setGesuchFormular(currentFormular);

        assertThat(gesuch.getGesuchTrancheToWorkWith(), notNullValue());
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGueltigAb(), is(GUELTIGKEIT_PERIODE_23_24.getGueltigAb()));
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGueltigBis(), is(GUELTIGKEIT_PERIODE_23_24.getGueltigBis()));
    }

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void updateWithNotExistingGesuchTranche() {
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecAusbildung();
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(UUID.randomUUID());
        gesuchApiSpec.updateGesuch()
            .gesuchIdPath(gesuchId)
            .body(gesuchUpdateDTO)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(5)
    void addPersonInAusbildung() {
        final var pia = PersonInAusbildungUpdateDtoSpecModel.personInAusbildungUpdateDtoSpec();
        currentFormular.setPersonInAusbildung(pia);
        final var returnedGesuch = patchAndValidate();

        assertThat(
            returnedGesuch.getGesuchTrancheToWorkWith()
                .getGesuchFormular()
                .getPersonInAusbildung()
                .getAdresse()
                .getId(),
            is(notNullValue())
        );

        // Set the Adresse ID from the returned Gesuch, so follow-up calls won't want to change it
        currentFormular.getPersonInAusbildung()
            .getAdresse()
            .setId(returnedGesuch.getGesuchTrancheToWorkWith()
                .getGesuchFormular()
                .getPersonInAusbildung()
                .getAdresse()
                .getId()
            );
    }

    @Test
    @TestAsGesuchsteller
    @Order(6)
    void addAusbildung() {
        final var ausbildung = AusbildungUpdateDtoSpecModel.ausbildungUpdateDtoSpec();
        currentFormular.setAusbildung(ausbildung);
        // Don't validate for now, as the LebenslaufItem validator is broken
        patchGesuch();
    }

    @Test
    @TestAsGesuchsteller
    @Order(7)
    void addLebenslauf() {
        final var lebenslaufItems = LebenslaufItemUpdateDtoSpecModel.lebenslaufItemUpdateDtoSpecs();
        currentFormular.setLebenslaufItems(lebenslaufItems);
        patchAndValidate();
    }

    @Test
    @TestAsGesuchsteller
    @Order(8)
    void addFamiliensituation() {
        final var famsit = FamiliensituationUpdateDtoSpecModel.familiensituationUpdateDtoSpec();
        currentFormular.setFamiliensituation(famsit);
        patchAndValidate();
    }

    @Test
    @TestAsGesuchsteller
    @Order(9)
    void addEltern() {
        final var eltern = ElternUpdateDtoSpecModel.elternUpdateDtoSpecs(2);
        eltern.get(0).setElternTyp(ElternTypDtoSpec.VATER);
        eltern.get(0).setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_VATTER);
        eltern.get(1).setElternTyp(ElternTypDtoSpec.MUTTER);
        eltern.get(1).setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_MUTTER);

        currentFormular.setElterns(eltern);
        patchAndValidate();
    }

    @Test
    @TestAsGesuchsteller
    @Order(10)
    void addSteuerdaten() {
        final var steuerdaten = SteuerdatenUpdateTabsDtoSpecModel.steuerdatenDtoSpec(SteuerdatenTypDtoSpec.FAMILIE);
        currentFormular.setSteuerdaten(List.of(steuerdaten));
        patchAndValidate();
    }

    @Test
    @TestAsGesuchsteller
    @Order(11)
    void addGeschwister() {
        final var geschwister = GeschwisterUpdateDtoSpecModel.geschwisterUpdateDtoSpecs();
        currentFormular.setGeschwisters(geschwister);
        patchAndValidate();
    }

    @Test
    @TestAsGesuchsteller
    @Order(12)
    void addPartner() {
        // Set partner to null as Zivilstand is LEDIG
        final var partner = (PartnerUpdateDtoSpec) null;
        currentFormular.setPartner(partner);
        patchAndValidate();
    }

    @Test
    @TestAsGesuchsteller
    @Order(13)
    void addKinder() {
        // Our PiA has no kinder
        final List<KindUpdateDtoSpec> kinder = List.of();
        currentFormular.setKinds(kinder);
        patchAndValidate();
    }

    @Test
    @TestAsGesuchsteller
    @Order(14)
    void addAuszahlung() {
        final var auszahlung = AuszahlungUpdateDtoSpecModel.auszahlungUpdateDtoSpec();
        currentFormular.setAuszahlung(auszahlung);
        patchAndValidate();
    }

    @Test
    @TestAsGesuchsteller
    @Order(15)
    void addEinnahmenKosten() {
        final var einnahmenKosten = EinnahmenKostenUpdateDtoSpecModel.einnahmenKostenUpdateDtoSpec();
        currentFormular.setEinnahmenKosten(einnahmenKosten);
        patchAndValidate();
    }

    @Test
    @TestAsGesuchsteller
    @Order(16)
    void addDokumente() {
        for (final var dokTyp : DokumentTypDtoSpec.values()) {
            final var file = TestUtil.getTestPng();
            TestUtil.uploadFile(dokumentApiSpec, gesuchTrancheId, dokTyp, file);
        }

        validatePage(false);
    }

    @Test
    @TestAsGesuchsteller
    @Order(17)
    void removeSuperfluousDocuments() {
        // getGesuchDokumente also removes superfluous documents from the Gesuch
        // This is needed so the follow check if only necessary documents are saved works
        gesuchTrancheApiSpec.getGesuchDokumente()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(18)
    void noSuperfluousDocuments() {
        final var expectedDokumentTypes = new DokumentTypDtoSpec[] {
            DokumentTypDtoSpec.EK_LOHNABRECHNUNG,
            DokumentTypDtoSpec.EK_BELEG_BETREUUNGSKOSTEN_KINDER,
            DokumentTypDtoSpec.EK_MIETVERTRAG,
            DokumentTypDtoSpec.EK_BELEG_OV_ABONNEMENT,
            DokumentTypDtoSpec.EK_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO,
            DokumentTypDtoSpec.EK_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN,
            DokumentTypDtoSpec.EK_BELEG_BEZAHLTE_RENTEN,
            DokumentTypDtoSpec.EK_VERFUEGUNG_GEMEINDE_INSTITUTION,
            DokumentTypDtoSpec.EK_BELEG_KINDERZULAGEN,
            DokumentTypDtoSpec.AUSZAHLUNG_ABTRETUNGSERKLAERUNG,
            DokumentTypDtoSpec.GESCHWISTER_BESTAETIGUNG_AUSBILDUNGSSTAETTE,
            DokumentTypDtoSpec.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE,
            DokumentTypDtoSpec.ELTERN_SOZIALHILFEBUDGET_MUTTER,
            DokumentTypDtoSpec.ELTERN_SOZIALHILFEBUDGET_VATER,
            DokumentTypDtoSpec.ELTERN_ERGAENZUNGSLEISTUNGEN_MUTTER,
            DokumentTypDtoSpec.ELTERN_ERGAENZUNGSLEISTUNGEN_VATER,
            DokumentTypDtoSpec.PERSON_SOZIALHILFEBUDGET
        };

        var gesuchDokumente = gesuchTrancheApiSpec.getGesuchDokumente()
            .gesuchTrancheIdPath(gesuchTrancheId)
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
    @TestAsGesuchsteller
    @Order(19)
    void gesuchEinreichenValidation() {
        final var validationReport = gesuchTrancheApiSpec.gesuchTrancheEinreichenValidieren()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(ValidationReportDtoSpec.class);

        assertThat(
            validationReport.getValidationErrors().toString(),
            validationReport.getValidationErrors().size(),
            not(greaterThan(0))
        );
    }

    @Test
    @TestAsGesuchsteller
    @Order(20)
    void gesuchEinreichen() {
        gesuchApiSpec.gesuchEinreichen()
            .gesuchIdPath(gesuchId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.ACCEPTED.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(21)
    void gesuchNotificationTest() {
        var notifications = notificationApiSpec.getNotificationsForCurrentUser()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(NotificationDtoSpec[].class);
        Arrays.stream(notifications).forEach(notification -> {
            assertTrue(!notification.getNotificationText().isEmpty());
        });
    }


    @Test
    @TestAsAdmin
    @Order(30)
    @AlwaysRun
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuchId);
    }

    private GesuchDtoSpec patchAndValidate() {
        final var returnedGesuch = patchGesuch();
        validatePage();
        return returnedGesuch;
    }

    private GesuchDtoSpec patchGesuch() {
        final var gesuchUpdateDtoSpec = new GesuchUpdateDtoSpec();
        gesuchUpdateDtoSpec.setGesuchTrancheToWorkWith(trancheUpdateDtoSpec);

        gesuchApiSpec.updateGesuch()
            .gesuchIdPath(gesuchId)
            .body(gesuchUpdateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());

        return gesuchApiSpec.getCurrentGesuch()
            .gesuchIdPath(gesuchId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDtoSpec.class);
    }

    private void validatePage() {
        validatePage(true);
    }

    private void validatePage(final boolean allowWarnings) {
        final var report = gesuchTrancheApiSpec
            .validateGesuchTranchePages()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
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
}
