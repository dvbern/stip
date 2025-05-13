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
import ch.dvbern.stip.generated.dto.BeschwerdeVerlaufEntryDtoSpec;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.UnterschriftenblattDokumentTypDtoSpec;
import com.mchange.io.FileUtils;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import jdk.jfr.Description;
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
    void setupCreateGesuch() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void setupFillGesuch() {
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void setupGesuchEinreichen() {
        gesuchApiSpec.gesuchEinreichen()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @TestAsSachbearbeiter
    @Order(4)
    @Test
    void setupGesuchVerfuegen() {
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
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());

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
        gesuchWithChanges = gesuchApiSpec.getInitialTrancheChanges()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
        Assertions.assertThat(gesuchWithChanges.getChanges()).hasSize(1);
    }

    @Test
    @Order(5)
    @TestAsSachbearbeiter
    void setupGesuchVersenden() {
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
    @Description("SB should be able to create a BeschwerdeEntscheid with the isBeschwerdeErfolgreich-flag set to false")
    @Order(6)
    void createBeschwerdeEntscheidWithFalseFlagShouldWorkTest() {
        final var file = TestUtil.getTestPng();
        TestUtil.uploadBeschwerdeEntscheid(gesuchApiSpec, gesuch.getId(), false, "test", file)
            .assertThat()
            .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Description(
        "Verify the Gesuchstatus has not changed, when a BeschwerdeEntscheid with the isBeschwerdeErfolgreich-flag set to false had been created"
    )
    @Order(7)
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
    @Description("Verify that a BeschwerdeVerlaufEntry has been created")
    @Order(8)
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
        final var entry = beschwerdeVerlaufEntries[0];
        assertThat(entry.getBeschwerdeEntscheid().getBeschwerdeErfolgreich(), is(false));
        assertThat(entry.getBeschwerdeEntscheid().getDokumente().size(), is(1));
    }

    @Test
    @TestAsSachbearbeiter
    @Description("SB should be able to create a BeschwerdeEntscheid with the isBeschwerdeErfolgreich-flag set to true")
    @Order(9)
    void createBeschwerdeEntscheidWithTrueFlagShouldWorkTest() {
        final var file = TestUtil.getTestPng();
        TestUtil.uploadBeschwerdeEntscheid(gesuchApiSpec, gesuch.getId(), true, "test2", file)
            .assertThat()
            .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Description(
        "Verify the Gesuchstatus has been set to BEREIT_FUER_BEARBEITUNG, when a BeschwerdeEntscheid with the isBeschwerdeErfolgreich-flag set to true had been created"
    )
    @Order(10)
    void verifyGesuchShouldBeInStatus_BEREIT_FUER_BEARBEITUNG() {
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
    @Description("Verify that another BeschwerdeVerlaufEntry has been created")
    @Order(11)
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
        dokumentId = beschwerdeVerlaufEntries[0].getBeschwerdeEntscheid().getDokumente().get(0).getId();
    }

    @Test
    @TestAsSachbearbeiter
    @Description("Verify that the documents of a BeschwerdeEntscheid is available")
    @Order(12)
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
}
