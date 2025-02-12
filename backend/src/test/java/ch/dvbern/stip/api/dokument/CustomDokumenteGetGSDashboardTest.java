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
import ch.dvbern.stip.generated.dto.DokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.FallDashboardItemDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDto;
import com.mchange.io.FileUtils;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
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
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomDokumenteGetGSDashboardTest {
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
    private GesuchDokumentDto createdGesuchDokumentWithCustomType;

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
    void gesuchStatusChangeToInBearbeitungSB() {
        gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDtoSpec.class);
    }

    @Test
    @TestAsSachbearbeiter
    @Order(5)
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

    @Test
    @TestAsSachbearbeiter
    @Order(6)
    void create_custom_gesuchdokument() {
        CustomDokumentTypCreateDtoSpec customDokumentTypCreateDtoSpec = new CustomDokumentTypCreateDtoSpec();
        customDokumentTypCreateDtoSpec.setType("test");
        customDokumentTypCreateDtoSpec.setDescription("test description");
        customDokumentTypCreateDtoSpec.setTrancheId(gesuchTrancheId);
        createdGesuchDokumentWithCustomType = dokumentApiSpec.createCustomDokumentTyp()
            .body(customDokumentTypCreateDtoSpec)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDokumentDto.class);
    }

    @TestAsSachbearbeiter
    @Test
    @Order(7)
    void fehlendeDokumenteUebermitteln() {
        gesuchApiSpec.gesuchFehlendeDokumenteUebermitteln()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(8)
    void getGsDashboardTest() {
        final var fallDashboardItems = gesuchApiSpec.getGsDashboard()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(FallDashboardItemDto[].class);

        assertThat(fallDashboardItems.length, is(1));

        final var fallDashboardItem = fallDashboardItems[0];
        final var ausbildungDashboardItems = fallDashboardItem.getAusbildungDashboardItems();

        assertThat(fallDashboardItem.getNotifications().size(), greaterThanOrEqualTo(1));

        assertThat(ausbildungDashboardItems.size(), greaterThanOrEqualTo(1));

        final var ausbildungDashboardItem = ausbildungDashboardItems.get(0);
        final var gesuchDashboardItems = ausbildungDashboardItem.getGesuchs();

        assertThat(gesuchDashboardItems.size(), greaterThanOrEqualTo(1));

        final var gesuchDashboardItem = gesuchDashboardItems.get(0);

        // since every document is uploaded by GS, there should not be any document missing
        assertThat(gesuchDashboardItem.getMissingDocuments().getCount(), is(greaterThan(0)));
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
