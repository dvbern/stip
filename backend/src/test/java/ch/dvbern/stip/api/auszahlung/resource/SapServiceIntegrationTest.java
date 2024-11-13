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

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.service.AuszahlungMapper;
import ch.dvbern.stip.api.sap.service.SapAuszahlungService;
import ch.dvbern.stip.api.sap.service.SapEndpointService;
import ch.dvbern.stip.api.sap.util.SAPUtils;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.apache.http.HttpStatus;

import static ch.dvbern.stip.api.generator.entities.service.AuszahlungGenerator.initAuszahlung;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class SapServiceIntegrationTest {
    /**
     * Important:
     * In this tests, the ACTIVE (INTEGRATION ENVIRONMENT) SAP is used,
     * NOT the mocks!
     *
     */

    @Inject
    SapEndpointService auszahlungSapService;

    @Inject
    SapAuszahlungService sapAuszahlungService;

    @Inject
    AuszahlungMapper auszahlungMapper;

    // @Test
    void getImportStatus() {
        final var response = auszahlungSapService.getImportStatus(SAPUtils.generateDeliveryId());
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    /*
     * todo KSTIP-1229: if a new businesspartner is created, it can take up do 48 hours until he's available in SAP
     */
    /*
     * @Test
     * void createBusinessPartnerTest() {
     * fail();
     * assertDoesNotThrow(() -> sapAuszahlungService.getOrCreateBusinessPartner(initAuszahlung()));
     * }
     *
     *
     */
    // @Test
    void createBusinessPartnerTest_invalidRequest() {
        assertNull(sapAuszahlungService.getOrCreateBusinessPartner(initAuszahlung()));
    }

    /*
     * todo KSTIP-1229: if a new businesspartner is created, it can take up do 48 hours until he's available in SAP
     */
    /*
     * @Test
     * void createVendorPostingTest_New_BusinessPartner_Success() {
     * fail();
     * Auszahlung auszahlung= initAuszahlung();
     * assertNotNull(sapAuszahlungService.createVendorPosting(auszahlung));
     * }
     *
     */
    // @Test
    void createVendorPostingTest_Existing_BusinessPartner_Success() {
        Integer bpId = 1000569588;
        Auszahlung auszahlung = initAuszahlung();
        auszahlung.setSapBusinessPartnerId(bpId);
        assertNotNull(sapAuszahlungService.createVendorPosting(auszahlung));
    }

    // @Test
    void createVendorPostingTest_alreadyExistingDeliveryId() {
        Auszahlung auszahlung = initAuszahlung();
        auszahlung.setSapBusinessPartnerId(0);
        assertThrows(WebApplicationException.class, () -> {
            sapAuszahlungService.createVendorPosting(auszahlung);
        });
    }

}
