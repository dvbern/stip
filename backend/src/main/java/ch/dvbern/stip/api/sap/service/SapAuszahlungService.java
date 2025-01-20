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

package ch.dvbern.stip.api.sap.service;

import java.math.BigDecimal;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.sap.util.SAPUtils;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
public class SapAuszahlungService {
    @Inject
    SapEndpointService sapEndpointService;

    /**
     * Important: it can take up to 48 hours until a newly created user will be set active in SAP!
     */
    public Integer getOrCreateBusinessPartner(Auszahlung auszahlung) {
        // generate new deliveryId
        BigDecimal deliveryId = SAPUtils.generateDeliveryId();
        if (auszahlung.getSapBusinessPartnerId() != null) {
            // update/sync busniesspartner
            final var response =
                sapEndpointService.changeBusinessPartner(auszahlung, auszahlung.getSapBusinessPartnerId(), deliveryId);
            if (response.getStatus() != Status.OK.getStatusCode()) {
                throw new WebApplicationException(Status.BAD_REQUEST);
            } else {
                SAPUtils.logAsWarningIfNoAction(response);
            }
        } else {
            // generate ext-id
            final var extId = SAPUtils.generateExtId();
            // create new businessparter
            final var createResponse1 = sapEndpointService.createBusinessPartner(auszahlung, extId, deliveryId);
            SAPUtils.logAsWarningIfNoAction(createResponse1);
            final var readResponse = sapEndpointService.readBusniessPartner(extId);
            if (SAPUtils.noSapActionHasBeenPerformed(readResponse)) {
                Log.warn("business partner is not yet active in SAP");
            }

        }
        return auszahlung.getSapBusinessPartnerId();
    }

    @Transactional
    public Response createVendorPosting(Auszahlung auszahlung) {
        // todo: KSTIP 1229: add proper handling for newly created
        // user that are not yet active in SAP
        // todo: KSTIP 1229: add proper handling for "no action" events
        // generate new deliveryId
        BigDecimal deliveryId = SAPUtils.generateDeliveryId();
        // createOrUpdateBusinessPartner
        Integer businessPartnerId = getOrCreateBusinessPartner(auszahlung);
        if (businessPartnerId != null) {
            // createVendorPosting
            final var response = sapEndpointService.createVendorPosting(auszahlung, businessPartnerId, deliveryId);
            if (response.getStatus() != Status.OK.getStatusCode()) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            } else {
                SAPUtils.logAsWarningIfNoAction(response);
            }
            return sapEndpointService.getImportStatus(deliveryId);
        } else {
            return Response.status(Status.BAD_REQUEST)
                .entity("No vendor posting could be created due to problems")
                .build();
        }

    }

}
