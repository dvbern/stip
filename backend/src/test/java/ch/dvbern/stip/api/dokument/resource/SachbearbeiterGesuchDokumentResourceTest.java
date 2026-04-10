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

package ch.dvbern.stip.api.dokument.resource;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsSuperUser;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.AuszahlungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.SachbearbeiterGesuchDokumentCreateDtoSpec;
import ch.dvbern.stip.generated.dto.SachbearbeiterGesuchDokumentDto;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SachbearbeiterGesuchDokumentResourceTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final AuszahlungApiSpec auszahlungApiSpec = AuszahlungApiSpec.auszahlung(RequestSpecUtil.quarkusSpec());

    private GesuchDtoSpec gesuch;
    private SachbearbeiterGesuchDokumentDto sachbearbeiterGesuchDokument;

    private List<SachbearbeiterGesuchDokumentDto> getAllSachbearbeiterGesuchDokumentsOfGesuch(final UUID gesuchId) {
        return Arrays.stream(
            dokumentApiSpec.getAllSachbearbeiterGesuchDokumentsOfGesuch()
                .gesuchIdPath(gesuchId)
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .assertThat()
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .body()
                .as(SachbearbeiterGesuchDokumentDto[].class)
        ).toList();
    }

    @Test
    @Order(1)
    @TestAsGesuchsteller
    void setupGs() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        TestUtil.fillGesuchWithAuszahlung(gesuchApiSpec, dokumentApiSpec, auszahlungApiSpec, gesuch);

        gesuchApiSpec.gesuchEinreichenGs()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(2)
    @TestAsSachbearbeiter
    void createSachbearbeiterGesuchDokumentFail() {
        final var sachbearbeiterGesuchDokumentCreateDto = new SachbearbeiterGesuchDokumentCreateDtoSpec();
        sachbearbeiterGesuchDokumentCreateDto.setDescription("");
        sachbearbeiterGesuchDokumentCreateDto.setType("");

        dokumentApiSpec.createSachbearbeiterGesuchDokument()
            .gesuchIdPath(gesuch.getId())
            .body(sachbearbeiterGesuchDokumentCreateDto)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(3)
    @TestAsSachbearbeiter
    void createSachbearbeiterGesuchDokument() {
        final var sachbearbeiterGesuchDokumentCreateDto = new SachbearbeiterGesuchDokumentCreateDtoSpec();
        sachbearbeiterGesuchDokumentCreateDto.setDescription("qwe");
        sachbearbeiterGesuchDokumentCreateDto.setType("123");

        sachbearbeiterGesuchDokument = dokumentApiSpec.createSachbearbeiterGesuchDokument()
            .gesuchIdPath(gesuch.getId())
            .body(sachbearbeiterGesuchDokumentCreateDto)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(SachbearbeiterGesuchDokumentDto.class);
    }

    @Test
    @Order(4)
    @TestAsSachbearbeiter
    void uploadSachbearbeiterGesuchDokumentDokumentTest() {
        dokumentApiSpec.uploadSachbearbeiterGesuchDokument()
            .sachbearbeiterGesuchDokumentIdPath(sachbearbeiterGesuchDokument.getId())
            .reqSpec(req -> {
                req.addMultiPart("fileUpload", TestUtil.getTestPng(), "image/png");
            })
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @Order(5)
    @TestAsSachbearbeiter
    void downloadSachbearbeiterGesuchDokumentDokumentTest() {
        final var sachbearbeiterGesuchDokumentList = getAllSachbearbeiterGesuchDokumentsOfGesuch(gesuch.getId());
        final var fileDownloadToken = dokumentApiSpec.getSachbearbeiterGesuchDokumentDokumentDownloadToken()
            .dokumentIdPath(sachbearbeiterGesuchDokumentList.getFirst().getDokumente().getFirst().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(FileDownloadTokenDto.class);

        final var actualFileContent = dokumentApiSpec.getSachbearbeiterGesuchDokumentDokument()
            .tokenQuery(fileDownloadToken.getToken())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .asString();

        assertThat(actualFileContent.length(), is(greaterThan(0)));
    }

    @Test
    @Order(6)
    @TestAsSachbearbeiter
    void deleteSachbearbeiterGesuchDokumentDokumentTest() {
        var sachbearbeiterGesuchDokumentList = getAllSachbearbeiterGesuchDokumentsOfGesuch(gesuch.getId());
        assertThat(sachbearbeiterGesuchDokumentList.size(), is(1));
        assertThat(sachbearbeiterGesuchDokumentList.getFirst().getDokumente().size(), is(1));

        dokumentApiSpec.deleteSachbearbeiterGesuchDokumentDokument()
            .dokumentIdPath(sachbearbeiterGesuchDokumentList.getFirst().getDokumente().getFirst().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());

        sachbearbeiterGesuchDokumentList = getAllSachbearbeiterGesuchDokumentsOfGesuch(gesuch.getId());
        assertThat(sachbearbeiterGesuchDokumentList.getFirst().getDokumente().size(), is(0));
    }

    @Test
    @Order(7)
    @TestAsSachbearbeiter
    void deleteSachbearbeiterGesuchDokumentTest() {
        dokumentApiSpec.deleteSachbearbeiterGesuchDokument()
            .sachbearbeiterGesuchDokumentIdPath(sachbearbeiterGesuchDokument.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());
        var sachbearbeiterGesuchDokumentList = getAllSachbearbeiterGesuchDokumentsOfGesuch(gesuch.getId());
        assertThat(sachbearbeiterGesuchDokumentList.size(), is(0));
    }

    @Test
    @TestAsSuperUser
    @Order(99)
    void test_delete_gesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }
}
