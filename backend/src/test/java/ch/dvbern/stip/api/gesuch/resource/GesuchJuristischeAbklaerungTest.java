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
import ch.dvbern.stip.api.benutzer.util.TestAsJurist;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsSuperUser;
import ch.dvbern.stip.api.generator.api.model.gesuch.AusbildungUpdateDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchTrancheApiSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchInfoDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheListDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class GesuchJuristischeAbklaerungTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec =
        GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());

    private GesuchDtoSpec gesuchDtoSpec;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void gesuchErstellen() {
        gesuchDtoSpec = TestUtil.createGesuchAusbildungFallWithAusbildung(
            fallApiSpec,
            ausbildungApiSpec,
            AusbildungUpdateDtoSpecModel.customAusbildungUpdateDtoSpec(),
            gesuchApiSpec
        );
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void fillGesuch() {
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuchDtoSpec);
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void gesuchEinreichenGs() {
        TestUtil.executeAndAssertOk(
            gesuchApiSpec.gesuchEinreichenGs()
                .gesuchTrancheIdPath(gesuchDtoSpec.getGesuchTrancheToWorkWith().getId())
        );

        final var gesuchTranchen = TestUtil.executeAndExtract(
            GesuchTrancheListDtoSpec.class,
            gesuchTrancheApiSpec.getAllTranchenForGesuchGS().gesuchIdPath(gesuchDtoSpec.getId())
        );

        assertThat("Gesuch was eingereicht with != 1 Tranchen", gesuchTranchen.getTranchen(), hasSize(1));
    }

    @Test
    @TestAsJurist
    @Order(4)
    void ausbildungAnpassen() {
        final var gesuchInfo = TestUtil.executeAndExtract(
            GesuchInfoDtoSpec.class,
            gesuchApiSpec.getGesuchInfo().gesuchIdPath(gesuchDtoSpec.getId())
        );
        assertThat(
            "Gesuch ist in Abklaerung durch Rechtsabteilung",
            gesuchInfo.getGesuchStatus(),
            is(GesuchstatusDtoSpec.ABKLAERUNG_DURCH_RECHSTABTEILUNG)
        );
        final var ausbildungDto = AusbildungUpdateDtoSpecModel.ausbildungUpdateDtoSpec();
        ausbildungDto.setId(gesuchDtoSpec.getAusbildungId());
        ausbildungDto.setFallId(gesuchDtoSpec.getFallId());
        TestUtil.executeAndAssertOk(
            ausbildungApiSpec.updateAusbildung()
                .ausbildungIdPath(gesuchDtoSpec.getAusbildungId())
                .body(ausbildungDto)
        );
    }

    @Test
    @TestAsSachbearbeiter
    @Order(5)
    void gesuchEinreichenAsSBShouldFail() {
        gesuchApiSpec.gesuchEinreichenGs()
            .gesuchTrancheIdPath(gesuchDtoSpec.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(6)
    void gesuchEinreichenAsGSShouldFail() {
        gesuchApiSpec.gesuchEinreichenGs()
            .gesuchTrancheIdPath(gesuchDtoSpec.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.FORBIDDEN.getStatusCode())
            .extract()
            .response();
    }

    @Test
    @TestAsJurist
    @Order(7)
    void gesuchManuellPruefenJurShouldWork() {
        gesuchApiSpec.gesuchManuellPruefenJur()
            .gesuchTrancheIdPath(gesuchDtoSpec.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsSuperUser
    @Order(99)
    void test_delete_gesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuchDtoSpec.getId());
    }
}
