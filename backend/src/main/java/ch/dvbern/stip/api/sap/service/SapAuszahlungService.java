package ch.dvbern.stip.api.sap.service;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.sap.util.SAPUtils;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.math.BigDecimal;
@Slf4j
@RequestScoped
public class SapAuszahlungService {
    @Inject SapEndpointService sapEndpointService;
    /**
     * Important: it can take up to 48 hours until a newly created user will be set active in SAP!
     */
    public Integer getOrCreateBusinessPartner(Auszahlung auszahlung) {
        //generate new deliveryId
        BigDecimal deliveryId = SAPUtils.generateDeliveryId();
        if(auszahlung.getSapBusinessPartnerId() != null) {
            //update/sync busniesspartner
            final var response = sapEndpointService.changeBusinessPartner(auszahlung,auszahlung.getSapBusinessPartnerId(), deliveryId);
            if(response.getStatus() != HttpStatus.SC_OK){
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }else{
                SAPUtils.logAsWarningIfNoAction(response);
            }
        }else{
            //generate ext-id
            final var extId = SAPUtils.generateExtId();
            //create new businessparter
            final var createResponse1 = sapEndpointService.createBusinessPartner(auszahlung, extId, deliveryId);
            SAPUtils.logAsWarningIfNoAction(createResponse1);
            final var readResponse = sapEndpointService.readBusniessPartner(extId);
            if(SAPUtils.noSapActionHasBeenPerformed(readResponse)){
                Log.warn("business partner is not yet active in SAP");
            }

        }
        return auszahlung.getSapBusinessPartnerId();
    }

    @Transactional
    public Response createVendorPosting(Auszahlung auszahlung) {
        //generate new deliveryId
        BigDecimal deliveryId = SAPUtils.generateDeliveryId();
        //createOrUpdateBusinessPartner
        Integer businessPartnerId = getOrCreateBusinessPartner(auszahlung);
        if(businessPartnerId != null) {
            //createVendorPosting
            final var response = sapEndpointService.createVendorPosting(auszahlung, businessPartnerId, deliveryId);
            if(response.getStatus() != HttpStatus.SC_OK){
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }else{
                SAPUtils.logAsWarningIfNoAction(response);
            }
            return sapEndpointService.getImportStatus(deliveryId);
        }else{
            return Response.status(HttpStatus.SC_BAD_REQUEST).entity("No vendor posting could be created due to problems").build();
        }

    }



}
