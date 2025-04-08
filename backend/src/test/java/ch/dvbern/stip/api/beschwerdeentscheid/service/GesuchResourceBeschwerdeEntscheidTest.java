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
import ch.dvbern.stip.generated.dto.BeschwerdeEntscheidDtoSpec;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchInfoDto;
import com.mchange.io.FileUtils;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

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

    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    // create a gesuch
    private GesuchDtoSpec gesuch;
    private UUID dokumentId;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void gesuchErstellen() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        // create a second gesuch for the same fall
        TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);

        var gesuchInfo = gesuchApiSpec.getGesuchInfo()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(GesuchInfoDto.class);
    }

    @Test
    @TestAsSachbearbeiter
    @Order(2)
    void createBeschwerdeEntscheid() {
        final var file = TestUtil.getTestPng();
        TestUtil.uploadBeschwerdeEntscheid(gesuchApiSpec, gesuch.getId(), true, "test", file)
            .assertThat()
            .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(3)
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
        assertThat(entrys.length, is(1));

        final var entry = entrys[0];
        assertThat(entry.getKommentar(), is("test"));
        assertThat(entry.getIsBeschwerdeErfolgreich(), is(true));
        assertThat(entry.getDokumente().size(), is(1));
        dokumentId = entry.getDokumente().get(0).getId();
    }

    @Test
    @TestAsSachbearbeiter
    @Order(4)
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
