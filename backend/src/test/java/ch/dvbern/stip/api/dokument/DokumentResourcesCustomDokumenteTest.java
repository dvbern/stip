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

package ch.dvbern.stip.api.dokument;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchTrancheApiSpec;
import ch.dvbern.stip.generated.dto.CustomDokumentTypCreateDtoSpec;
import ch.dvbern.stip.generated.dto.CustomDokumentTypDto;
import ch.dvbern.stip.generated.dto.DokumentArtDtoSpec;
import ch.dvbern.stip.generated.dto.DokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.DokumenteToUploadDto;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDto;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDtoSpec;
import com.mchange.io.FileUtils;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static ch.dvbern.stip.api.util.TestConstants.TEST_FILE_LOCATION;
import static ch.dvbern.stip.api.util.TestConstants.TEST_PNG_FILE_LOCATION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DokumentResourcesCustomDokumenteTest {
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec =
        GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());

    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private UUID gesuchId;
    private GesuchDtoSpec gesuch;
    private UUID gesuchTrancheId;
    private UUID dokumentId;
    private UUID customDokumentId;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void test_prepare_gesuch_for_dokument() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        gesuchId = gesuch.getId();
        gesuchTrancheId = gesuch.getGesuchTrancheToWorkWith().getId();
    }

    // for the next tests, gesuch
    // should be in state IN_BEARBEITUNG_SB
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
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(4)
    void test_setup_gesuch_to_bearbeitung_sb() {
        gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
    }

    @Test
    @TestAsSachbearbeiter
    @Order(6)
    void test_create_custom_gesuchdokument() {
        CustomDokumentTypCreateDtoSpec customDokumentTypCreateDtoSpec = new CustomDokumentTypCreateDtoSpec();
        customDokumentTypCreateDtoSpec.setType("test");
        customDokumentTypCreateDtoSpec.setDescription("test description");
        customDokumentTypCreateDtoSpec.setTrancheId(gesuchTrancheId);
        final var createdGesuchDokumentWithCustomType = dokumentApiSpec.createCustomDokumentTyp()
            .body(customDokumentTypCreateDtoSpec)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDokumentDto.class);
        // get newly created type
        // check if empty dokument has been created
        // dokumentstatus: ausstehend
        assertThat(createdGesuchDokumentWithCustomType.getCustomDokumentTyp().getType(), is("test"));
        assertThat(createdGesuchDokumentWithCustomType.getDokumente().isEmpty(), is(true));
        customDokumentId = createdGesuchDokumentWithCustomType.getCustomDokumentTyp().getId();

        final var result = dokumentApiSpec.getCustomGesuchDokumenteForTyp()
            .customDokumentTypIdPath(createdGesuchDokumentWithCustomType.getCustomDokumentTyp().getId())
            .execute(ResponseBody::prettyPeek)
            .then()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(NullableGesuchDokumentDto.class);
        assertThat(result.getValue().getCustomDokumentTyp(), notNullValue());
    }

    // testAsGS
    // new document should appear in required documents
    @Test
    @TestAsGesuchsteller
    @Order(7)
    void test_get_required_custom_gesuchdokuments() {
        final var requiredDocuments = gesuchTrancheApiSpec.getDocumentsToUpload()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(DokumenteToUploadDto.class);
        final var result = requiredDocuments.getCustomDokumentTyps();
        assertThat(result.size(), is(greaterThan(0)));
    }

    // testAsGS
    // upload newly created required document
    @Test
    @TestAsGesuchsteller
    @Order(8)
    void test_upload_custom_gesuchdokuments() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        File file = new File(TEST_PNG_FILE_LOCATION);
        TestUtil.uploadCustomDokumentFile(dokumentApiSpec, customDokumentId, file);
    }

    // testAsGS
    // uploaded document should NOT appear in required documents
    @Test
    @TestAsGesuchsteller
    @Order(9)
    void test_get_required_custom_gesuchdokuments_should_not_appear() {
        final var requiredDocuments = gesuchTrancheApiSpec.getDocumentsToUpload()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(DokumenteToUploadDto.class);
        final var result = requiredDocuments.getCustomDokumentTyps();
        assertThat(result.size(), is(0));
    }

    // testAsSB
    // delete should fail
    @Test
    @TestAsSachbearbeiter
    @Order(10)
    void test_delete_required_custom_gesuchdokument_typ_should_fail() {
        dokumentApiSpec.deleteCustomDokumentTyp()
            .customDokumentTypIdPath(customDokumentId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.FORBIDDEN.getStatusCode());

        final var customDocumentTypes = dokumentApiSpec.getAllCustomDokumentTypes()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(CustomDokumentTypDto[].class);

        assertThat(customDocumentTypes.length, is(greaterThan(0)));
    }

    @Test
    @TestAsGesuchsteller
    @Order(11)
    void test_read_custom_gesuchdokument() throws IOException {
        var dokumentDtoList = dokumentApiSpec.getCustomGesuchDokumenteForTyp()
            .customDokumentTypIdPath(customDokumentId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(NullableGesuchDokumentDtoSpec.class)
            .getValue()
            .getDokumente();

        assertThat(dokumentDtoList.size(), is(1));

        dokumentId = dokumentDtoList.get(0).getId();

        final var token = dokumentApiSpec.getDokumentDownloadToken()
            .dokumentIdPath(dokumentId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(FileDownloadTokenDtoSpec.class)
            .getToken();

        final var actualFileContent = dokumentApiSpec.getDokument()
            .tokenQuery(token)
            .dokumentArtPath(DokumentArtDtoSpec.GESUCH_DOKUMENT)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .asString();

        final var expectedFileContent = readPngFileData();
        assertThat(expectedFileContent, is(actualFileContent));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(12)
    void dokument_ablehnen_or_akzeptieren() {
        var allDokTypesExceptOne = Arrays.stream(DokumentTypDtoSpec.values()).toList();
        var modifiableDokTypeList = new ArrayList<>(allDokTypesExceptOne);
        modifiableDokTypeList.forEach(dokType -> {
            var dokToAccept = dokumentApiSpec.getGesuchDokumenteForTyp()
                .dokumentTypPath(dokType)
                .gesuchTrancheIdPath(gesuchTrancheId)
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .assertThat()
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .body()
                .as(NullableGesuchDokumentDto.class);

            dokumentApiSpec.gesuchDokumentAkzeptieren()
                .gesuchDokumentIdPath(dokToAccept.getValue().getId())
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .assertThat()
                .statusCode(Status.NO_CONTENT.getStatusCode());
        });
    }

    @TestAsSachbearbeiter
    @Test
    @Order(13)
    void fehlendeDokumenteUebermitteln() {
        gesuchApiSpec.gesuchFehlendeDokumenteUebermitteln()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    // testAsGS
    // delete file
    @Test
    @TestAsGesuchsteller
    @Order(14)
    void test_delete_required_custom_gesuchdokuments() {
        dokumentApiSpec.deleteDokument()
            .dokumentIdPath(dokumentId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @TestAsAdmin
    @Order(15)
    void delete_gesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuchId);
    }

    // for the next tests, gesuch
    // should be in state IN_BEARBEITUNG_SB

    @Test
    @TestAsGesuchsteller
    @Order(16)
    void reset() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        gesuchId = gesuch.getId();
        gesuchTrancheId = gesuch.getGesuchTrancheToWorkWith().getId();
    }

    @Test
    @TestAsGesuchsteller
    @Order(17)
    void fillGesuch_2() {
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);
    }

    @Test
    @TestAsGesuchsteller
    @Order(18)
    void gesuchEinreichen_2() {
        gesuchApiSpec.gesuchEinreichen()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(19)
    void gesuchStatusChangeToInBearbeitungSB() {
        final var foundGesuch = gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);

        assertThat(foundGesuch.getGesuchStatus(), is(GesuchstatusDtoSpec.IN_BEARBEITUNG_SB));
    }

    // testAsSB
    // delete custom
    // b: no files -> ok
    @Test
    @TestAsSachbearbeiter
    @Order(20)
    void test_delete_required_custom_gesuchdokument_should_success() {
        // first, prepare a new custom gesuch dokument type
        test_create_custom_gesuchdokument();

        dokumentApiSpec.deleteCustomDokumentTyp()
            .customDokumentTypIdPath(customDokumentId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());

        final var customDocumentTypes = dokumentApiSpec.getAllCustomDokumentTypes()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(CustomDokumentTypDto[].class);
        assertThat(customDocumentTypes.length, is(0));
    }

    // testAsGS
    // new document should appear in required documents
    // upload
    @Test
    @TestAsGesuchsteller
    @Order(21)
    void test_get_required_custom_gesuchdokuments_should_be_empty() {
        final var requiredDocuments = gesuchTrancheApiSpec.getDocumentsToUpload()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(DokumenteToUploadDto.class);
        assertThat(requiredDocuments.getCustomDokumentTyps().size(), is(0));
    }

    @Test
    @TestAsAdmin
    @Order(99)
    void test_delete_gesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuchId);
    }

    private String readFileData() throws IOException {
        return Files.readString(new File(TEST_FILE_LOCATION).toPath());
    }

    private String readPngFileData() throws IOException {
        return FileUtils.getContentsAsString(new File(TEST_PNG_FILE_LOCATION));
    }
}
