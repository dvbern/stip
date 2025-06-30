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

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsSuperUser;
import ch.dvbern.stip.api.generator.api.model.gesuch.SteuerdatenUpdateTabsDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.StepwiseExtension.AlwaysRun;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.AuszahlungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchTrancheApiSpec;
import ch.dvbern.stip.generated.api.SteuerdatenApiSpec;
import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDtoSpec;
import ch.dvbern.stip.generated.dto.CustomDokumentTypCreateDtoSpec;
import ch.dvbern.stip.generated.dto.DokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.FallDashboardItemDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheListDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDto;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDtoSpec;
import ch.dvbern.stip.generated.dto.SteuerdatenTypDtoSpec;
import ch.dvbern.stip.generated.dto.UnterschriftenblattDokumentTypDtoSpec;
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

import static ch.dvbern.stip.api.util.TestConstants.TEST_PNG_FILE_LOCATION;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class GesuchTrancheAenderungEinbindenTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec =
        GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final SteuerdatenApiSpec steuerdatenApiSpec = SteuerdatenApiSpec.steuerdaten(RequestSpecUtil.quarkusSpec());
    private final AuszahlungApiSpec auszahlungApiSpec = AuszahlungApiSpec.auszahlung(RequestSpecUtil.quarkusSpec());

    private GesuchTrancheListDtoSpec gesuchtranchen;
    private GesuchDtoSpec gesuch;
    private GesuchWithChangesDtoSpec gesuchWithChanges;
    private GesuchTrancheDtoSpec aenderung;
    private UUID customDokumentId;
    private UUID aenderungId;

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
        TestUtil.fillGesuchWithAuszahlung(gesuchApiSpec, dokumentApiSpec, auszahlungApiSpec, gesuch);
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void gesuchEinreichen() {
        gesuchApiSpec.gesuchEinreichenGs()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @TestAsSachbearbeiter
    @Order(4)
    @Test
    void makeGesuchVerfuegt() {
        gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
        final var steuerdatenUpdateDto =
            SteuerdatenUpdateTabsDtoSpecModel.steuerdatenDtoSpec(SteuerdatenTypDtoSpec.FAMILIE);
        steuerdatenApiSpec.updateSteuerdaten()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .body(List.of(steuerdatenUpdateDto))
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());

        // Upload Unterschriftenblatt to "skip" Verfuegt state
        TestUtil.uploadUnterschriftenblatt(
            dokumentApiSpec,
            gesuch.getId(),
            UnterschriftenblattDokumentTypDtoSpec.GEMEINSAM,
            TestUtil.getTestPng()
        ).assertThat().statusCode(Response.Status.CREATED.getStatusCode());

        var modifiableDokTypeList = Arrays.stream(DokumentTypDtoSpec.values()).toList();
        modifiableDokTypeList.forEach(dokType -> {
            var dokToAccept = dokumentApiSpec.getGesuchDokumentForTypSB()
                .dokumentTypPath(dokType)
                .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .assertThat()
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .body()
                .as(NullableGesuchDokumentDto.class);

            if (dokToAccept.getValue() != null) {
                dokumentApiSpec.gesuchDokumentAkzeptieren()
                    .gesuchDokumentIdPath(dokToAccept.getValue().getId())
                    .execute(TestUtil.PEEK_IF_ENV_SET)
                    .then()
                    .assertThat()
                    .statusCode(Status.NO_CONTENT.getStatusCode());
            }
        });

        gesuchApiSpec.changeGesuchStatusToVerfuegt()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
        gesuchWithChanges = gesuchApiSpec.getInitialTrancheChanges()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
        assertThat(gesuchWithChanges.getChanges()).hasSize(1);
    }

    @Test
    @Order(5)
    @TestAsSachbearbeiter
    void changeToFinalState() {
        gesuchApiSpec.changeGesuchStatusToVersendet()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());

        gesuchWithChanges = gesuchApiSpec.getGesuchSB()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);

        assertThat(gesuchWithChanges.getGesuchStatus())
            .isIn(List.of(GesuchstatusDtoSpec.STIPENDIENANSPRUCH, GesuchstatusDtoSpec.KEIN_STIPENDIENANSPRUCH));
    }

    @Test
    @TestAsGesuchsteller
    @Order(6)
    void createFirstAenderungsantrag() {
        aenderung = gesuchTrancheApiSpec.createAenderungsantrag()
            .gesuchIdPath(gesuch.getId())
            .body(
                new CreateAenderungsantragRequestDtoSpec().comment("aenderung1")
                    .start(gesuch.getGesuchTrancheToWorkWith().getGueltigAb().plusMonths(4))
                    .end(gesuch.getGesuchTrancheToWorkWith().getGueltigBis())
            )
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchTrancheDtoSpec.class);
        aenderungId = aenderung.getId();
    }

    @Test
    @TestAsGesuchsteller
    @Order(7)
    void aenderungEinreichen() {
        gesuchTrancheApiSpec.aenderungEinreichen()
            .aenderungIdPath(aenderungId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(
                Status.NO_CONTENT.getStatusCode()
            );
    }

    @Test
    @TestAsSachbearbeiter
    @Order(8)
    void aenderungAddCustomDokument() {
        CustomDokumentTypCreateDtoSpec customDokumentTypCreateDtoSpec = new CustomDokumentTypCreateDtoSpec();
        customDokumentTypCreateDtoSpec.setType("test");
        customDokumentTypCreateDtoSpec.setDescription("test description");
        customDokumentTypCreateDtoSpec.setTrancheId(aenderung.getId());
        final var createdGesuchDokumentWithCustomType = dokumentApiSpec.createCustomDokumentTyp()
            .body(customDokumentTypCreateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDokumentDto.class);
        customDokumentId = createdGesuchDokumentWithCustomType.getCustomDokumentTyp().getId();
    }

    @Test
    @TestAsSachbearbeiter
    @Order(9)
    void aenderungFehlendeDokumenteUebermitteln() {
        gesuchTrancheApiSpec.aenderungFehlendeDokumenteUebermitteln()
            .gesuchTrancheIdPath(aenderungId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(10)
    void test_upload_custom_gesuchdokuments() {
        File file = new File(TEST_PNG_FILE_LOCATION);
        TestUtil.uploadCustomDokumentFile(dokumentApiSpec, customDokumentId, file);
    }

    @Test
    @TestAsGesuchsteller
    @Order(11)
    void aenderungFehlendeDokumenteEinreichen() {
        gesuchTrancheApiSpec.aenderungFehlendeDokumenteEinreichen()
            .gesuchTrancheIdPath(aenderungId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(12)
    void aenderungAkzeptieren() {
        var nullableGesuchDokumentDto = dokumentApiSpec.getCustomGesuchDokumentForTypSB()
            .customDokumentTypIdPath(customDokumentId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(NullableGesuchDokumentDtoSpec.class);
        dokumentApiSpec.gesuchDokumentAkzeptieren()
            .gesuchDokumentIdPath(nullableGesuchDokumentDto.getValue().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());
        gesuchTrancheApiSpec.aenderungAkzeptieren()
            .aenderungIdPath(aenderungId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());

        gesuchtranchen = gesuchTrancheApiSpec.getAllTranchenForGesuchSB()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(
                GesuchTrancheListDtoSpec.class
            );
        assertThat(gesuchtranchen.getTranchen()).hasSize(3);
    }

    @Test
    @TestAsSachbearbeiter
    @Order(13)
    void aenderungAkzeptiertZurueckweisen() {
        gesuchApiSpec.gesuchZurueckweisen()
            .gesuchTrancheIdPath(gesuchtranchen.getTranchen().get(0).getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());

        gesuchtranchen = gesuchTrancheApiSpec.getAllTranchenForGesuchSB()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(
                GesuchTrancheListDtoSpec.class
            );
        assertThat(gesuchtranchen.getTranchen()).hasSize(2);
    }

    @Test
    @TestAsGesuchsteller
    @Order(14)
    void aenderungEinreichenAgain() {
        final var fallDashboardItem = gesuchApiSpec.getGsDashboard()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(FallDashboardItemDto.class);
        aenderungId = fallDashboardItem.getAusbildungDashboardItems()
            .get(0)
            .getGesuchs()
            .get(0)
            .getOffeneAenderung()
            .getId();

        gesuchTrancheApiSpec.aenderungEinreichen()
            .aenderungIdPath(
                aenderungId
            )
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(
                Status.NO_CONTENT.getStatusCode()
            );
    }

    @Test
    @TestAsSachbearbeiter
    @Order(15)
    void aenderungAkzeptierenAgain() {
        gesuchTrancheApiSpec.aenderungAkzeptieren()
            .aenderungIdPath(aenderungId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());

        gesuchtranchen = gesuchTrancheApiSpec.getAllTranchenForGesuchSB()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(
                GesuchTrancheListDtoSpec.class
            );
        assertThat(gesuchtranchen.getTranchen()).hasSize(3);
    }

    @TestAsSachbearbeiter
    @Order(16)
    @Test
    void makeGesuchVerfuegtAgain() {
        gesuchApiSpec.changeGesuchStatusToVerfuegt()
            .gesuchTrancheIdPath(gesuchtranchen.getTranchen().get(0).getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(17)
    @TestAsSachbearbeiter
    void changeToFinalStateAgain() {
        gesuchApiSpec.changeGesuchStatusToVersendet()
            .gesuchTrancheIdPath(gesuchtranchen.getTranchen().get(0).getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());

        gesuchWithChanges = gesuchApiSpec.getGesuchSB()
            .gesuchTrancheIdPath(gesuchtranchen.getTranchen().get(0).getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);

        assertThat(gesuchWithChanges.getGesuchStatus())
            .isIn(List.of(GesuchstatusDtoSpec.STIPENDIENANSPRUCH, GesuchstatusDtoSpec.KEIN_STIPENDIENANSPRUCH));
    }

    @Test
    @TestAsSuperUser
    @Order(99)
    @AlwaysRun
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }
}
