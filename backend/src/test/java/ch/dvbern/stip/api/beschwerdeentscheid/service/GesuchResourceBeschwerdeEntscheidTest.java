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

package ch.dvbern.stip.api.beschwerdeentscheid.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchTrancheApiSpec;
import ch.dvbern.stip.generated.dto.BeschwerdeEntscheidDtoSpec;
import ch.dvbern.stip.generated.dto.BeschwerdeVerlaufEntryDtoSpec;
import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDtoSpec;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.UnterschriftenblattDokumentTypDtoSpec;
import com.mchange.io.FileUtils;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import wiremock.org.eclipse.jetty.http.HttpStatus;

import static ch.dvbern.stip.api.dokument.type.DokumentArt.BESCHWERDE_ENTSCHEID;
import static ch.dvbern.stip.api.util.TestConstants.TEST_PNG_FILE_LOCATION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class GesuchResourceBeschwerdeEntscheidTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec =
        GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    // create a gesuch
    private GesuchDtoSpec gesuch;
    private UUID dokumentId;
    private GesuchWithChangesDtoSpec gesuchWithChanges;

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
    void gesuchEinreichen() {
        gesuchApiSpec.gesuchEinreichen()
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
        gesuchApiSpec.getInitialTrancheChangesByGesuchId()
            .gesuchIdPath(gesuch.getId())
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
        // TODO KSTIP-1631: Make Gesuch the correct state
        gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
        gesuchApiSpec.getInitialTrancheChangesByGesuchId()
            .gesuchIdPath(gesuch.getId())
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
        gesuchWithChanges = gesuchApiSpec.getInitialTrancheChangesByGesuchId()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
        Assertions.assertThat(gesuchWithChanges.getChanges()).hasSize(1);
    }

    @Test
    @Order(7)
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

        Assertions.assertThat(gesuchWithChanges.getGesuchStatus())
            .satisfiesAnyOf(
                status -> Assertions.assertThat(status).isEqualTo(GesuchstatusDtoSpec.STIPENDIENANSPRUCH),
                status -> Assertions.assertThat(status).isEqualTo(GesuchstatusDtoSpec.KEIN_STIPENDIENANSPRUCH)
            );
    }

    @Test
    @TestAsSachbearbeiter
    @Order(8)
    void createBeschwerdeEntscheidWithFalseFlag() {
        final var file = TestUtil.getTestPng();
        TestUtil.uploadBeschwerdeEntscheid(gesuchApiSpec, gesuch.getId(), false, "test", file)
            .assertThat()
            .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(9)
    void verifyGesuchShouldBeInSameStatus() {
        var gesuchWithChanges = gesuchApiSpec.getGesuchSB()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);

        Assertions.assertThat(gesuchWithChanges.getGesuchStatus())
            .satisfiesAnyOf(
                status -> Assertions.assertThat(status).isEqualTo(GesuchstatusDtoSpec.STIPENDIENANSPRUCH),
                status -> Assertions.assertThat(status).isEqualTo(GesuchstatusDtoSpec.KEIN_STIPENDIENANSPRUCH)
            );
    }

    @Test
    @TestAsSachbearbeiter
    @Order(10)
    void verifyBeschwerdeVerlaufEntryCreated() {
        final var beschwerdeVerlaufEntries = gesuchApiSpec.getAllBeschwerdeVerlaufEntrys()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK_200)
            .extract()
            .body()
            .as(BeschwerdeVerlaufEntryDtoSpec[].class);
        assertThat(beschwerdeVerlaufEntries.length, is(1));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(11)
    void createBeschwerdeEntscheidWithTrueFlag() {
        final var file = TestUtil.getTestPng();
        TestUtil.uploadBeschwerdeEntscheid(gesuchApiSpec, gesuch.getId(), true, "test2", file)
            .assertThat()
            .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(12)
    void verifyGesuchShouldBeInCorrectStatus() {
        var gesuchWithChanges = gesuchApiSpec.getGesuchSB()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);

        Assertions.assertThat(gesuchWithChanges.getGesuchStatus())
            .satisfiesAnyOf(
                status -> Assertions.assertThat(status).isEqualTo(GesuchstatusDtoSpec.BEREIT_FUER_BEARBEITUNG)
            );
    }

    @Test
    @TestAsSachbearbeiter
    @Order(13)
    void verifyBeschwerdeVerlaufEntryCreated2() {
        final var beschwerdeVerlaufEntries = gesuchApiSpec.getAllBeschwerdeVerlaufEntrys()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK_200)
            .extract()
            .body()
            .as(BeschwerdeVerlaufEntryDtoSpec[].class);
        assertThat(beschwerdeVerlaufEntries.length, is(2));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(14)
    void getAllBeschwerdeEntscheide() {
        final var entrys = gesuchApiSpec.getAllBeschwerdeentscheideForGesuch()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(BeschwerdeEntscheidDtoSpec[].class);
        assertThat(entrys.length, is(2));

        final var entry = entrys[0];
        assertThat(entry.getKommentar(), is("test"));
        assertThat(entry.getIsBeschwerdeErfolgreich(), is(true));
        assertThat(entry.getDokumente().size(), is(1));
        dokumentId = entry.getDokumente().get(0).getId();
    }

    @Test
    @TestAsSachbearbeiter
    @Order(15)
    void getBeschwerdeEntscheidDokumentTest() throws IOException {
        final var token = dokumentApiSpec.getDokumentDownloadToken()
            .dokumentIdPath(dokumentId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(FileDownloadTokenDtoSpec.class)
            .getToken();

        dokumentApiSpec.getDokument()
            .tokenQuery(token)
            .dokumentArtPath(BESCHWERDE_ENTSCHEID)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .body(equalTo(readPngFileData()));
    }

    @Test
    @TestAsAdmin
    @Order(99)
    void test_delete_gesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }

    private String readPngFileData() throws IOException {
        return FileUtils.getContentsAsString(new File(TEST_PNG_FILE_LOCATION));
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
}
