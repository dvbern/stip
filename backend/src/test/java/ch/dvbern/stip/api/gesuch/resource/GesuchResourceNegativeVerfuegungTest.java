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

package ch.dvbern.stip.api.gesuch.resource;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsSuperUser;
import ch.dvbern.stip.api.generator.api.model.gesuch.AdresseSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.CreateGesuchTrancheRequestDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.AuszahlungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchTrancheApiSpec;
import ch.dvbern.stip.generated.api.StipDecisionApiSpec;
import ch.dvbern.stip.generated.dto.AusgewaehlterGrundDtoSpec;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.FallAuszahlungDto;
import ch.dvbern.stip.generated.dto.FallDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheListDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.StipDecisionTextDto;
import ch.dvbern.stip.generated.dto.ZahlungsverbindungDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import wiremock.org.eclipse.jetty.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class GesuchResourceNegativeVerfuegungTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec =
        GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final AuszahlungApiSpec auszahlungApiSpec = AuszahlungApiSpec.auszahlung(RequestSpecUtil.quarkusSpec());
    private final StipDecisionApiSpec stipDecisionApiSpec =
        StipDecisionApiSpec.stipDecision(RequestSpecUtil.quarkusSpec());
    private GesuchDtoSpec gesuch;
    private FallDtoSpec fall;

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
        TestUtil.fillGesuchWithAuszahlung(gesuchApiSpec, dokumentApiSpec, auszahlungApiSpec, gesuch);
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
        TestUtil.executeAndAssertOk(
            gesuchApiSpec.gesuchEinreichenGs()
                .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
        );

        final var gesuchTranchen = TestUtil.executeAndExtract(
            GesuchTrancheListDtoSpec.class,
            gesuchTrancheApiSpec.getAllTranchenForGesuchGS().gesuchIdPath(gesuch.getId())
        );

        assertThat("Gesuch was eingereicht with != 1 Tranchen", gesuchTranchen.getTranchen(), hasSize(1));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(5)
    void trancheErstellen() {
        gesuchApiSpec.changeGesuchStatusToBereitFuerBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
        TestUtil.executeAndAssertOk(
            gesuchApiSpec.changeGesuchStatusToInBearbeitung()
                .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
        );

        TestUtil.executeAndAssertOk(
            gesuchTrancheApiSpec.createGesuchTrancheCopy()
                .gesuchIdPath(gesuch.getId())
                .body(CreateGesuchTrancheRequestDtoSpecModel.createGesuchTrancheRequestDtoSpec(gesuch))
        );

        assertSBTranchenCount("No Tranche was created (full override?)", 2);
    }

    @Test
    @TestAsSachbearbeiter
    @Order(6)
    void negativeVerfuegung() {
        var decision = stipDecisionApiSpec.getAll()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(StipDecisionTextDto[].class)[0];

        var ausgewaehlterGrund = new AusgewaehlterGrundDtoSpec();
        ausgewaehlterGrund.setDecisionId(decision.getId());
        TestUtil.executeAndAssertOk(
            gesuchApiSpec.changeGesuchStatusToNegativeVerfuegung()
                .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
                .body(ausgewaehlterGrund)
        );

    }

    @Test
    @TestAsSachbearbeiter
    @Order(7)
    void gesuchVersenden() {
        TestUtil.executeAndAssertOk(
            gesuchApiSpec.changeGesuchStatusToVersendet()
                .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
        );
    }

    @Test
    @TestAsSachbearbeiter
    @Order(8)
    void gesuchShouldBeInState_Kein_Stipendienanspruch() {
        var gesuchWithChanges = gesuchApiSpec.getGesuchSB()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);

        Assertions.assertThat(gesuchWithChanges.getGesuchStatus())
            .satisfiesAnyOf(
                status -> Assertions.assertThat(status).isEqualTo(GesuchstatusDtoSpec.KEIN_STIPENDIENANSPRUCH)
            );
    }

    @Test
    @TestAsSuperUser
    @Order(99)
    @StepwiseExtension.AlwaysRun
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }

    private void assertSBTranchenCount(final String message, final int size) {
        final var gesuchTranchen = TestUtil.executeAndExtract(
            GesuchTrancheListDtoSpec.class,
            gesuchTrancheApiSpec.getAllTranchenForGesuchSB().gesuchIdPath(gesuch.getId())
        );

        assertThat(message, gesuchTranchen.getTranchen(), hasSize(size));
    }
}
