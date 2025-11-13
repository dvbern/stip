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
import ch.dvbern.stip.api.generator.api.model.gesuch.CreateGesuchTrancheRequestDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.StepwiseExtension.AlwaysRun;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.AuszahlungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchTrancheApiSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheListDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import ch.dvbern.stip.generated.dto.KommentarDtoSpec;
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
class GesuchZurueckweisenTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec =
        GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final AuszahlungApiSpec auszahlungApiSpec = AuszahlungApiSpec.auszahlung(RequestSpecUtil.quarkusSpec());
    private GesuchDtoSpec gesuch;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void gesuchErstellen() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        TestUtil.fillAuszahlung(gesuch.getFallId(), auszahlungApiSpec, TestUtil.getAuszahlungUpdateDtoSpec());
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
    @Order(4)
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
    @Order(5)
    void gesuchZurueckweisen() {
        TestUtil.executeAndAssertOk(
            gesuchApiSpec.gesuchZurueckweisen()
                .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
                .body(new KommentarDtoSpec().text("Reset Gesuch for Testing"))
        );

        assertSBTranchenCount("Multiple Tranchen still exist after Resetting", 1);
    }

    @Order(6)
    @TestAsGesuchsteller
    @Test
    void gesuchEinreichen2() {
        TestUtil.executeAndAssertOk(
            gesuchApiSpec.gesuchEinreichenGs()
                .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
        );
    }

    @Order(7)
    @TestAsSachbearbeiter
    @Test
    void changeGesuchToBereitFuerBearbeitungShouldFail() {
        // because all Datenschutzbriefe have been deleted, it should not be possible now
        // to change the gesuchstatus back to BEREIT_FUER_BEARBEITUNG directly...

        TestUtil.executeAndAssert(
            gesuchApiSpec.changeGesuchStatusToInBearbeitung()
                .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId()),
            HttpStatus.FORBIDDEN_403
        );
    }

    @Test
    @TestAsSuperUser
    @Order(99)
    @AlwaysRun
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
