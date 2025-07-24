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
import java.math.BigInteger;

import ch.dvbern.stip.api.auszahlung.entity.Zahlungsverbindung;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerReadRequest;
import ch.dvbern.stip.api.sap.generated.business_partner.SenderParms;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public abstract class BusinessPartnerReadMapper {
    // @Mapping(source = ".", target = "EXTID", qualifiedByName = "getExtId")
    // @Mapping(source = "sapBusinessPartnerId", target = "BPARTNER")
    @Mapping(source = ".", target = "DELIVERYID", qualifiedByName = "getDeliveryId")
    public abstract BusinessPartnerReadRequest.FILTERPARMS getFilterParms(
        @Context BigDecimal deliveryid,
        Zahlungsverbindung zahlungsverbindung
    );

    @Named("getDeliveryId")
    public String getDeliveryId(@Context BigDecimal deliveryid, Zahlungsverbindung zahlungsverbindung) {
        return String.valueOf(Math.abs(deliveryid.longValue()));
    }

    @Named("getSenderParms")
    public SenderParms getSenderParms(
        @Context BigInteger sysid,
        Zahlungsverbindung zahlungsverbindung
    ) {
        final SenderParms sender = new SenderParms();
        sender.setSYSID(sysid);
        return sender;
    }

    @Mapping(source = ".", target = "FILTERPARMS")
    @Mapping(source = ".", target = "SENDER", qualifiedByName = "getSenderParms")
    public abstract BusinessPartnerReadRequest toBusinessPartnerReadRequest(
        @Context BigInteger sysid,
        @Context BigDecimal deliveryid,
        Zahlungsverbindung zahlungsverbindung
    );

}
