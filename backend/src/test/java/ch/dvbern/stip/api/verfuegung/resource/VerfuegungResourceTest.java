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

package ch.dvbern.stip.api.verfuegung.resource;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsFreigabestelleAndSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsSuperUser;
import ch.dvbern.stip.api.generator.api.model.gesuch.SteuerdatenUpdateTabsDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.StepwiseExtension.AlwaysRun;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.AuszahlungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchTrancheApiSpec;
import ch.dvbern.stip.generated.api.SteuerdatenApiSpec;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDtoSpec;
import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDtoSpec;
import ch.dvbern.stip.generated.dto.DokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchHeaderDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDto;
import ch.dvbern.stip.generated.dto.SteuerdatenTypDtoSpec;
import ch.dvbern.stip.generated.dto.UnterschriftenblattDokumentTypDtoSpec;
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
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class VerfuegungResourceTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec =
        GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final SteuerdatenApiSpec steuerdatenApiSpec = SteuerdatenApiSpec.steuerdaten(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final AuszahlungApiSpec auszahlungApiSpec = AuszahlungApiSpec.auszahlung(RequestSpecUtil.quarkusSpec());

    private GesuchDtoSpec gesuch;
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

    @TestAsFreigabestelleAndSachbearbeiter
    @Order(5)
    @Test
    void makeGesuchVerfuegt() {
        gesuchApiSpec.changeGesuchStatusToBereitFuerBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
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
            .statusCode(Status.FORBIDDEN.getStatusCode());

        final var steuerdatenUpdateDto =
            SteuerdatenUpdateTabsDtoSpecModel.steuerdatenDtoSpec(SteuerdatenTypDtoSpec.FAMILIE);
        steuerdatenApiSpec.updateSteuerdaten()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .body(List.of(steuerdatenUpdateDto))
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());

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
        gesuchApiSpec.changeGesuchStatusToVersendet()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @TestAsSachbearbeiter
    @Order(6)
    @Test
    void getVerfuegungs() {
        final GesuchHeaderDtoSpec header = gesuchApiSpec.getGesuchHeaderSb()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchHeaderDtoSpec.class);

        assertThat(header.getVersions().size(), is(1));

        final var berechnung = gesuchApiSpec.getBerechnungForVerfuegung()
            .verfuegungIdPath(header.getVersions().getFirst().getBerechnungId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(BerechnungsresultatDtoSpec.class);

        assertThat(berechnung.getBerechnungStipendium(), greaterThan(500));
    }

    @Test
    @TestAsGesuchsteller
    @Order(7)
    void createAenderungs() {
        aenderungId = gesuchTrancheApiSpec.createAenderungsantrag()
            .gesuchIdPath(gesuch.getId())
            .body(
                new CreateAenderungsantragRequestDtoSpec().comment("aenderung1")
                    .start(gesuch.getGesuchTrancheToWorkWith().getGueltigAb())
                    .end(gesuch.getGesuchTrancheToWorkWith().getGueltigBis())
            )
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchTrancheDtoSpec.class)
            .getId();

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
    @TestAsFreigabestelleAndSachbearbeiter
    @Order(8)
    void aenderungAkzeptieren() {
        gesuchTrancheApiSpec.aenderungAkzeptieren()
            .aenderungIdPath(aenderungId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());

        final var gesuchHeader = TestUtil.executeAndExtract(
            GesuchHeaderDtoSpec.class,
            gesuchApiSpec.getGesuchHeaderSb().gesuchIdPath(gesuch.getId())
        );
        gesuchApiSpec.changeGesuchStatusToVerfuegt()
            .gesuchTrancheIdPath(gesuchHeader.getCurrentTranches().get(0).getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
        gesuchApiSpec.changeGesuchStatusToVersendet()
            .gesuchTrancheIdPath(gesuchHeader.getCurrentTranches().get(0).getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());

        final var gesuchWithChanges = gesuchApiSpec.getGesuchSB()
            .gesuchTrancheIdPath(gesuchHeader.getCurrentTranches().get(0).getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);

        Assertions.assertThat(gesuchWithChanges.getGesuchStatus())
            .isIn(List.of(GesuchstatusDtoSpec.STIPENDIENANSPRUCH, GesuchstatusDtoSpec.KEIN_STIPENDIENANSPRUCH));
    }

    @TestAsSachbearbeiter
    @Order(9)
    @Test
    void getVerfuegungsAgain() {
        final GesuchHeaderDtoSpec header = gesuchApiSpec.getGesuchHeaderSb()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchHeaderDtoSpec.class);

        assertThat(header.getVersions().size(), is(2));

        final var berechnung = gesuchApiSpec.getBerechnungForVerfuegung()
            .verfuegungIdPath(header.getVersions().getFirst().getBerechnungId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(BerechnungsresultatDtoSpec.class);

        assertThat(berechnung.getBerechnungStipendium(), greaterThan(500));
    }

    @Test
    @TestAsSuperUser
    @Order(99)
    @AlwaysRun
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }
}
