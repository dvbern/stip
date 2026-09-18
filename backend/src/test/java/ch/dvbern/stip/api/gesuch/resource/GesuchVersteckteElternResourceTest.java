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

import java.util.Arrays;
import java.util.List;

import ch.dvbern.stip.api.benutzer.util.TestAsFreigabestelle;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsSuperUser;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.generator.api.model.gesuch.SteuerdatenUpdateTabsDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.SteuererklaerungUpdateTabsDtoSpecModel;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.StepwiseExtension.AlwaysRun;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.AuszahlungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.ElternApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.SteuerdatenApiSpec;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.DokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.ElternTypDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchHeaderDto;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDto;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class GesuchVersteckteElternResourceTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final AuszahlungApiSpec auszahlungApiSpec = AuszahlungApiSpec.auszahlung(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final SteuerdatenApiSpec steuerdatenApiSpec = SteuerdatenApiSpec.steuerdaten(RequestSpecUtil.quarkusSpec());
    private final ElternApiSpec elternApiSpec = ElternApiSpec.eltern(RequestSpecUtil.quarkusSpec());

    private static GesuchDtoSpec gesuch;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void gesuchErstellenFillEinreichen() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        final var fullGesuch = GesuchTestSpecGenerator.gesuchUpdateDtoSpecFull();
        fullGesuch.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        final var famsit = fullGesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation();
        famsit.setElternVerheiratetZusammen(false);
        famsit.setElternteilUnbekanntVerstorben(false);
        famsit.setGerichtlicheAlimentenregelung(false);
        fullGesuch.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getElterns()
            .forEach(
                elternUpdateDtoSpec -> elternUpdateDtoSpec.setWiederverheiratet(false)
            );

        fullGesuch.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .setSteuererklaerung(
                SteuererklaerungUpdateTabsDtoSpecModel
                    .steuererklaerungDtoSpecs(SteuerdatenTypDtoSpec.MUTTER, SteuerdatenTypDtoSpec.VATER)
            );
        TestUtil.fillGesuch(
            gesuchApiSpec,
            fullGesuch,
            dokumentApiSpec,
            gesuch
        );
        TestUtil.fillAuszahlung(gesuch.getFallId(), auszahlungApiSpec, TestUtil.getAuszahlungUpdateDtoSpec());

        gesuchApiSpec.gesuchEinreichenGs()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(2)
    void setStatusInBearbeitungSb() {
        gesuchApiSpec.changeGesuchStatusToBereitFuerBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
        gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
        var allDokTypes = Arrays.stream(DokumentTypDtoSpec.values()).toList();
        allDokTypes.forEach(dokType -> {
            var dokToAccept = dokumentApiSpec.getGesuchDokumentForTypSB()
                .dokumentTypPath(dokType)
                .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .body()
                .as(NullableGesuchDokumentDto.class);

            dokumentApiSpec.gesuchDokumentAkzeptieren()
                .gesuchDokumentIdPath(dokToAccept.getValue().getId())
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .assertThat()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        }
        );
        final var steuerdatenUpdateDtos =
            SteuerdatenUpdateTabsDtoSpecModel
                .steuerdatenDtoSpecs(SteuerdatenTypDtoSpec.MUTTER, SteuerdatenTypDtoSpec.VATER);
        steuerdatenApiSpec.updateSteuerdaten()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .body(steuerdatenUpdateDtos)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(3)
    void elternVerstecken() {
        elternApiSpec.setVersteckteEltern()
            .body(List.of(ElternTypDtoSpec.VATER))
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(4)
    void bearbeitungAbschliessen() {
        gesuchApiSpec.bearbeitungAbschliessen()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @Order(5)
    @TestAsFreigabestelle
    void changeGesuchToVerfuegt() {
        gesuchApiSpec.changeGesuchStatusToVerfuegt()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @Order(6)
    @TestAsSachbearbeiter
    void changeGesuchStatusToAbgeschlossen() {
        gesuchApiSpec.changeGesuchStatusToVerfuegungDruckbereit()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());

        final var response = TestUtil.uploadUnterschriftenblatt(
            dokumentApiSpec,
            gesuch.getId(),
            UnterschriftenblattDokumentTypDtoSpec.GEMEINSAM,
            TestUtil.getTestPng()
        );

        response.assertThat().statusCode(Status.CREATED.getStatusCode());

        gesuchApiSpec.changeGesuchStatusToVersendet()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @Order(7)
    @TestAsGesuchsteller
    void getBerechnungOfVerfuegungVaterVersteckt() {
        final var header = gesuchApiSpec.getGesuchHeaderGs()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(
                GesuchHeaderDto.class
            );

        final var berechnung = gesuchApiSpec.getBerechnungForVerfuegungGs()
            .verfuegungIdPath(header.getVersions().get(0).getBerechnungId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(
                BerechnungsresultatDto.class
            );
        assertThat(berechnung.getTranchenBerechnungsresultate().get(0).getFamilienBudgetresultate().size(), is(1));

        berechnung.getTranchenBerechnungsresultate()
            .get(0)
            .getFamilienBudgetresultate()
            .forEach(
                familienBudgetresultatDto -> assertThat(
                    familienBudgetresultatDto.getSteuerdatenTyp(),
                    not(SteuerdatenTyp.VATER)
                )
            );
    }

    @Test
    @Order(99)
    @TestAsSuperUser
    @AlwaysRun
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }
}
