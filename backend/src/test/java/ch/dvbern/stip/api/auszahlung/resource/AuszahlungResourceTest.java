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

package ch.dvbern.stip.api.auszahlung.resource;

import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.generator.api.model.gesuch.AdresseSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.AuszahlungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.AuszahlungDtoSpec;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.ZahlungsverbindungDto;
import ch.dvbern.stip.generated.dto.ZahlungsverbindungDtoSpec;
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

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class AuszahlungResourceTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final AuszahlungApiSpec auszahlungApiSpec = AuszahlungApiSpec.auszahlung(RequestSpecUtil.quarkusSpec());
    private GesuchDtoSpec gesuch;

    private UUID auszahlungId;
    private AuszahlungDtoSpec auszahlung;
    private ZahlungsverbindungDto zahlungsverbindungDto;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void setupCreateGesuch() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void setupFillGesuch() {
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void createAuszahlung() {
        AuszahlungDtoSpec auszahlungDtoSpec = new AuszahlungDtoSpec();
        auszahlungDtoSpec.setAuszahlungAnSozialdienst(false);
        ZahlungsverbindungDtoSpec zahlungsverbindungDtoSpec = new ZahlungsverbindungDtoSpec();
        final var adresse =
            AdresseSpecModel.adresseDtoSpec();
        zahlungsverbindungDtoSpec.setIban(TestConstants.IBAN_CH_NUMMER_VALID);
        zahlungsverbindungDtoSpec.setVorname("Max");
        zahlungsverbindungDtoSpec.setNachname("Muster");
        zahlungsverbindungDtoSpec.setAdresse(adresse);
        auszahlungDtoSpec.setZahlungsverbindung(zahlungsverbindungDtoSpec);

        auszahlungId = auszahlungApiSpec.createAuszahlungForGesuch()
            .gesuchIdPath(gesuch.getId())
            .body(auszahlungDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK_200)
            .extract()
            .body()
            .as(UUID.class);
    }

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void getAuszahlungForGesuch() {
        auszahlung = auszahlungApiSpec.getAuszahlungForGesuch()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK_200)
            .extract()
            .body()
            .as(AuszahlungDtoSpec.class);
    }

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void updateAuszahlungForGesuch() {
        var auszahlungUpdate = new AuszahlungUpdateDtoSpec();
        auszahlungUpdate.setAuszahlungAnSozialdienst(auszahlung.getAuszahlungAnSozialdienst());
        auszahlungUpdate.setZahlungsverbindung(auszahlung.getZahlungsverbindung());

        var updatedAuszahlung = auszahlungApiSpec.updateAuszahlungForGesuch()
            .gesuchIdPath(gesuch.getId())
            .body(auszahlungUpdate)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK_200)
            .extract()
            .body()
            .as(AuszahlungDtoSpec.class);
    }

}
