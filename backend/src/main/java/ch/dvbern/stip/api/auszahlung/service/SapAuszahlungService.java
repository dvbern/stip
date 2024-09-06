package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.create.BusinessPartnerCreateResponse;
import ch.dvbern.stip.api.auszahlung.service.sap.SAPUtils;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.apache.http.HttpStatus;

import java.math.BigDecimal;

@RequestScoped
public class SapAuszahlungService {
    @Inject SapEndpointService sapEndpointService;
    /**
     * Important: in order to create a new (non-existing in SAP) businesspartner,
     * the "Create"-Endpoint has to be called twice!
     * first call: create businesspartner
     * second call: extract busniesspartner-id from response in order to proceed
     */
    public Integer getOrCreateBusinessPartner(Auszahlung auszahlung) {
        //generate new deliveryId
        BigDecimal deliveryId = SAPUtils.generateDeliveryId();
        if(auszahlung.getSapBusinessPartnerId() != null) {
            //update/sync busniesspartner
            if(sapEndpointService.changeBusinessPartner(auszahlung,auszahlung.getSapBusinessPartnerId(),deliveryId).getStatus() != HttpStatus.SC_OK){
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
        }else{
            //generate ext-id
            final var extId = SAPUtils.generateExtId();
            //create new businessparter
            //note: endpoint has to be called 2x in order to extract busniesspartner-id
            // 1. call
           sapEndpointService.createBusinessPartner(auszahlung,extId,deliveryId);
            //2. call
            final var createResponse2 = sapEndpointService.createBusinessPartner(auszahlung,extId,deliveryId);
            //extract busniessParnterId
            if(createResponse2.getEntity() == null ||
                ((BusinessPartnerCreateResponse) createResponse2.getEntity())
                .getBUSINESSPARTNER() == null) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }else{
                Integer businessPartnerId = Integer.valueOf(((BusinessPartnerCreateResponse) createResponse2.getEntity())
                    .getBUSINESSPARTNER().getHEADER().getBPARTNER());
                auszahlung.setSapBusinessPartnerId(businessPartnerId);
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
        //createVendorPosting
        if(sapEndpointService.createVendorPosting(auszahlung,businessPartnerId,deliveryId).getStatus() != HttpStatus.SC_OK){
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        };
        return sapEndpointService.getImportStatus(deliveryId);
    }

}
