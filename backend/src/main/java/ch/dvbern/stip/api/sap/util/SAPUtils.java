package ch.dvbern.stip.api.sap.util;

import ch.dvbern.stip.api.sap.service.endpoints.businesspartner.change.BusinessPartnerChangeResponse;
import ch.dvbern.stip.api.sap.service.endpoints.businesspartner.create.BusinessPartnerCreateResponse;
import ch.dvbern.stip.api.sap.service.endpoints.businesspartner.read.BusinessPartnerReadResponse;
import ch.dvbern.stip.api.sap.service.endpoints.vendorposting.VendorPostingCreateResponse;
import jakarta.ws.rs.core.Response;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
@Slf4j
@UtilityClass
public class SAPUtils {
    public BigDecimal generateDeliveryId(){
        /*length should be max 19;
         */        return BigDecimal.valueOf(Math.abs(ThreadLocalRandom.current().nextLong()));
    }

    public String generateExtId(){
        /*length should be max 20;
         */
        return String.valueOf(BigDecimal.valueOf(Math.abs(ThreadLocalRandom.current().nextLong())));
    }

    public void logAsWarningIfNoAction(Response response) {
        if(noSapActionHasBeenPerformed(response)){
            LOG.warn("No SAP action has been performed.");
            logAsWarning(response);
        }
    }

    //todo: make generic
    public <T> boolean noSapActionHasBeenPerformed (Response response) {
        if(response.getEntity() instanceof VendorPostingCreateResponse){
            return SapMessageType.valueOf(
                    ((VendorPostingCreateResponse) response.getEntity()).getRETURNCODE().get(0).getTYPE())
                .equals(SapMessageType.E);
        }else if(response.getEntity() instanceof BusinessPartnerCreateResponse){
            return SapMessageType.valueOf(
                    ((BusinessPartnerCreateResponse) response.getEntity()).getRETURNCODE().get(0).getTYPE())
                .equals(SapMessageType.E);
        }else if (response.getEntity() instanceof BusinessPartnerChangeResponse){
            return SapMessageType.valueOf(
                    ((BusinessPartnerChangeResponse) response.getEntity()).getRETURNCODE().get(0).getTYPE())
                .equals(SapMessageType.E);
        }else if(response.getEntity() instanceof BusinessPartnerReadResponse){
            return SapMessageType.valueOf(
                    ((BusinessPartnerReadResponse) response.getEntity()).getRETURNCODE().get(0).getTYPE())
                .equals(SapMessageType.E);
        }
        return false;
    }

    private void logAsWarning(Response response) {
        String message = "";
        if(response.getEntity() instanceof VendorPostingCreateResponse){
            message = ((VendorPostingCreateResponse) response.getEntity()).getRETURNCODE().get(0).getMESSAGE();
        }else if(response.getEntity() instanceof BusinessPartnerCreateResponse){
            message = ((BusinessPartnerCreateResponse) response.getEntity()).getRETURNCODE().get(0).getMESSAGE();
        }else if (response.getEntity() instanceof BusinessPartnerChangeResponse){
            message = ((BusinessPartnerChangeResponse) response.getEntity()).getRETURNCODE().get(0).getMESSAGE();
        }
        LOG.warn(message);
    }
}
