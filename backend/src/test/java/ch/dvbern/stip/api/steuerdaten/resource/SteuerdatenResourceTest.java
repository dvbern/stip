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

package ch.dvbern.stip.api.steuerdaten.resource;

import java.util.List;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsSuperUser;
import ch.dvbern.stip.api.generator.api.model.gesuch.AdresseSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.SteuerdatenUpdateTabsDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.StepwiseExtension.AlwaysRun;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.AuszahlungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.SteuerdatenApiSpec;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.FallAuszahlungDto;
import ch.dvbern.stip.generated.dto.FallAuszahlungDtoSpec;
import ch.dvbern.stip.generated.dto.FallDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.SteuerdatenTypDtoSpec;
import ch.dvbern.stip.generated.dto.ValidationReportDtoSpec;
import ch.dvbern.stip.generated.dto.ZahlungsverbindungDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response.Status;
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
class SteuerdatenResourceTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final SteuerdatenApiSpec steuerdatenApiSpec = SteuerdatenApiSpec.steuerdaten(RequestSpecUtil.quarkusSpec());

    private final AuszahlungApiSpec auszahlungApiSpec = AuszahlungApiSpec.auszahlung(RequestSpecUtil.quarkusSpec());
    private GesuchDtoSpec gesuch;
    private FallDtoSpec fall;

    private FallAuszahlungDtoSpec auszahlung;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void gesuchErstellen() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        fall = TestUtil.getFall(fallApiSpec).orElseThrow(() -> new RuntimeException("Failed to create/ get fall"));

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
    void createAuszahlung() {
        var auszahlungDtoSpec = new AuszahlungUpdateDtoSpec();
        auszahlungDtoSpec.setAuszahlungAnSozialdienst(false);
        ZahlungsverbindungDtoSpec zahlungsverbindungDtoSpec = new ZahlungsverbindungDtoSpec();
        final var adresse =
            AdresseSpecModel.adresseDtoSpec();
        zahlungsverbindungDtoSpec.setIban(TestConstants.IBAN_CH_NUMMER_VALID);
        zahlungsverbindungDtoSpec.setVorname("Max");
        zahlungsverbindungDtoSpec.setNachname("Muster");
        zahlungsverbindungDtoSpec.setAdresse(adresse);
        auszahlungDtoSpec.setZahlungsverbindung(zahlungsverbindungDtoSpec);

        auszahlungApiSpec.createAuszahlungForGesuch()
            .fallIdPath(fall.getId())
            .body(auszahlungDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK_200)
            .extract()
            .body()
            .as(FallAuszahlungDto.class);
    }

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void gesuchEinreichen() {
        gesuchApiSpec.gesuchEinreichenGs()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(5)
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

    @Test
    @TestAsSachbearbeiter
    @Order(6)
    void gesuchAddSteuerdaten() {
        final var steuerdatenUpdateDto =
            SteuerdatenUpdateTabsDtoSpecModel.steuerdatenDtoSpec(SteuerdatenTypDtoSpec.FAMILIE);
        steuerdatenApiSpec.updateSteuerdaten()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .body(List.of(steuerdatenUpdateDto))
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(7)
    void gesuchAddSteuerdatenBadType() {
        final var steuerdatenUpdateDto =
            SteuerdatenUpdateTabsDtoSpecModel.steuerdatenDtoSpec(SteuerdatenTypDtoSpec.MUTTER);
        final var validationReport = steuerdatenApiSpec.updateSteuerdaten()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .body(List.of(steuerdatenUpdateDto))
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.BAD_REQUEST.getStatusCode())
            .extract()
            .body()
            .as(ValidationReportDtoSpec.class);
        assertThat(
            validationReport.getValidationErrors().get(0).getMessageTemplate(),
            equalTo("{jakarta.validation.constraints.steuerdaten.tab.invalid.message}")
        );
    }

    @Test
    @TestAsSachbearbeiter
    @Order(8)
    void gesuchAddSteuerdatenBadYear() {
        final var steuerdatenUpdateDto =
            SteuerdatenUpdateTabsDtoSpecModel.steuerdatenDtoSpec(SteuerdatenTypDtoSpec.FAMILIE);
        steuerdatenUpdateDto.setSteuerjahr(2099);
        final var validationReport = steuerdatenApiSpec.updateSteuerdaten()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .body(List.of(steuerdatenUpdateDto))
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.BAD_REQUEST.getStatusCode())
            .extract()
            .body()
            .as(ValidationReportDtoSpec.class);
        assertThat(
            validationReport.getValidationErrors().get(0).getMessageTemplate(),
            equalTo("{jakarta.validation.constraints.steuerdaten.steuerjahr.invalid.message}")
        );
    }

    @Test
    @TestAsGesuchsteller
    @Order(9)
    void getAndSetAsGSShouldFail() {
        steuerdatenApiSpec.getSteuerdaten()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(
                Status.FORBIDDEN.getStatusCode()
            );

        final var steuerdatenUpdateDto =
            SteuerdatenUpdateTabsDtoSpecModel.steuerdatenDtoSpec(SteuerdatenTypDtoSpec.FAMILIE);
        steuerdatenApiSpec.updateSteuerdaten()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .body(List.of(steuerdatenUpdateDto))
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @TestAsSuperUser
    @AlwaysRun
    @Order(99)
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }
}
