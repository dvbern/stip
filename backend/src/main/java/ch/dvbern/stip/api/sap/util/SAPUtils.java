package ch.dvbern.stip.api.sap.util;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import ch.dvbern.stip.api.sap.generated.businesspartner.change.BusinessPartnerChangeResponse;
import ch.dvbern.stip.api.sap.generated.businesspartner.create.BusinessPartnerCreateResponse;
import ch.dvbern.stip.api.sap.generated.businesspartner.read.BusinessPartnerReadResponse;
import ch.dvbern.stip.api.sap.generated.vendorposting.VendorPostingCreateResponse;
import jakarta.ws.rs.core.Response;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class SAPUtils {
    public BigDecimal generateDeliveryId() {
        /*length should be max 19;*/
        return BigDecimal.valueOf(Math.abs(ThreadLocalRandom.current().nextLong()));
    }

    public String generateExtId() {
        /*length should be max 20;*/
        return String.valueOf(BigDecimal.valueOf(Math.abs(ThreadLocalRandom.current().nextLong())));
    }

    public void logAsWarningIfNoAction(Response response) {
        if (noSapActionHasBeenPerformed(response)) {
            LOG.warn("No SAP action has been performed.");
            logAsWarning(response);
        }
    }

    public boolean noSapActionHasBeenPerformed(Response response) {
        if (response.getEntity() instanceof VendorPostingCreateResponse entity) {
            return isSuccess(entity.getRETURNCODE().get(0).getTYPE());
        } else if (response.getEntity() instanceof BusinessPartnerCreateResponse entity) {
            return isSuccess(entity.getRETURNCODE().get(0).getTYPE());
        } else if (response.getEntity() instanceof BusinessPartnerChangeResponse entity) {
            return isSuccess(entity.getRETURNCODE().get(0).getTYPE());
        } else if (response.getEntity() instanceof BusinessPartnerReadResponse entity) {
            return isSuccess(entity.getRETURNCODE().get(0).getTYPE());
        }

        return false;
    }

    private boolean isSuccess(final String rawMessageType) {
        return SapMessageType.parse(rawMessageType).equals(SapMessageType.SUCCESS);
    }

    private void logAsWarning(Response response) {
        String message = "";
        if (response.getEntity() instanceof VendorPostingCreateResponse) {
            message = ((VendorPostingCreateResponse) response.getEntity()).getRETURNCODE().get(0).getMESSAGE();
        } else if (response.getEntity() instanceof BusinessPartnerCreateResponse) {
            message = ((BusinessPartnerCreateResponse) response.getEntity()).getRETURNCODE().get(0).getMESSAGE();
        } else if (response.getEntity() instanceof BusinessPartnerChangeResponse) {
            message = ((BusinessPartnerChangeResponse) response.getEntity()).getRETURNCODE().get(0).getMESSAGE();
        }
        LOG.warn(message);
    }
}
