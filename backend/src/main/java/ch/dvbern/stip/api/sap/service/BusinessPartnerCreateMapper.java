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

import ch.dvbern.stip.api.auszahlung.entity.Zahlungsverbindung;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerCreateRequest;
import ch.dvbern.stip.api.sap.generated.business_partner.SenderParmsDelivery;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public abstract class BusinessPartnerCreateMapper {
    public BusinessPartnerCreateRequest.BUSINESSPARTNER.HEADER getHeader() {
        final var header = new BusinessPartnerCreateRequest.BUSINESSPARTNER.HEADER();
        header.setPARTNCAT("1");
        return header;
    }

    @Mapping(source = ".", target = "EXTID", qualifiedByName = "getExtId")
    public abstract BusinessPartnerCreateRequest.BUSINESSPARTNER.IDKEYS toIdKeys(Zahlungsverbindung zahlungsverbindung);

    @Named("getExtId")
    public String getExtId(Zahlungsverbindung zahlungsverbindung) {
        return String.valueOf(Math.abs(zahlungsverbindung.getId().getMostSignificantBits()));
    }

    @Mapping(source = "zahlungsverbindung.vorname", target = "FIRSTNAME")
    @Mapping(source = "zahlungsverbindung.nachname", target = "LASTNAME")
    @Mapping(target = "CORRESPONDLANGUAGEISO", constant = "DE")
    public abstract BusinessPartnerCreateRequest.BUSINESSPARTNER.PERSDATA toPersData(
        Zahlungsverbindung zahlungsverbindung
    );

    @Mapping(target = "ADRKIND", constant = "XXDEFAULT")
    @Mapping(source = "zahlungsverbindung.adresse.land", target = "COUNTRY", qualifiedByName = "getLandStringFromLand")
    @Mapping(source = "zahlungsverbindung.adresse.ort", target = "CITY")
    @Mapping(source = "zahlungsverbindung.adresse.coAdresse", target = "CONAME")
    @Mapping(source = "zahlungsverbindung.adresse.strasse", target = "STREET")
    @Mapping(source = "zahlungsverbindung.adresse.hausnummer", target = "HOUSENO")
    @Mapping(source = "zahlungsverbindung.adresse.plz", target = "POSTLCOD1")
    public abstract BusinessPartnerCreateRequest.BUSINESSPARTNER.ADDRESS toAddress(
        Zahlungsverbindung zahlungsverbindung
    );

    @Named("getLandStringFromLand")
    public String getLandStringFromLand(Land land) {
        return land.getDeKurzform();
    }

    @Mapping(source = "zahlungsverbindung.iban", target = "IBAN")
    public abstract BusinessPartnerCreateRequest.BUSINESSPARTNER.PAYMENTDETAIL toPaymentDetails(
        Zahlungsverbindung zahlungsverbindung
    );

    @Mapping(source = "zahlungsverbindung", target = "IDKEYS")
    @Mapping(source = "zahlungsverbindung", target = "PERSDATA")
    @Mapping(source = "zahlungsverbindung", target = "ADDRESS", qualifiedByName = "setAdress")
    @Mapping(source = "zahlungsverbindung", target = "PAYMENTDETAIL", qualifiedByName = "setPaymentDetail")
    public abstract BusinessPartnerCreateRequest.BUSINESSPARTNER toBusinessPartner(
        Zahlungsverbindung zahlungsverbindung
    );

    @Named("setAdress")
    public List<BusinessPartnerCreateRequest.BUSINESSPARTNER.ADDRESS> setAdress(Zahlungsverbindung zahlungsverbindung) {
        return List.of(toAddress(zahlungsverbindung));
    }

    @Named("setPaymentDetail")
    public List<BusinessPartnerCreateRequest.BUSINESSPARTNER.PAYMENTDETAIL> setPaymentDetail(
        Zahlungsverbindung zahlungsverbindung
    ) {
        return List.of(toPaymentDetails(zahlungsverbindung));
    }

    @Named("getSenderParmsDelivery")
    public SenderParmsDelivery getSenderParmsDelivery(
        @Context BigInteger sysid,
        @Context BigDecimal deliveryid,
        Zahlungsverbindung zahlungsverbindung
    ) {
        final SenderParmsDelivery sender = new SenderParmsDelivery();
        sender.setSYSID(sysid);
        sender.setDELIVERYID(deliveryid);
        return sender;
    }

    @Mapping(source = ".", target = "BUSINESSPARTNER")
    @Mapping(source = ".", target = "SENDER", qualifiedByName = "getSenderParmsDelivery")
    public abstract BusinessPartnerCreateRequest toBusinessPartnerCreateRequest(
        @Context BigInteger sysid,
        @Context BigDecimal deliveryid,
        Zahlungsverbindung zahlungsverbindung
    );
}
