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

package ch.dvbern.stip.api.beschwerdeverlauf.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import ch.dvbern.stip.generated.api.GesuchNotizApiSpec;
import ch.dvbern.stip.generated.dto.BeschwerdeVerlaufEntryCreateDtoSpec;
import ch.dvbern.stip.generated.dto.BeschwerdeVerlaufEntryDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchNotizDto;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import wiremock.org.eclipse.jetty.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class GesuchResourceBeschwerdeVerlaufTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final GesuchNotizApiSpec gesuchNotizApiSpec = GesuchNotizApiSpec.gesuchNotiz(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());

    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    // create a gesuch
    private GesuchDtoSpec gesuch;
    private GesuchNotizDto juristischeAbklaerungNotizDto;
    private GesuchNotizDto abklaerungNotizDto;
    private GesuchNotizDto gesuchNotizDto;

    private List<GesuchDto> gesucheGS = new ArrayList<>();

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void gesuchErstellen() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        // create a second gesuch for the same fall
        TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);

        var gesuche = gesuchApiSpec.getGesucheGs()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(GesuchDto[].class);
        gesucheGS = Arrays.stream(gesuche).toList();
    }

    // todo: assert flag is false

    // create a notiz as SB
    @Test
    @TestAsSachbearbeiter
    @Order(2)
    void createBeschwerdeVerlaufEntry() {
        BeschwerdeVerlaufEntryCreateDtoSpec createDto = new BeschwerdeVerlaufEntryCreateDtoSpec();
        createDto.setBeschwerdeSetTo(true);
        createDto.setKommentar("test");

        final var createdEntry = gesuchApiSpec.createBeschwerdeVerlaufEntry()
            .gesuchIdPath(gesuch.getId())
            .body(createDto)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK_200)
            .extract()
            .body()
            .as(BeschwerdeVerlaufEntryDtoSpec.class);
        assertThat(createdEntry, is(notNullValue()));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(3)
    void getAllBeschwerdeVerlauf() {
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

    // todo: try to set flag to true again -> bad request

    @Test
    @Order(99)
    @TestAsAdmin
    @StepwiseExtension.AlwaysRun
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }
}
