package ch.dvbern.stip.api.auszahlung.service.sap;

import ch.dvbern.stip.api.auszahlung.sap.businesspartner.change.BusinessPartnerChangeResponse;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.create.BusinessPartnerCreateResponse;
import ch.dvbern.stip.api.auszahlung.sap.vendorposting.VendorPostingCreateResponse;
import jakarta.ws.rs.core.Response;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
@Slf4j
@UtilityClass
public class SAPUtils {
    public BigDecimal generateDeliveryId(){
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextLong(19));
    }

    public String generateExtId(){
        return UUID.randomUUID().toString();
    }

    public void logAsWarningIfNoAction(Response response) {
        if(noSapActionHasBeenPerformed(response)){
            LOG.warn("No SAP action has been performed.");
            logAsWarning(response);
        }
    }

    private <T> boolean noSapActionHasBeenPerformed (Response response) {
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
