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

import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsSuperUser;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.AuszahlungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchTrancheApiSpec;
import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDtoSpec;
import ch.dvbern.stip.generated.dto.CustomDokumentTypCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheListDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheTypDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDto;
import ch.dvbern.stip.generated.dto.UnterschriftenblattDokumentTypDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class GesuchTrancheCustomDokumentOnAenderungTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec =
        GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final AuszahlungApiSpec auszahlungApiSpec = AuszahlungApiSpec.auszahlung(RequestSpecUtil.quarkusSpec());

    private GesuchTrancheListDtoSpec gesuchtranchen;
    private GesuchDtoSpec gesuch;
    private GesuchWithChangesDtoSpec gesuchWithChanges;
    private UUID customDokumentId;

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

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void gesuchWithChangesShouldNotBeAccessibleForGSBeforeVERFUEGT() {
        gesuchApiSpec.getInitialTrancheChanges()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(5)
    void createFirstAenderungsantragFails() {
        createAenderungsanstrag()
            .then()
            .assertThat()
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @TestAsSachbearbeiter
    @Order(6)
    @Test
    void makeGesuchVerfuegt() {
        gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
        gesuchApiSpec.getInitialTrancheChanges()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        // Upload Unterschriftenblatt to "skip" Verfuegt state
        TestUtil.uploadUnterschriftenblatt(
            dokumentApiSpec,
            gesuch.getId(),
            UnterschriftenblattDokumentTypDtoSpec.GEMEINSAM,
            TestUtil.getTestPng()
        );

        gesuchApiSpec.changeGesuchStatusToVerfuegt()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
        gesuchWithChanges =
            gesuchApiSpec.getInitialTrancheChanges()
                .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .extract()
                .body()
                .as(GesuchWithChangesDtoSpec.class);
        assertThat(gesuchWithChanges.getChanges()).hasSize(1);
    }

    @Test
    @Order(8)
    @TestAsSachbearbeiter
    void changeToFinalState() {
        gesuchApiSpec.changeGesuchStatusToVersendet()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());

        var gesuchWithChanges = gesuchApiSpec.getGesuchSB()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);

        assertThat(gesuchWithChanges.getGesuchStatus()).satisfiesAnyOf(
            status -> assertThat(status).isEqualTo(GesuchstatusDtoSpec.STIPENDIENANSPRUCH),
            status -> assertThat(status).isEqualTo(GesuchstatusDtoSpec.KEIN_STIPENDIENANSPRUCH)
        );
    }

    @Test
    @TestAsGesuchsteller
    @Order(9)
    void createFirstAenderungsantrag() {
        createAenderungsanstrag()
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(10)
    @Description("Only one (open: NOT in State ABGELEHNT|AKZEPTIIERT) Aenderungsantrag should be allowed")
    void createSecondAenderungsantragFails() {
        createAenderungsanstrag()
            .then()
            .assertThat()
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    io.restassured.response.Response createAenderungsanstrag() {
        return gesuchTrancheApiSpec.createAenderungsantrag()
            .gesuchIdPath(gesuch.getId())
            .body(
                new CreateAenderungsantragRequestDtoSpec().comment("aenderung1")
                    .start(gesuch.getGesuchTrancheToWorkWith().getGueltigAb())
                    .end(gesuch.getGesuchTrancheToWorkWith().getGueltigBis())
            )
            .execute(TestUtil.PEEK_IF_ENV_SET);
    }

    @Test
    @TestAsGesuchsteller
    @Order(11)
    @Description("Test setup for: The another GS must not be able do delete a Aenderung'")
    void setupnextTest() {
        gesuchtranchen = gesuchTrancheApiSpec.getAllTranchenForGesuchGS()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchTrancheListDtoSpec.class);
        final var aenderung = gesuchtranchen.getTranchen()
            .stream()
            .filter(tranche -> tranche.getTyp() == GesuchTrancheTypDtoSpec.AENDERUNG)
            .findFirst()
            .get();

        gesuchTrancheApiSpec.aenderungEinreichen()
            .aenderungIdPath(aenderung.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(12)
    void createCustomDokumentTypOnAenderungShouldSucceed() {
        final var aenderung = gesuchtranchen.getTranchen()
            .stream()
            .filter(tranche -> tranche.getTyp() == GesuchTrancheTypDtoSpec.AENDERUNG)
            .findFirst()
            .get();

        CustomDokumentTypCreateDtoSpec customDokumentTypCreateDtoSpec = new CustomDokumentTypCreateDtoSpec();
        customDokumentTypCreateDtoSpec.setType("test");
        customDokumentTypCreateDtoSpec.setDescription("test description");
        customDokumentTypCreateDtoSpec.setTrancheId(aenderung.getId());
        final var createdGesuchDokumentWithCustomType = dokumentApiSpec.createCustomDokumentTyp()
            .body(customDokumentTypCreateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDokumentDto.class);
        // get newly created type
        // check if empty dokument has been created
        // dokumentstatus: ausstehend
        MatcherAssert.assertThat(createdGesuchDokumentWithCustomType.getCustomDokumentTyp().getType(), is("test"));
        MatcherAssert.assertThat(createdGesuchDokumentWithCustomType.getDokumente().isEmpty(), is(true));
        customDokumentId = createdGesuchDokumentWithCustomType.getCustomDokumentTyp().getId();

        final var result = dokumentApiSpec.getCustomGesuchDokumentForTypSB()
            .customDokumentTypIdPath(createdGesuchDokumentWithCustomType.getCustomDokumentTyp().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(NullableGesuchDokumentDto.class);
        MatcherAssert.assertThat(result.getValue().getCustomDokumentTyp(), notNullValue());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(13)
    void deleteCustomDokumentTypOnAenderungShouldSucceed() {
        dokumentApiSpec.deleteCustomDokumentTyp()
            .customDokumentTypIdPath(customDokumentId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @TestAsSuperUser
    @Order(99)
    @StepwiseExtension.AlwaysRun
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }
}
