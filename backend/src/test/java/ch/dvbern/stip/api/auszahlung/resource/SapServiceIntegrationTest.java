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

import java.math.BigDecimal;
import java.util.UUID;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.sap.service.SapEndpointService;
import ch.dvbern.stip.api.sap.util.SapReturnCodeType;
import ch.dvbern.stip.api.zahlungsverbindung.entity.Zahlungsverbindung;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SapServiceIntegrationTest {
    /**
     * Important:
     * In this tests, the ACTIVE (INTEGRATION ENVIRONMENT) SAP is used,
     * NOT the mocks!
     *
     */

    @Inject
    SapEndpointService sapEndpointService;

    BigDecimal deliveryid;

    static final Integer TEST_BUSINESS_PARTNER_ID = 1000134830;

    private static @NotNull Auszahlung createAuszahlung() {
        final var auszahlung = new Auszahlung();

        auszahlung.setAuszahlungAnSozialdienst(false);
        auszahlung.setId(UUID.randomUUID());
        auszahlung.setZahlungsverbindung(new Zahlungsverbindung());
        auszahlung.getZahlungsverbindung().setVorname("Vorname");
        auszahlung.getZahlungsverbindung().setNachname("Nachname");
        final var adresse = new Adresse();
        adresse.setStrasse("Teststrasse");
        adresse.setHausnummer("1");
        adresse.setPlz("3000");
        adresse.setOrt("Bern");
        auszahlung.getZahlungsverbindung().setAdresse(adresse);
        auszahlung.getZahlungsverbindung().setIban("CH4689144846113617661");
        return auszahlung;
    }

    // @Test
    @Order(1)
    void createBusinessPartnerTest() {
        final var auszahlung = createAuszahlung();
        deliveryid = SapEndpointService.generateDeliveryId();

        final var businessPartnerCreateResponse =
            sapEndpointService.createBusinessPartner(auszahlung.getBuchhaltung().getFall(), deliveryid);

        assertThat(
            SapReturnCodeType.isSuccess(businessPartnerCreateResponse.getRETURNCODE().get(0).getTYPE()),
            is(true)
        );
    }

    // @Test
    @Order(2)
    void changeBusinessPartnerTest() {
        final var auszahlung = createAuszahlung();
        auszahlung.setSapBusinessPartnerId(TEST_BUSINESS_PARTNER_ID);
        auszahlung.getZahlungsverbindung();
        deliveryid = SapEndpointService.generateDeliveryId();

        final var businessPartnerChangeResponse =
            sapEndpointService.changeBusinessPartner(auszahlung.getBuchhaltung().getFall(), deliveryid);
        assertThat(
            SapReturnCodeType.isSuccess(businessPartnerChangeResponse.getRETURNCODE().get(0).getTYPE()),
            is(true)
        );

    }

    // @Test
    @Order(3)
    void readBusinessPartnerTest() {
        final var auszahlung = createAuszahlung().setSapBusinessPartnerId(TEST_BUSINESS_PARTNER_ID);
        auszahlung.getZahlungsverbindung();

        final var businessPartnerReadResponse =
            sapEndpointService.readBusinessPartner(BigDecimal.ZERO);
        assertThat(
            SapReturnCodeType.isSuccess(businessPartnerReadResponse.getRETURNCODE().get(0).getTYPE()),
            is(false)
        );
    }

    // @Test
    @Order(4)
    void readImportStatusTest() {
        final var importStatusReadResponse = sapEndpointService.readImportStatus(deliveryid);

        assertThat(
            SapReturnCodeType.isSuccess(importStatusReadResponse.getRETURNCODE().get(0).getTYPE()),
            is(true)
        );
    }

    // @Test
    @Order(5)
    void createVendorPostingTest() {
        final var auszahlung = createAuszahlung().setSapBusinessPartnerId(TEST_BUSINESS_PARTNER_ID);
        auszahlung.getZahlungsverbindung();
        deliveryid = SapEndpointService.generateDeliveryId();

        final var vendorPostingCreateResponse = sapEndpointService.createVendorPosting(
            auszahlung.getBuchhaltung().getFall(),
            5,
            deliveryid,
            "",
            String.valueOf(UUID.randomUUID().getMostSignificantBits())
        );

        assertThat(
            SapReturnCodeType.isSuccess(vendorPostingCreateResponse.getRETURNCODE().get(0).getTYPE()),
            is(true)
        );
    }
}
