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

package ch.dvbern.stip.api.ausbildung.resource;

import java.util.List;

import ch.dvbern.stip.api.benutzer.util.TestAsFreigabestelleAndSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
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
import ch.dvbern.stip.generated.dto.AusbildungUnterbruchAntragGSDtoSpec;
import ch.dvbern.stip.generated.dto.AusbildungUnterbruchAntragSBDtoSpec;
import ch.dvbern.stip.generated.dto.AusbildungUnterbruchAntragStatusDtoSpec;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDtoSpec;
import ch.dvbern.stip.generated.dto.FallDashboardItemDto;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import ch.dvbern.stip.generated.dto.SteuerdatenTypDtoSpec;
import ch.dvbern.stip.generated.dto.UnterschriftenblattDokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.UpdateAusbildungUnterbruchAntragGSDtoSpec;
import ch.dvbern.stip.generated.dto.UpdateAusbildungUnterbruchAntragSBDtoSpec;
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

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class AusbildungUnterbruchAntragResourceTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec =
        GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final AuszahlungApiSpec auszahlungApiSpec = AuszahlungApiSpec.auszahlung(RequestSpecUtil.quarkusSpec());
    private final SteuerdatenApiSpec steuerdatenApiSpec = SteuerdatenApiSpec.steuerdaten(RequestSpecUtil.quarkusSpec());

    private GesuchDtoSpec gesuch;
    private AusbildungUnterbruchAntragGSDtoSpec ausbildungUnterbruchAntragGs;

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
    void unterbruchAntragErstellen() {
        ausbildungUnterbruchAntragGs = ausbildungApiSpec.createAusbildungUnterbruchAntrag()
            .ausbildungIdPath(gesuch.getAusbildungId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(AusbildungUnterbruchAntragGSDtoSpec.class);

        final var fallDashboardItem = gesuchApiSpec.getGsDashboard()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(FallDashboardItemDto.class);

        final var ausbildungDashboardItems = fallDashboardItem.getAusbildungDashboardItems();
        final var ausbildungDashboardItem = ausbildungDashboardItems.get(0);

        assertThat(
            ausbildungDashboardItem.getOpenAusbildungUnterbruchAntragId(),
            is(ausbildungUnterbruchAntragGs.getId())
        );
        assertThat(ausbildungDashboardItem.getCanCreateAusbildungUnterbruchAntrag(), is(false));
    }

    @Test
    @TestAsGesuchsteller
    @Order(5)
    void unterbruchAntragEinreichen() {
        ausbildungApiSpec.createAusbildungUnterbruchAntragDokument()
            .ausbildungUnterbruchAntragIdPath(ausbildungUnterbruchAntragGs.getId())
            .reqSpec(req -> req.addMultiPart("fileUpload", TestUtil.getTestPng(), "image/png"))
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.CREATED.getStatusCode());

        final var updateAusbildungUnterbruchAntragGSDtoSpec = new UpdateAusbildungUnterbruchAntragGSDtoSpec();
        updateAusbildungUnterbruchAntragGSDtoSpec
            .setStartDate(gesuch.getGesuchTrancheToWorkWith().getGueltigAb().plusMonths(1));
        updateAusbildungUnterbruchAntragGSDtoSpec
            .setEndDate(gesuch.getGesuchTrancheToWorkWith().getGueltigBis().minusMonths(1));
        updateAusbildungUnterbruchAntragGSDtoSpec.setKommentarGS("asd");
        ausbildungUnterbruchAntragGs = ausbildungApiSpec.einreichenAusbildungUnterbruchAntrag()
            .ausbildungUnterbruchAntragIdPath(ausbildungUnterbruchAntragGs.getId())
            .body(updateAusbildungUnterbruchAntragGSDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(AusbildungUnterbruchAntragGSDtoSpec.class);
    }

    @TestAsFreigabestelleAndSachbearbeiter
    @Order(6)
    @Test
    void makeGesuchInBearbeitung() {
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

        TestUtil.uploadUnterschriftenblatt(
            dokumentApiSpec,
            gesuch.getId(),
            UnterschriftenblattDokumentTypDtoSpec.GEMEINSAM,
            TestUtil.getTestPng()
        ).assertThat().statusCode(Response.Status.CREATED.getStatusCode());
        final var steuerdatenUpdateDto =
            SteuerdatenUpdateTabsDtoSpecModel.steuerdatenDtoSpec(SteuerdatenTypDtoSpec.FAMILIE);
        steuerdatenApiSpec.updateSteuerdaten()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .body(List.of(steuerdatenUpdateDto))
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @TestAsFreigabestelleAndSachbearbeiter
    @Order(7)
    @Test
    void antragAblehnen() {
        final var updateAusbildungUnterbruchAntragSBDtoSpec = new UpdateAusbildungUnterbruchAntragSBDtoSpec();
        updateAusbildungUnterbruchAntragSBDtoSpec
            .setStartDate(gesuch.getGesuchTrancheToWorkWith().getGueltigAb().plusMonths(1));
        updateAusbildungUnterbruchAntragSBDtoSpec
            .setEndDate(gesuch.getGesuchTrancheToWorkWith().getGueltigBis().minusMonths(1));
        updateAusbildungUnterbruchAntragSBDtoSpec.setKommentarSB("asd");
        updateAusbildungUnterbruchAntragSBDtoSpec.setMonateOhneAnspruch(0);
        updateAusbildungUnterbruchAntragSBDtoSpec.setStatus(AusbildungUnterbruchAntragStatusDtoSpec.ABGELEHNT);

        ausbildungApiSpec.updateAusbildungUnterbruchAntragSB()
            .ausbildungUnterbruchAntragIdPath(ausbildungUnterbruchAntragGs.getId())
            .body(updateAusbildungUnterbruchAntragSBDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(AusbildungUnterbruchAntragSBDtoSpec.class);
    }

    @Test
    @TestAsGesuchsteller
    @Order(8)
    void unterbruchAntragErstellenAgain() {
        ausbildungUnterbruchAntragGs = ausbildungApiSpec.createAusbildungUnterbruchAntrag()
            .ausbildungIdPath(gesuch.getAusbildungId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(AusbildungUnterbruchAntragGSDtoSpec.class);

        final var fallDashboardItem = gesuchApiSpec.getGsDashboard()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(FallDashboardItemDto.class);

        final var ausbildungDashboardItems = fallDashboardItem.getAusbildungDashboardItems();
        final var ausbildungDashboardItem = ausbildungDashboardItems.get(0);

        assertThat(
            ausbildungDashboardItem.getOpenAusbildungUnterbruchAntragId(),
            is(ausbildungUnterbruchAntragGs.getId())
        );
        assertThat(ausbildungDashboardItem.getCanCreateAusbildungUnterbruchAntrag(), is(false));

        ausbildungApiSpec.createAusbildungUnterbruchAntragDokument()
            .ausbildungUnterbruchAntragIdPath(ausbildungUnterbruchAntragGs.getId())
            .reqSpec(req -> req.addMultiPart("fileUpload", TestUtil.getTestPng(), "image/png"))
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.CREATED.getStatusCode());

        final var updateAusbildungUnterbruchAntragGSDtoSpec = new UpdateAusbildungUnterbruchAntragGSDtoSpec();
        updateAusbildungUnterbruchAntragGSDtoSpec
            .setStartDate(gesuch.getGesuchTrancheToWorkWith().getGueltigAb().plusMonths(1));
        updateAusbildungUnterbruchAntragGSDtoSpec
            .setEndDate(gesuch.getGesuchTrancheToWorkWith().getGueltigBis().minusMonths(1));
        updateAusbildungUnterbruchAntragGSDtoSpec.setKommentarGS("asd");
        ausbildungUnterbruchAntragGs = ausbildungApiSpec.einreichenAusbildungUnterbruchAntrag()
            .ausbildungUnterbruchAntragIdPath(ausbildungUnterbruchAntragGs.getId())
            .body(updateAusbildungUnterbruchAntragGSDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(AusbildungUnterbruchAntragGSDtoSpec.class);
    }

    @TestAsFreigabestelleAndSachbearbeiter
    @Order(9)
    @Test
    void antragAkzeptieren() {
        final var updateAusbildungUnterbruchAntragSBDtoSpec = new UpdateAusbildungUnterbruchAntragSBDtoSpec();
        updateAusbildungUnterbruchAntragSBDtoSpec
            .setStartDate(gesuch.getGesuchTrancheToWorkWith().getGueltigAb().plusMonths(1));
        updateAusbildungUnterbruchAntragSBDtoSpec
            .setEndDate(gesuch.getGesuchTrancheToWorkWith().getGueltigBis().minusMonths(1));
        updateAusbildungUnterbruchAntragSBDtoSpec.setKommentarSB("asd");
        updateAusbildungUnterbruchAntragSBDtoSpec.setMonateOhneAnspruch(3);
        updateAusbildungUnterbruchAntragSBDtoSpec.setStatus(AusbildungUnterbruchAntragStatusDtoSpec.AKZEPTIERT);

        ausbildungApiSpec.updateAusbildungUnterbruchAntragSB()
            .ausbildungUnterbruchAntragIdPath(ausbildungUnterbruchAntragGs.getId())
            .body(updateAusbildungUnterbruchAntragSBDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(AusbildungUnterbruchAntragSBDtoSpec.class);
    }

    @TestAsFreigabestelleAndSachbearbeiter
    @Order(10)
    @Test
    void berechnungReturnsAntragValues() {
        final var berechnung = gesuchApiSpec.getBerechnungForGesuch()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(BerechnungsresultatDtoSpec.class);

        assertThat(berechnung.getMonateOhneAnspruch(), is(3));
    }

    @Test
    @TestAsSuperUser
    @Order(99)
    @AlwaysRun
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }
}
