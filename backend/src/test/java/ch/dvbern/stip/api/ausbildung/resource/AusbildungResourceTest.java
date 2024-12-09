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

package ch.dvbern.stip.api.ausbildung.resource;

import java.time.LocalDate;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.common.service.DateMapperImpl;
import ch.dvbern.stip.api.generator.api.model.gesuch.AusbildungUpdateDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.AusbildungDto;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.KommentarDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AusbildungResourceTest {
    private final AusbildungApiSpec ausbildungApiSpec =
        AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());

    private GesuchDtoSpec gesuch;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void createAusbildungFailEnddateInPast() {
        final var fall = TestUtil.getOrCreateFall(fallApiSpec);
        var ausbildungUpdateDtoSpec = AusbildungUpdateDtoSpecModel.ausbildungUpdateDtoSpec();
        ausbildungUpdateDtoSpec.setFallId(fall.getId());
        ausbildungUpdateDtoSpec.setAusbildungEnd(DateMapperImpl.dateToMonthYear(LocalDate.now().minusYears(1)));

        ausbildungApiSpec.createAusbildung()
            .body(ausbildungUpdateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void createAusbildungFailStartdateInFuture() {
        final var fall = TestUtil.getOrCreateFall(fallApiSpec);
        var ausbildungUpdateDtoSpec = AusbildungUpdateDtoSpecModel.ausbildungUpdateDtoSpec();
        ausbildungUpdateDtoSpec.setFallId(fall.getId());
        ausbildungUpdateDtoSpec.setAusbildungBegin(DateMapperImpl.dateToMonthYear(LocalDate.now().plusYears(2)));
        ausbildungUpdateDtoSpec.setAusbildungEnd(DateMapperImpl.dateToMonthYear(LocalDate.now().plusYears(4)));

        ausbildungApiSpec.createAusbildung()
            .body(ausbildungUpdateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void createAusbildung() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);
        gesuchApiSpec.gesuchEinreichen()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(4)
    void gesuchStatusChangeToInBearbeitungSB() {
        final var foundGesuch = gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDtoSpec.class);

        assertThat(foundGesuch.getGesuchStatus(), is(GesuchstatusDtoSpec.IN_BEARBEITUNG_SB));
    }

    @Test
    @TestAsGesuchsteller
    @Order(5)
    void getAusbildung() {
        final var ausbildung = ausbildungApiSpec.getAusbildung()
            .ausbildungIdPath(gesuch.getAusbildungId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(AusbildungDto.class);

        assertThat(ausbildung.getEditable(), is(false));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(6)
    void updateAusbildungFail() {
        final var ausbildungUpdateDtoSpec = AusbildungUpdateDtoSpecModel.ausbildungUpdateDtoSpec();
        ausbildungUpdateDtoSpec.setId(gesuch.getAusbildungId());
        ausbildungUpdateDtoSpec.setFallId(gesuch.getFallId());
        ausbildungUpdateDtoSpec.setAusbildungEnd(DateMapperImpl.dateToMonthYear(LocalDate.now().minusYears(1)));

        ausbildungApiSpec.updateAusbildung()
            .ausbildungIdPath(gesuch.getAusbildungId())
            .body(ausbildungUpdateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(7)
    void updateAusbildung() {
        final var ausbildungUpdateDtoSpec = AusbildungUpdateDtoSpecModel.ausbildungUpdateDtoSpec();
        ausbildungUpdateDtoSpec.setId(gesuch.getAusbildungId());
        ausbildungUpdateDtoSpec.setFallId(gesuch.getFallId());
        final String ausbildungsOrtToSet = "Bielefeld";

        ausbildungUpdateDtoSpec.setAusbildungsort(ausbildungsOrtToSet);
        final var updatedAusbildung = ausbildungApiSpec.updateAusbildung()
            .ausbildungIdPath(gesuch.getAusbildungId())
            .body(ausbildungUpdateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(AusbildungDto.class);

        assertThat(updatedAusbildung.getAusbildungsort(), is(ausbildungsOrtToSet));
        gesuchApiSpec.gesuchZurueckweisen()
            .gesuchIdPath(gesuch.getId())
            .body(
                new KommentarDtoSpec()
                    .text("DONT_CARE")
            )
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(99)
    void deleteAusbildung() {
        TestUtil.deleteAusbildung(gesuchApiSpec, gesuch.getAusbildungId());
    }
}
