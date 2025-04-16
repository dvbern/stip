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

package ch.dvbern.stip.api.gesuchtranche.resource;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.StepwiseExtension.AlwaysRun;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.BenutzerApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchTrancheApiSpec;
import ch.dvbern.stip.generated.dto.AusbildungssituationDtoSpec;
import ch.dvbern.stip.generated.dto.CreateGesuchTrancheRequestDtoSpec;
import ch.dvbern.stip.generated.dto.DokumenteToUploadDtoSpec;
import ch.dvbern.stip.generated.dto.GeschwisterUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheListDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import ch.dvbern.stip.generated.dto.UnterschriftenblattDokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.WohnsitzDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class GesuchTrancheCreateTest {
    /**
     * Goal: create a new GesuchTranche
     * & verify that superflous GesuchDokuments are only being deleted on one tranche
     */
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec =
        GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final BenutzerApiSpec benutzerApiSpec = BenutzerApiSpec.benutzer(RequestSpecUtil.quarkusSpec());

    private GesuchDtoSpec gesuch;

    private GesuchTrancheDtoSpec tranche1;
    private GesuchTrancheDtoSpec tranche2;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void gesuchErstellen() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void fillGesuch() {
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void prepareAddRequiredDocument() {
        // preparation for the last few tests
        addDocument(gesuch.getGesuchTrancheToWorkWith().getId());
    }

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void gesuchEinreichen() {
        gesuchApiSpec.gesuchEinreichen()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(5)
    void prepareSachbearbeiter() {
        benutzerApiSpec.prepareCurrentBenutzer()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(6)
    void createTrancheFail() {
        gesuchTrancheApiSpec.createGesuchTrancheCopy()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(7)
    void setStatusInBearbeitungSb() {
        gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(8)
    void createTrancheSuccess() {
        final var createGesuchTrancheRequestDtoSpec = new CreateGesuchTrancheRequestDtoSpec();
        createGesuchTrancheRequestDtoSpec.setStart(gesuch.getGesuchTrancheToWorkWith().getGueltigAb());
        createGesuchTrancheRequestDtoSpec.setEnd(gesuch.getGesuchTrancheToWorkWith().getGueltigAb().plusMonths(5));
        createGesuchTrancheRequestDtoSpec.setComment("Bla");

        gesuchTrancheApiSpec.createGesuchTrancheCopy()
            .gesuchIdPath(gesuch.getId())
            .body(createGesuchTrancheRequestDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    UUID tranche1Id;
    UUID tranche2Id;

    @Test
    @TestAsSachbearbeiter
    @Order(9)
    void getTranchen() {
        var result = gesuchTrancheApiSpec.getAllTranchenForGesuchSB()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchTrancheListDtoSpec.class);
        assertThat(result.getTranchen().size(), is(2));
        tranche1Id = result.getTranchen().get(0).getId();
        tranche2Id = result.getTranchen().get(1).getId();
    }

    @TestAsSachbearbeiter
    @Order(10)
    @Test
    void makeGesuchVerfuegt() {
        // TODO KSTIP-1631: Make Gesuch the correct state
        gesuchApiSpec.getInitialTrancheChanges()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());

        // Upload Unterschriftenblatt to "skip" Verfuegt state
        TestUtil.uploadUnterschriftenblatt(
            dokumentApiSpec,
            gesuch.getId(),
            UnterschriftenblattDokumentTypDtoSpec.GEMEINSAM,
            TestUtil.getTestPng()
        ).assertThat().statusCode(Response.Status.CREATED.getStatusCode());

        gesuchApiSpec.changeGesuchStatusToVerfuegt()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
        var gesuchWithChanges =
            gesuchApiSpec.getInitialTrancheChanges()
                .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .extract()
                .body()
                .as(GesuchWithChangesDtoSpec.class);
        Assertions.assertThat(gesuchWithChanges.getChanges()).hasSize(1);
    }

    @Test
    @TestAsSachbearbeiter
    @Order(11)
    void getInitialTrancheChangesAsSBInGesuchstatusEingereicht() {
        // test for each tranche if SB gets correct status
        gesuchApiSpec.getInitialTrancheChanges()
            .gesuchTrancheIdPath(tranche1Id)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
        gesuchApiSpec.getInitialTrancheChanges()
            .gesuchTrancheIdPath(tranche2Id)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(12)
    void getInitialTrancheChangesAsGSInGesuchstatusEingereicht() {
        // test for each tranche if SB gets correct status
        gesuchApiSpec.getInitialTrancheChanges()
            .gesuchTrancheIdPath(tranche1Id)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
        gesuchApiSpec.getInitialTrancheChanges()
            .gesuchTrancheIdPath(tranche2Id)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(13)
    void testIfSuperflousDocumentOnlyGetsDeletedOnOneTranche() {
        var tranchen = gesuchTrancheApiSpec.getAllTranchenForGesuchSB()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchTrancheListDtoSpec.class);

        // make the formlerly added document (by GS) superflous on tranche 2:
        // therefore, remove & re-add the document
        removeDocument(tranchen.getTranchen().get(1).getId());
        addDocument(tranchen.getTranchen().get(1).getId());

        var documentsToUploadOfTranche1 =
            gesuchTrancheApiSpec.getDocumentsToUploadSB()
                .gesuchTrancheIdPath(tranchen.getTranchen().get(0).getId())
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .extract()
                .body()
                .as(DokumenteToUploadDtoSpec.class);
        assertThat(documentsToUploadOfTranche1.getRequired().size(), is(0));
        var documentsToUploadOfTranche2 =
            gesuchTrancheApiSpec.getDocumentsToUploadSB()
                .gesuchTrancheIdPath(tranchen.getTranchen().get(1).getId())
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .extract()
                .body()
                .as(DokumenteToUploadDtoSpec.class);
        assertThat(documentsToUploadOfTranche2.getRequired().size(), is(1));

        // verify that the superflous document only gets deleted on the correct tranche - not on both...
        var dokumentsOfTranche1 = gesuchTrancheApiSpec.getGesuchDokumenteSB()
            .gesuchTrancheIdPath(tranchen.getTranchen().get(0).getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDokumentDto[].class);
        var dokumentsOfTranche2 = gesuchTrancheApiSpec.getGesuchDokumenteSB()
            .gesuchTrancheIdPath(tranchen.getTranchen().get(1).getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDokumentDto[].class);
        assertThat(dokumentsOfTranche1.length, is(greaterThan(dokumentsOfTranche2.length)));
    }

    @Test
    @TestAsGesuchsteller
    @Order(14)
    void getTranchenAsGSShouldReturnStateOfGesuchEingereicht() {
        // the gesuch (tranchen) of state eingereicht should be returned to GS
        // so the total count of (visible) tranchen should be 1 instead of 2
        var result = gesuchTrancheApiSpec.getAllTranchenForGesuchGS()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchTrancheListDtoSpec.class);
        assertThat(result.getTranchen().size(), is(2));
    }

    @Test
    @TestAsAdmin
    @Order(99)
    @AlwaysRun
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }

    private void addDocument(UUID trancheId) {
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecGeschwister();
        var geschwisterUpdate = new GeschwisterUpdateDtoSpec();
        geschwisterUpdate.setAusbildungssituation(AusbildungssituationDtoSpec.IN_AUSBILDUNG);
        geschwisterUpdate.setNachname("test");
        geschwisterUpdate.setVorname("test");
        geschwisterUpdate.setGeburtsdatum(LocalDate.now().minusYears(18));
        geschwisterUpdate.setWohnsitz(WohnsitzDtoSpec.EIGENER_HAUSHALT);
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().setGeschwisters(List.of(geschwisterUpdate));

        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(trancheId);
        gesuchApiSpec.updateGesuch()
            .gesuchIdPath(gesuch.getId())
            .body(gesuchUpdateDTO)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    private void removeDocument(UUID trancheId) {
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecGeschwister();
        var geschwisterUpdate = new GeschwisterUpdateDtoSpec();
        geschwisterUpdate.setAusbildungssituation(AusbildungssituationDtoSpec.VORSCHULPFLICHTIG);
        geschwisterUpdate.setNachname("test");
        geschwisterUpdate.setVorname("test");
        geschwisterUpdate.setGeburtsdatum(LocalDate.now().minusYears(18));
        geschwisterUpdate.setWohnsitz(WohnsitzDtoSpec.EIGENER_HAUSHALT);
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().setGeschwisters(List.of(geschwisterUpdate));

        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(trancheId);
        gesuchApiSpec.updateGesuch()
            .gesuchIdPath(gesuch.getId())
            .body(gesuchUpdateDTO)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }
}
