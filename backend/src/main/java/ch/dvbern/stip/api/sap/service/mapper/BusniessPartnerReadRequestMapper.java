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

package ch.dvbern.stip.api.sap.service.mapper;

import java.math.BigInteger;

import ch.dvbern.stip.api.sap.generated.businesspartner.read.BusinessPartnerReadRequest;
import ch.dvbern.stip.api.sap.generated.businesspartner.read.SenderParms;

public class BusniessPartnerReadRequestMapper {
    private BusinessPartnerReadFilterParamsMapper businessPartnerReadFilterParamsMapper =
        new BusinessPartnerReadFilterParamsMapper();

    public BusinessPartnerReadRequest toBusinessPartnerReadRequest(String extId, BigInteger sysid) {
        BusinessPartnerReadRequest businessPartnerReadRequest = new BusinessPartnerReadRequest();
        SenderParms senderParams = new SenderParms();
        senderParams.setSYSID(sysid);
        businessPartnerReadRequest.setSENDER(senderParams);
        businessPartnerReadRequest.setFILTERPARMS(businessPartnerReadFilterParamsMapper.toFilterParams(extId));
        return businessPartnerReadRequest;
    }
}
