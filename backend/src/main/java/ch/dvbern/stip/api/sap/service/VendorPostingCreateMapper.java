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
import javax.xml.datatype.XMLGregorianCalendar;

import ch.dvbern.stip.api.auszahlung.entity.Zahlungsverbindung;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.sap.generated.general.SenderParmsDelivery;
import ch.dvbern.stip.api.sap.generated.vendor_posting.VendorPostingCreateRequest;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public abstract class VendorPostingCreateMapper {
    @Mapping(target = "DOCTYPE", constant = "YK")
    @Mapping(target = "COMPCODE", constant = "4800")
    @Mapping(target = "HEADERTXT", constant = "4890")
    @Mapping(source = "refDocNo", target = "REFDOCNO")
    @Mapping(source = "docdate", target = "DOCDATE")
    @Mapping(source = "pstngdate", target = "PSTNGDATE")
    @Mapping(target = "CURRENCY", constant = "CHF")
    @Mapping(target = "IKSRELEVANT", constant = "false")
    public abstract VendorPostingCreateRequest.VENDORPOSTING.HEADER toHeader(
        String refDocNo,
        XMLGregorianCalendar docdate,
        XMLGregorianCalendar pstngdate
    );

    @Mapping(source = "zahlungsverbindung.sapBusinessPartnerId", target = "VENDORNO")
    @Mapping(source = ".", target = "AMTDOCCUR", qualifiedByName = "getAmtdoccur")
    @Mapping(target = "ZTERM", constant = "ZB04")
    @Mapping(source = ".", target = "ITEMTEXT", qualifiedByName = "getQrIbanAddlInfo")
    public abstract VendorPostingCreateRequest.VENDORPOSTING.VENDOR toVendor(
        @Context Integer amount,
        @Context String qrIbanAddlInfo,
        Zahlungsverbindung zahlungsverbindung
    );

    @Named("getAmtdoccur")
    public BigDecimal getAmtdoccur(
        @Context Integer amount,
        Zahlungsverbindung zahlungsverbindung
    ) {
        return new BigDecimal(amount);
    }

    @Mapping(source = "iban", target = "IBAN")
    public abstract VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL.IBAN toIban(
        Zahlungsverbindung zahlungsverbindung
    );

    @Mapping(target = "QRIBAN", constant = "")
    @Mapping(source = ".", target = "QRIBANADDLINFO", qualifiedByName = "getQrIbanAddlInfo")
    @Mapping(target = "POREFNO", constant = "")
    public abstract VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL.QRIBAN toQrIban(
        @Context String qrIbanAddlInfo,
        Zahlungsverbindung zahlungsverbindung
    );

    @Mapping(source = "zahlungsverbindung", target = "IBAN")
    @Mapping(source = ".", target = "QRIBAN")
    public abstract VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL toPaymentDetail(
        @Context String qrIbanAddlInfo,
        Zahlungsverbindung zahlungsverbindung
    );

    @Mapping(target = "ITEMNOACC", constant = "1")
    @Mapping(target = "GLACCOUNT", constant = "363710000")
    @Mapping(target = "ORDERID", constant = "485100210001")
    @Mapping(target = "TAXCODE", constant = "V0")
    @Mapping(target = "COSTCENTER", constant = "")
    @Mapping(source = ".", target = "ITEMTEXT", qualifiedByName = "getQrIbanAddlInfo")
    @Mapping(target = "KBLPOS", constant = "0")
    @Mapping(target = "REFSETERLK", constant = "false")
    public abstract VendorPostingCreateRequest.VENDORPOSTING.GLACCOUNT toGlAccount(
        @Context String qrIbanAddlInfo,
        Zahlungsverbindung zahlungsverbindung
    );

    @Named("getQrIbanAddlInfo")
    public String getQrIbanAddlInfo(
        @Context String qrIbanAddlInfo,
        Zahlungsverbindung zahlungsverbindung
    ) {
        return qrIbanAddlInfo;
    }

    @Named("setGlAccount")
    public List<VendorPostingCreateRequest.VENDORPOSTING.GLACCOUNT> setGlAccount(
        @Context String qrIbanAddlInfo,
        Zahlungsverbindung zahlungsverbindung
    ) {
        return List.of(toGlAccount(qrIbanAddlInfo, zahlungsverbindung));
    }

    @Named("setPosition")
    public List<VendorPostingCreateRequest.VENDORPOSTING.POSITION> setPosition(
        @Context Integer amount,
        Zahlungsverbindung zahlungsverbindung
    ) {
        return List.of(toPosition(amount));
    }

    @Mapping(target = "ITEMNOACC", constant = "1")
    @Mapping(source = "amount", target = "AMTDOCCUR")
    public abstract VendorPostingCreateRequest.VENDORPOSTING.POSITION toPosition(Integer amount);

    @Mapping(source = ".", target = "VENDOR")
    @Mapping(source = ".", target = "PAYMENTDETAIL")
    @Mapping(source = ".", target = "GLACCOUNT", qualifiedByName = "setGlAccount")
    @Mapping(source = ".", target = "POSITION", qualifiedByName = "setPosition")
    public abstract VendorPostingCreateRequest.VENDORPOSTING toVendorPosting(
        @Context Integer amount,
        @Context String qrIbanAddlInfo,
        Zahlungsverbindung zahlungsverbindung
    );

    @Named("setVendorPosting")
    public List<VendorPostingCreateRequest.VENDORPOSTING> setVendorPosting(
        @Context Integer amount,
        @Context String qrIbanAddlInfo,
        Zahlungsverbindung zahlungsverbindung
    ) {
        return List.of(toVendorPosting(amount, qrIbanAddlInfo, zahlungsverbindung));
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

    @Mapping(source = ".", target = "VENDORPOSTING", qualifiedByName = "setVendorPosting")
    @Mapping(source = ".", target = "SENDER", qualifiedByName = "getSenderParmsDelivery")
    public abstract VendorPostingCreateRequest toVendorPostingCreateRequest(
        @Context BigInteger sysid,
        @Context BigDecimal deliveryid,
        @Context Integer amount,
        @Context String qrIbanAddlInfo,
        Zahlungsverbindung zahlungsverbindung
    );
}
