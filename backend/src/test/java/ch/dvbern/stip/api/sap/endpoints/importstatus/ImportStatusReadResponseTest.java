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

package ch.dvbern.stip.api.sap.endpoints.importstatus;

import java.io.IOException;

import ch.dvbern.stip.api.sap.generated.importstatus.ImportStatusReadResponse;
import ch.dvbern.stip.api.sap.service.endpoints.util.SoapUtils;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.soap.SOAPException;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ImportStatusReadResponseTest {
    @Test
    void parseImportStatusResponseTest() throws IOException, JAXBException, SOAPException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/getImportStatusExampleResponse.xml"),
            "UTF-8"
        );
        final var response = SoapUtils.parseSoapResponse(xml, ImportStatusReadResponse.class);
        assertNotNull(response);
    }

    @Test
    void parseImportStatusNotFoundResponseTest() throws IOException, JAXBException, SOAPException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/getImportStatusDeliveryIdNotFoundExampleResponse.xml"),
            "UTF-8"
        );
        final var response = SoapUtils.parseSoapResponse(xml, ImportStatusReadResponse.class);
        assertNotNull(response);
    }

}
