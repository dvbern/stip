/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.gesuch.resource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsSuperUser;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.generator.api.model.gesuch.EinnahmenKostenUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.ElternUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.FamiliensituationUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.GeschwisterUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.LebenslaufItemUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.PersonInAusbildungUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.SteuererklaerungUpdateTabsDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.StepwiseExtension.AlwaysRun;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.AuszahlungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchTrancheApiSpec;
import ch.dvbern.stip.generated.api.NotificationApiSpec;
import ch.dvbern.stip.generated.dto.DokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.ElternTypDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDokumentDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.KindUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.NotificationDtoSpec;
import ch.dvbern.stip.generated.dto.PartnerUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.SteuerdatenTypDtoSpec;
import ch.dvbern.stip.generated.dto.ValidationReportDto;
import ch.dvbern.stip.generated.dto.ValidationReportDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
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

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_AUSBILDUNG_ONLY_ONE_GESUCH_PER_YEAR;
import static ch.dvbern.stip.api.util.TestConstants.GUELTIGKEIT_PERIODE_CURRENT;
import static ch.dvbern.stip.api.util.TestUtil.DATE_TIME_FORMATTER;
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
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec =
        GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final NotificationApiSpec notificationApiSpec =
        NotificationApiSpec.notification(RequestSpecUtil.quarkusSpec());
    private final AuszahlungApiSpec auszahlungApiSpec = AuszahlungApiSpec.auszahlung(RequestSpecUtil.quarkusSpec());

    private UUID gesuchId;
    private UUID gesuchTrancheId;
    private UUID ausbildungId;
    private final GesuchFormularUpdateDtoSpec currentFormular = new GesuchFormularUpdateDtoSpec();
    private GesuchTrancheUpdateDtoSpec trancheUpdateDtoSpec;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void testCreateEndpoint() {
        final var gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        gesuchId = gesuch.getId();
        gesuchTrancheId = gesuch.getGesuchTrancheToWorkWith().getId();
        ausbildungId = gesuch.getAusbildungId();
        gesuchTrancheId = gesuch.getGesuchTrancheToWorkWith().getId();

        assertThat(
            "Newly created Gesuch is not IN_BEARBEITUNG_GS",
            gesuch.getGesuchStatus(),
            is(GesuchstatusDtoSpec.IN_BEARBEITUNG_GS)
        );
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void gesuchCreateFail() {
        var validationReport = gesuchApiSpec.createGesuch()
            .body(new GesuchCreateDtoSpec().ausbildungId(ausbildungId))
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.BAD_REQUEST.getStatusCode())
            .extract()
            .body()
            .as(ValidationReportDtoSpec.class);

        assertThat(
            validationReport.getValidationErrors().get(0).getMessageTemplate(),
            is(VALIDATION_AUSBILDUNG_ONLY_ONE_GESUCH_PER_YEAR)
        );
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void gesuchTrancheCreated() {
        final var gesuch = gesuchApiSpec.getGesuchGS()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);

        trancheUpdateDtoSpec = new GesuchTrancheUpdateDtoSpec();
        trancheUpdateDtoSpec.setId(gesuch.getGesuchTrancheToWorkWith().getId());
        trancheUpdateDtoSpec.setGesuchFormular(currentFormular);

        assertThat(gesuch.getGesuchTrancheToWorkWith(), notNullValue());
        assertThat(
            gesuch.getGesuchTrancheToWorkWith().getGueltigAb(),
            is(GUELTIGKEIT_PERIODE_CURRENT.getGueltigAb())
        );
        assertThat(
            gesuch.getGesuchTrancheToWorkWith().getGueltigBis(),
            is(GUELTIGKEIT_PERIODE_CURRENT.getGueltigBis())
        );
    }

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void updateWithNotExistingGesuchTranche() {
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecPersonInAusbildung();
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
        final var returnedGesuch = patchGesuch();

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
            .setId(
                returnedGesuch.getGesuchTrancheToWorkWith()
                    .getGesuchFormular()
                    .getPersonInAusbildung()
                    .getAdresse()
                    .getId()
            );
    }

    @Test
    @TestAsGesuchsteller
    @Order(6)
    void addLebenslauf() {
        final var lebenslaufItems = LebenslaufItemUpdateDtoSpecModel.lebenslaufItemUpdateDtoSpecs();
        lebenslaufItems.get(0)
            .setVon(
                currentFormular.getPersonInAusbildung()
                    .getGeburtsdatum()
                    .plusYears(16)
                    .withMonth(8)
                    .format(DATE_TIME_FORMATTER)
            );
        currentFormular.setLebenslaufItems(lebenslaufItems);
        patchAndValidate();
    }

    @Test
    @TestAsGesuchsteller
    @Order(7)
    void addFamiliensituation() {
        final var famsit = FamiliensituationUpdateDtoSpecModel.familiensituationUpdateDtoSpec();
        currentFormular.setFamiliensituation(famsit);
        patchAndValidate();
    }

    @Test
    @TestAsGesuchsteller
    @Order(8)
    void addEltern() {
        final var eltern = ElternUpdateDtoSpecModel.elternUpdateDtoSpecs(2);
        eltern.get(0).setElternTyp(ElternTypDtoSpec.VATER);
        eltern.get(0).setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_VATER);
        eltern.get(1).setElternTyp(ElternTypDtoSpec.MUTTER);
        eltern.get(1).setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_MUTTER);

        currentFormular.setElterns(eltern);
        patchAndValidate();
    }

    @Test
    @TestAsGesuchsteller
    @Order(9)
    void addSteuererklaerung() {
        final var steuererklaerung =
            SteuererklaerungUpdateTabsDtoSpecModel.steuererklaerungDtoSpec(SteuerdatenTypDtoSpec.FAMILIE);
        currentFormular.setSteuererklaerung(List.of(steuererklaerung));
        patchAndValidate();
    }

    @Test
    @TestAsGesuchsteller
    @Order(10)
    void addGeschwister() {
        final var geschwister = GeschwisterUpdateDtoSpecModel.geschwisterUpdateDtoSpecs();
        currentFormular.setGeschwisters(geschwister);
        patchAndValidate();
    }

    @Test
    @TestAsGesuchsteller
    @Order(11)
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
        final var auszahlung = TestUtil.getAuszahlungUpdateDtoSpec();
        final var fall = TestUtil.getFall(fallApiSpec).orElseThrow();
        TestUtil.fillAuszahlung(fall.getId(), auszahlungApiSpec, auszahlung);
    }

    @Test
    @TestAsGesuchsteller
    @Order(15)
    void addEinnahmenKosten() {
        final var einnahmenKosten = EinnahmenKostenUpdateDtoSpecModel.einnahmenKostenUpdateDtoSpec();
        currentFormular.setEinnahmenKosten(einnahmenKosten);
        patchAndValidate();
    }

    // make sure Bug KSTIP-1883 is fixed
    @Test
    @TestAsGesuchsteller
    @Order(16)
    void validateResetOfEinnahmenKosten() {
        final var minderjaehrigBirthDate = currentFormular.getPersonInAusbildung().getGeburtsdatum();
        final var volljaehrigBirthDate = LocalDate.now().minusYears(19);
        // set pia to volljaehrig
        currentFormular.getPersonInAusbildung().setGeburtsdatum(volljaehrigBirthDate);
        currentFormular.getEinnahmenKosten().setVermoegen(100);
        patchGesuch();

        // reset pia to minderjaehrig again
        currentFormular.getPersonInAusbildung().setGeburtsdatum(minderjaehrigBirthDate);
        patchGesuch();

        final var lebenslaufItems = LebenslaufItemUpdateDtoSpecModel.lebenslaufItemUpdateDtoSpecs();
        lebenslaufItems.get(0)
            .setVon(
                currentFormular.getPersonInAusbildung()
                    .getGeburtsdatum()
                    .plusYears(16)
                    .withMonth(8)
                    .format(DATE_TIME_FORMATTER)
            );
        currentFormular.setLebenslaufItems(lebenslaufItems);
        patchAndValidate();
    }

    @Test
    @TestAsGesuchsteller
    @Order(17)
    void addDokumente() {
        for (final var dokTyp : DokumentTypDtoSpec.values()) {
            final var file = TestUtil.getTestPng();
            TestUtil.uploadFile(dokumentApiSpec, gesuchTrancheId, dokTyp, file);
        }

        validatePage(false);
    }

    @Test
    @TestAsGesuchsteller
    @Order(18)
    void removeSuperfluousDocuments() {
        // getGesuchDokumente also removes superfluous documents from the Gesuch
        // This is needed so the follow check if only necessary documents are saved works
        gesuchTrancheApiSpec.getGesuchDokumenteGS()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(19)
    void noSuperfluousDocuments() {
        final var expectedDokumentTypes = new DokumentTypDtoSpec[] {
            DokumentTypDtoSpec.AUSBILDUNG_BESTAETIGUNG_AUSBILDUNGSSTAETTE,
            DokumentTypDtoSpec.EK_LOHNABRECHNUNG,
            DokumentTypDtoSpec.EK_BELEG_BETREUUNGSKOSTEN_KINDER,
            DokumentTypDtoSpec.EK_MIETVERTRAG,
            DokumentTypDtoSpec.EK_BELEG_OV_ABONNEMENT,
            DokumentTypDtoSpec.EK_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO,
            DokumentTypDtoSpec.EK_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN,
            DokumentTypDtoSpec.EK_BELEG_BEZAHLTE_RENTEN,
            DokumentTypDtoSpec.EK_VERFUEGUNG_GEMEINDE_INSTITUTION,
            DokumentTypDtoSpec.EK_BELEG_KINDERZULAGEN,
            DokumentTypDtoSpec.GESCHWISTER_BESTAETIGUNG_AUSBILDUNGSSTAETTE,
            DokumentTypDtoSpec.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE,
            DokumentTypDtoSpec.ELTERN_SOZIALHILFEBUDGET_MUTTER,
            DokumentTypDtoSpec.ELTERN_SOZIALHILFEBUDGET_VATER,
            DokumentTypDtoSpec.STEUERERKLAERUNG_ERGAENZUNGSLEISTUNGEN_FAMILIE,
            DokumentTypDtoSpec.PERSON_SOZIALHILFEBUDGET,
            DokumentTypDtoSpec.PERSON_MIETVERTRAG,
            DokumentTypDtoSpec.STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_FAMILIE
        };

        var gesuchDokumente = gesuchTrancheApiSpec.getGesuchDokumenteGS()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
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
    @Order(22)
    void gesuchEinreichenValidation() {
        final var validationReport = gesuchTrancheApiSpec.gesuchTrancheEinreichenValidierenGS()
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
    @TestAsSachbearbeiter
    @Order(23)
    void gesuchEinreichenAsSBShouldFail() {
        gesuchApiSpec.gesuchEinreichenGs()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(24)
    void gesuchEinreichen() {
        gesuchApiSpec.gesuchEinreichenGs()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(25)
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
    @TestAsSuperUser
    @Order(99)
    @AlwaysRun
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuchId);
    }

    private GesuchWithChangesDtoSpec patchAndValidate() {
        final var returnedGesuch = patchGesuch();
        validatePage();
        return returnedGesuch;
    }

    private GesuchWithChangesDtoSpec patchGesuch() {
        final var gesuchUpdateDtoSpec = new GesuchUpdateDtoSpec();
        gesuchUpdateDtoSpec.setGesuchTrancheToWorkWith(trancheUpdateDtoSpec);

        gesuchApiSpec.updateGesuch()
            .gesuchIdPath(gesuchId)
            .body(gesuchUpdateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        return gesuchApiSpec.getGesuchGS()
            .gesuchTrancheIdPath(trancheUpdateDtoSpec.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
    }

    private void validatePage() {
        validatePage(true);
    }

    private void validatePage(final boolean allowWarnings) {
        final var report = gesuchTrancheApiSpec
            .validateGesuchTranchePagesGS()
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
