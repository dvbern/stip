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

package ch.dvbern.stip.api.sap.util;

import java.math.BigDecimal;
import java.security.SecureRandom;

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
        return BigDecimal.valueOf(Math.abs(generateSecureRandomLong()));
    }

    public String generateExtId() {
        return String.valueOf(BigDecimal.valueOf(Math.abs(generateSecureRandomLong())));
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

    private long generateSecureRandomLong() {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextLong();
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
