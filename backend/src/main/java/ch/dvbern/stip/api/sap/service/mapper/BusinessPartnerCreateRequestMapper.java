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

import java.math.BigDecimal;
import java.math.BigInteger;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.sap.generated.businesspartner.create.BusinessPartnerCreateRequest;
import ch.dvbern.stip.api.sap.generated.businesspartner.create.SenderParmsDelivery;

public class BusinessPartnerCreateRequestMapper {
    BusinessParnterCreateMapper businessPartnerMapper = new BusinessParnterCreateMapper();

    public BusinessPartnerCreateRequest toBusinessPartnerCreateRequest(
        Auszahlung auszahlung,
        BigInteger sysid,
        BigDecimal deliveryId
    ) {
        BusinessPartnerCreateRequest businessPartnerCreateRequest = new BusinessPartnerCreateRequest();
        BusinessPartnerCreateRequest.BUSINESSPARTNER businesspartner =
            businessPartnerMapper.toBusniessPartner(auszahlung);
        businessPartnerCreateRequest.setBUSINESSPARTNER(businesspartner);
        SenderParmsDelivery senderParmsDelivery = new SenderParmsDelivery();
        senderParmsDelivery.setSYSID(sysid);
        senderParmsDelivery.setDELIVERYID(deliveryId);
        businessPartnerCreateRequest.setSENDER(senderParmsDelivery);
        return businessPartnerCreateRequest;
    }

}
