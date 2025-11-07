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
import java.time.LocalDate;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSuperUser;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.DokumentArtDtoSpec;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
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
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DokumentResourcesTest {
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());

    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private UUID gesuchId;
    private GesuchDtoSpec gesuch;
    private UUID gesuchTrancheId;
    private UUID dokumentId;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void test_prepare_gesuch_for_dokument() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        gesuchId = gesuch.getId();
        gesuchTrancheId = gesuch.getGesuchTrancheToWorkWith().getId();

        // set pia to < 18 years old - to prevent validation error in test
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecPersonInAusbildung();
        gesuchUpdateDTO.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setGeburtsdatum(LocalDate.now().minusYears(19));

        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());

        gesuchApiSpec.updateGesuch()
            .gesuchIdPath(gesuchId)
            .body(gesuchUpdateDTO)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void test_create_dokument_with_wrong_mime_type() {
        File file = new File(TEST_XML_FILE_LOCATION);
        dokumentApiSpec.createDokumentGS();
        given()
            .pathParam("gesuchTrancheId", gesuchTrancheId)
            .pathParam("dokumentTyp", DokumentTyp.PERSON_AUSWEIS)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA)
            .multiPart("fileUpload", file)
            .when()
            .post("/api/v1" + DokumentApiSpec.CreateDokumentGSOper.REQ_URI)
            .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void test_create_dokument_for_gesuch() {
        File file = new File(TEST_FILE_LOCATION);
        dokumentApiSpec.createDokumentGS();
        given()
            .pathParam("gesuchTrancheId", gesuchTrancheId)
            .pathParam("dokumentTyp", DokumentTyp.PERSON_AUSWEIS)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA)
            .multiPart("fileUpload", file)
            .when()
            .post("/api/v1" + DokumentApiSpec.CreateDokumentGSOper.REQ_URI)
            .then()
            .statusCode(Status.CREATED.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void test_list_and_read_dokument_for_gesuch() throws IOException {
        var dokumentDtoList = dokumentApiSpec.getGesuchDokumentForTypSB()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .dokumentTypPath(DokumentTyp.PERSON_AUSWEIS)
            .execute(TestUtil.PEEK_IF_ENV_SET)
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
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(FileDownloadTokenDtoSpec.class)
            .getToken();

        final var actualFileContent = dokumentApiSpec.getDokument()
            .tokenQuery(token)
            .dokumentArtPath(DokumentArtDtoSpec.GESUCH_DOKUMENT)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .asString();

        assertThat(actualFileContent.length(), is(greaterThan(0)));
    }

    @Test
    @TestAsGesuchsteller
    @Order(5)
    void test_delete_dokument() {
        dokumentApiSpec.deleteDokumentGS()
            .dokumentIdPath(dokumentId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @TestAsSuperUser
    @Order(99)
    void test_delete_gesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuchId);
    }

    private String readFileData() throws IOException {
        return Files.readString(new File(TEST_FILE_LOCATION).toPath());
    }
}
