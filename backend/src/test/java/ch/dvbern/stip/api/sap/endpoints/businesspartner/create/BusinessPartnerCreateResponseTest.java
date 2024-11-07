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

package ch.dvbern.stip.api.sap.endpoints.businesspartner.create;

import java.io.IOException;

import ch.dvbern.stip.api.sap.generated.businesspartner.create.BusinessPartnerCreateResponse;
import ch.dvbern.stip.api.sap.service.endpoints.util.SoapUtils;
import ch.dvbern.stip.api.sap.util.SapMessageType;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.soap.SOAPException;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

class BusinessPartnerCreateResponseTest {
    @Test
    void parseBusinessPartnerCreateResponseTest() throws IOException, JAXBException, SOAPException {
        // arrange
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerSuccessResponse.xml"),
            "UTF-8"
        );
        // act
        final var response = SoapUtils.parseSoapResponse(xml, BusinessPartnerCreateResponse.class);
        // assert
        assertNotNull(response);
        assertDoesNotThrow(() -> {
            SoapUtils.parseSoapResponse(xml, BusinessPartnerCreateResponse.class);
        });
        assertEquals(SapMessageType.SUCCESS, SapMessageType.parse(response.getRETURNCODE().get(0).getTYPE()));
    }

    @Test
    void parseBusinessPartnerCreateAlreadyExistingDeliveryIdResponseTest()
    throws IOException, JAXBException, SOAPException {
        // arrange
        String xml = IOUtils.toString(
            this.getClass()
                .getResourceAsStream("/auszahlung/createBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        // act
        final var response = SoapUtils.parseSoapResponse(xml, BusinessPartnerCreateResponse.class);
        // assert
        assertNotNull(response);
        assertDoesNotThrow(() -> {
            SoapUtils.parseSoapResponse(xml, BusinessPartnerCreateResponse.class);
        });
        assertEquals(SapMessageType.ERROR, SapMessageType.parse(response.getRETURNCODE().get(0).getTYPE()));

    }

    @Test
    void parseBusinessPartnerCreateAlreadyExistingBusinessPartnerResponseTest()
    throws IOException, JAXBException, SOAPException {
        // arrange
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessParnterAlreadyExistingPartnerResponse.xml"),
            "UTF-8"
        );
        // act
        final var response = SoapUtils.parseSoapResponse(xml, BusinessPartnerCreateResponse.class);
        // assert
        assertNotNull(response);
        assertDoesNotThrow(() -> {
            SoapUtils.parseSoapResponse(xml, BusinessPartnerCreateResponse.class);
        });
        assertEquals(SapMessageType.ERROR, SapMessageType.parse(response.getRETURNCODE().get(0).getTYPE()));
    }

}
