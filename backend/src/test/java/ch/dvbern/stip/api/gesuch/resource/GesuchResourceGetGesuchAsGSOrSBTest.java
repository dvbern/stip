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

import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.BenutzerApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchTrancheApiSpec;
import ch.dvbern.stip.generated.dto.EinnahmenKostenDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
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
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GesuchResourceGetGesuchAsGSOrSBTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec =
        GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final BenutzerApiSpec benutzerApiSpec = BenutzerApiSpec.benutzer(RequestSpecUtil.quarkusSpec());
    private static UUID gesuchId;
    private static UUID trancheId;
    private GesuchDtoSpec gesuch;
    private GesuchWithChangesDtoSpec gesuchWithChanges;
    private EinnahmenKostenDtoSpec ekBeforeUpdate;

    private final int wohnkostenChangeValue = 77;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void gesuchErstellen() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void fillGesuch() {
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);
    }

    @Test
    @TestAsSachbearbeiter
    @Order(3)
    void checkSBReceivesEmptyChanges() {
        gesuchWithChanges = gesuchApiSpec.getGesuchSB()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
        assertThat(gesuchWithChanges.getChanges().size(), is(0));
    }

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void gesuchEinreichen() {
        gesuchApiSpec.gesuchEinreichen()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(5)
    void setStatusInBearbeitungSb() {
        gesuchWithChanges = gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
    }

    // make some changes to Gesuch
    @Test
    @TestAsSachbearbeiter
    @Order(6)
    void updateGesuch() {
        gesuchId = gesuchWithChanges.getId();
        trancheId = gesuchWithChanges.getGesuchTrancheToWorkWith().getId();
        ekBeforeUpdate = gesuchWithChanges.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten();
        var piaUpdate = GesuchTestSpecGenerator.gesuchUpdateDtoSpecPersonInAusbildung()
            .getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung();
        piaUpdate
            .setAdresse(
                gesuchWithChanges.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().getAdresse()
            );
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecEinnahmenKosten();
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().setPersonInAusbildung(piaUpdate);
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuchWithChanges.getGesuchTrancheToWorkWith().getId());
        gesuchUpdateDTO.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setWohnkosten(wohnkostenChangeValue);
        gesuchApiSpec.updateGesuch()
            .gesuchIdPath(gesuchId)
            .body(gesuchUpdateDTO)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(7)
    void checkSBReceivesCurrentStateOfTranche() {
        gesuchWithChanges = gesuchApiSpec.getGesuchSB()
            .gesuchTrancheIdPath(trancheId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
        assertThat(
            gesuchWithChanges.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().getWohnkosten(),
            is(wohnkostenChangeValue)
        );
        assertThat(
            gesuchWithChanges.getChanges().get(0).getGesuchFormular().getEinnahmenKosten().getWohnkosten(),
            is(ekBeforeUpdate.getWohnkosten())
        );
        assertThat(gesuchWithChanges.getChanges().size(), is(greaterThan(0)));
    }

    @Test
    @TestAsAdmin
    @Order(99)
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuchId);
    }
}
