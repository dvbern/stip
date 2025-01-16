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

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDtoSpec;
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
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DokumentResourcesTest {
    public final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    public final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private UUID gesuchId;
    private UUID gesuchTrancheId;
    private UUID dokumentId;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void test_prepare_gesuch_for_dokument() {
        final var gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
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
            .dokumentTypPath(DokumentTyp.PERSON_AUSWEIS)
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(FileDownloadTokenDtoSpec.class)
            .getToken();

        dokumentApiSpec.getDokument()
            .tokenQuery(token)
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
            .gesuchTrancheIdPath(gesuchTrancheId)
            .dokumentIdPath(dokumentId)
            .dokumentTypPath(DokumentTyp.PERSON_AUSWEIS)
            .execute(ResponseBody::prettyPeek)
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(6)
    void test_delete_gesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuchId);
    }

    private String readFileData() throws IOException {
        return Files.readString(new File(TEST_FILE_LOCATION).toPath());
    }
}
