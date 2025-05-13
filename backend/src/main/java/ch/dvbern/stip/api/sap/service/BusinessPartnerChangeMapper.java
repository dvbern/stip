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
import java.util.List;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerChangeRequest;
import ch.dvbern.stip.api.sap.generated.business_partner.SenderParmsDelivery;
import ch.dvbern.stip.api.stammdaten.type.Land;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public abstract class BusinessPartnerChangeMapper {
    @Mapping(source = "businessPartnerId", target = "BPARTNER")
    public abstract BusinessPartnerChangeRequest.BUSINESSPARTNER.HEADER getHeader(Integer businessPartnerId);

    @Mapping(target = "EXTID", expression = "java( String.valueOf(auszahlung.getId().getMostSignificantBits()))")
    public abstract BusinessPartnerChangeRequest.BUSINESSPARTNER.IDKEYS toIdKeys(Auszahlung auszahlung);

    @Mapping(source = "vorname", target = "FIRSTNAME")
    @Mapping(source = "nachname", target = "LASTNAME")
    @Mapping(target = "CORRESPONDLANGUAGEISO", constant = "DE")
    public abstract BusinessPartnerChangeRequest.BUSINESSPARTNER.PERSDATA toPersData(Auszahlung auszahlung);

    @Mapping(target = "ADRKIND", constant = "XXDEFAULT")
    @Mapping(source = "adresse.land", target = "COUNTRY", qualifiedByName = "getLandStringFromLand")
    @Mapping(source = "adresse.ort", target = "CITY")
    @Mapping(source = "adresse.coAdresse", target = "CONAME")
    @Mapping(source = "adresse.strasse", target = "STREET")
    @Mapping(source = "adresse.hausnummer", target = "HOUSENO")
    @Mapping(source = "adresse.plz", target = "POSTLCOD1")
    public abstract BusinessPartnerChangeRequest.BUSINESSPARTNER.ADDRESS toAddress(Auszahlung auszahlung);

    @Named("getLandStringFromLand")
    public String getLandStringFromLand(Land land) {
        return land.name();
    }

    @Mapping(target = "BANKID", constant = "0001")
    @Mapping(source = "iban", target = "IBAN")
    public abstract BusinessPartnerChangeRequest.BUSINESSPARTNER.PAYMENTDETAIL toPaymentDetails(Auszahlung auszahlung);

    @Mapping(source = "auszahlung", target = "IDKEYS")
    @Mapping(source = "auszahlung", target = "PERSDATA")
    @Mapping(source = "auszahlung", target = "ADDRESS", qualifiedByName = "setAdress")
    @Mapping(source = "auszahlung", target = "PAYMENTDETAIL", qualifiedByName = "setPaymentDetail")
    public abstract BusinessPartnerChangeRequest.BUSINESSPARTNER toBusinessPartner(
        Auszahlung auszahlung
    );

    @Named("setAdress")
    public List<BusinessPartnerChangeRequest.BUSINESSPARTNER.ADDRESS> setAdress(Auszahlung auszahlung) {
        return List.of(toAddress(auszahlung));
    }

    @Named("setPaymentDetail")
    public List<BusinessPartnerChangeRequest.BUSINESSPARTNER.PAYMENTDETAIL> setPaymentDetail(Auszahlung auszahlung) {
        return List.of(toPaymentDetails(auszahlung));
    }

    @Named("getSenderParmsDelivery")
    public SenderParmsDelivery getSenderParmsDelivery(
        @Context BigInteger sysid,
        @Context BigDecimal deliveryid,
        Auszahlung auszahlung
    ) {
        final SenderParmsDelivery sender = new SenderParmsDelivery();
        sender.setSYSID(sysid);
        sender.setDELIVERYID(deliveryid);
        return sender;
    }

    @Mapping(source = ".", target = "BUSINESSPARTNER")
    @Mapping(source = ".", target = "SENDER", qualifiedByName = "getSenderParmsDelivery")
    public abstract BusinessPartnerChangeRequest toBusinessPartnerCreateRequest(
        @Context BigInteger sysid,
        @Context BigDecimal deliveryid,
        Auszahlung auszahlung
    );
}
