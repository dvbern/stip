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

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerChangeRequest;
import ch.dvbern.stip.api.sap.generated.business_partner.SenderParmsDelivery;
import ch.dvbern.stip.api.sap.util.SapMapperUtil;
import ch.dvbern.stip.api.zahlungsverbindung.entity.Zahlungsverbindung;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public abstract class BusinessPartnerChangeMapper {
    @Mapping(source = "businessPartnerId", target = "BPARTNER")
    public abstract BusinessPartnerChangeRequest.BUSINESSPARTNER.HEADER getHeader(Integer businessPartnerId);

    @Mapping(source = ".", target = "EXTID", qualifiedByName = "getExtId")
    @Mapping(source = ".", target = "AHVNR", qualifiedByName = "getAhvNr")
    public abstract BusinessPartnerChangeRequest.BUSINESSPARTNER.IDKEYS toIdKeys(
        Fall fall
    );

    @Named("getExtId")
    public String getExtId(Fall fall) {
        return SapMapperUtil.getExtId(fall);
    }

    @Named("getAhvNr")
    public String getAhvNr(Fall fall) {
        return SapMapperUtil.getAhvNr(fall);
    }

    @Mapping(source = "vorname", target = "FIRSTNAME")
    @Mapping(source = "nachname", target = "LASTNAME")
    @Mapping(source = "nationalitaet.iso2code", target = "NATIONALITYISO")
    @Mapping(source = "geburtsdatum", target = "BIRTHDATE")
    @Mapping(target = "CORRESPONDLANGUAGEISO", constant = "DE")
    public abstract BusinessPartnerChangeRequest.BUSINESSPARTNER.PERSDATA toPersData(
        PersonInAusbildung pia
    );

    @Mapping(target = "ADRKIND", constant = "XXDEFAULT")
    @Mapping(source = "adresse.land", target = "COUNTRY", qualifiedByName = "getLandStringFromLand")
    @Mapping(source = "adresse.ort", target = "CITY")
    @Mapping(source = "adresse.coAdresse", target = "CONAME")
    @Mapping(source = "adresse.strasse", target = "STREET")
    @Mapping(source = "adresse.hausnummer", target = "HOUSENO")
    @Mapping(source = "adresse.plz", target = "POSTLCOD1")
    public abstract BusinessPartnerChangeRequest.BUSINESSPARTNER.ADDRESS toAddress(
        PersonInAusbildung pia
    );

    @Named("getLandStringFromLand")
    public String getLandStringFromLand(Land land) {
        return land.getIso2code();
    }

    @Mapping(target = "BANKID", constant = "0001")
    @Mapping(source = "iban", target = "IBAN")
    @Mapping(source = ".", target = "ACCOUNTHOLDER", qualifiedByName = "getAccountHolder")
    public abstract BusinessPartnerChangeRequest.BUSINESSPARTNER.PAYMENTDETAIL toPaymentDetails(
        Zahlungsverbindung zahlungsverbindung
    );

    @Named("getAccountHolder")
    public String getAccountHolder(Zahlungsverbindung zahlungsverbindung) {
        return SapMapperUtil.getAccountHolder(zahlungsverbindung);
    }

    @Mapping(source = ".", target = "IDKEYS")
    @Mapping(source = "auszahlung.sapBusinessPartnerId", target = "HEADER")
    @Mapping(source = ".", target = "PERSDATA", qualifiedByName = "setPersdata")
    @Mapping(source = ".", target = "ADDRESS", qualifiedByName = "setAdress")
    @Mapping(source = ".", target = "PAYMENTDETAIL", qualifiedByName = "setPaymentDetail")
    public abstract BusinessPartnerChangeRequest.BUSINESSPARTNER toBusinessPartner(
        Fall fall
    );

    @Named("setPersdata")
    public BusinessPartnerChangeRequest.BUSINESSPARTNER.PERSDATA setPersdata(
        Fall fall
    ) {
        return toPersData(SapMapperUtil.getPia(fall));
    }

    @Named("setAdress")
    public List<BusinessPartnerChangeRequest.BUSINESSPARTNER.ADDRESS> setAdress(Fall fall) {
        return List.of(toAddress(SapMapperUtil.getPia(fall)));
    }

    @Named("setPaymentDetail")
    public List<BusinessPartnerChangeRequest.BUSINESSPARTNER.PAYMENTDETAIL> setPaymentDetail(
        Fall fall
    ) {
        return List.of(toPaymentDetails(fall.getRelevantZahlungsverbindung()));
    }

    @Named("getSenderParmsDelivery")
    public SenderParmsDelivery getSenderParmsDelivery(
        @Context BigInteger sysid,
        @Context BigDecimal deliveryid,
        Fall fall
    ) {
        return SapMapperUtil.getBusinessPartnerSenderParmsDelivery(
            sysid,
            deliveryid
        );
    }

    @Mapping(source = ".", target = "BUSINESSPARTNER")
    @Mapping(source = ".", target = "SENDER", qualifiedByName = "getSenderParmsDelivery")
    public abstract BusinessPartnerChangeRequest toBusinessPartnerChangeRequest(
        @Context BigInteger sysid,
        @Context BigDecimal deliveryid,
        Fall fall
    );
}
