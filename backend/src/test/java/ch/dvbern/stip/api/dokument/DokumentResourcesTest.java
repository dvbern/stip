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
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
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
import ch.dvbern.stip.generated.dto.DokumenteToUploadDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDto;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static ch.dvbern.stip.api.util.TestConstants.TEST_FILE_LOCATION;
import static ch.dvbern.stip.api.util.TestConstants.TEST_XML_FILE_LOCATION;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DokumentResourcesTest {
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec =
        GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());

    public GesuchApiSpec getGesuchApiSpec() {
        return gesuchApiSpec;
    }

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

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void test_create_dokument_with_wrong_mime_type() {
        File file = new File(TEST_XML_FILE_LOCATION);
        dokumentApiSpec.createDokument();
        given()
            .pathParam("gesuchTrancheId", gesuchTrancheId)
            .pathParam("dokumentTyp", DokumentTyp.PERSON_AUSWEIS)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA)
            .multiPart("fileUpload", file)
            .when()
            .post("/api/v1" + DokumentApiSpec.CreateDokumentOper.REQ_URI)
            .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void test_create_dokument_for_gesuch() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        File file = new File(TEST_FILE_LOCATION);
        dokumentApiSpec.createDokument();
        given()
            .pathParam("gesuchTrancheId", gesuchTrancheId)
            .pathParam("dokumentTyp", DokumentTyp.PERSON_AUSWEIS)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA)
            .multiPart("fileUpload", file)
            .when()
            .post("/api/v1" + DokumentApiSpec.CreateDokumentOper.REQ_URI)
            .then()
            .statusCode(Status.CREATED.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void test_list_and_read_dokument_for_gesuch() throws IOException {
        var dokumentDtoList = dokumentApiSpec.getGesuchDokumenteForTyp()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .dokumentTypPath(DokumentTyp.PERSON_AUSWEIS)
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
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .asString();

        dokumentApiSpec.getDokument()
            .tokenQuery(token)
            .dokumentArtPath(DokumentArtDtoSpec.GESUCH_DOKUMENT)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .body(equalTo(readFileData()));
    }

    @Test
    @TestAsGesuchsteller
    @Order(5)
    void test_delete_dokument() {
        dokumentApiSpec.deleteDokument()
            .dokumentIdPath(dokumentId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());
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
            .gesuchTrancheIdPath(gesuchTrancheId)
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
        File file = new File(TEST_FILE_LOCATION);
        TestUtil.uploadCustomDokumentFile(dokumentApiSpec, gesuchTrancheId, customDokumentId, file);
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
            .gesuchIdPath(gesuchId)
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
            .gesuchTrancheIdPath(gesuchTrancheId)
            .customDokumentTypIdPath(customDokumentId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(NullableGesuchDokumentDtoSpec.class)
            .getValue()
            .getDokumente();

        assertThat(dokumentDtoList.size(), is(1));

        dokumentId = dokumentDtoList.get(dokumentDtoList.size() - 1).getId();

        final var token = dokumentApiSpec.getDokumentDownloadToken()
            .dokumentIdPath(dokumentId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .asString();

        dokumentApiSpec.getDokument()
            .tokenQuery(token)
            .dokumentArtPath(DokumentArtDtoSpec.GESUCH_DOKUMENT)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .body(equalTo(readFileData()));
    }

    // testAsGS
    // delete file
    @Test
    @TestAsGesuchsteller
    @Order(12)
    void test_delete_required_custom_gesuchdokuments() {
        dokumentApiSpec.deleteDokument()
            .dokumentIdPath(dokumentId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    // gesuch is still in wrong status for the operation -> should fail
    @Test
    @TestAsSachbearbeiter
    @Order(13)
    void test_delete_required_custom_gesuchdokument_should_still_fail() {
        dokumentApiSpec.deleteCustomDokumentTyp()
            .gesuchIdPath(gesuchId)
            .customDokumentTypIdPath(customDokumentId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    // for the next tests, gesuch
    // should be in state IN_BEARBEITUNG_SB
    @Test
    @TestAsGesuchsteller
    @Order(14)
    void fillGesuch() {
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);
    }

    @Test
    @TestAsGesuchsteller
    @Order(15)
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
    @Order(16)
    void gesuchStatusChangeToInBearbeitungSB() {
        final var foundGesuch = gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDtoSpec.class);

        assertThat(foundGesuch.getGesuchStatus(), is(GesuchstatusDtoSpec.IN_BEARBEITUNG_SB));
    }

    // testAsSB
    // delete custom
    // b: no files -> ok
    @Test
    @TestAsSachbearbeiter
    @Order(17)
    void test_delete_required_custom_gesuchdokument_should_success() {
        dokumentApiSpec.deleteCustomDokumentTyp()
            .gesuchIdPath(gesuchId)
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
    @Order(18)
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
}
