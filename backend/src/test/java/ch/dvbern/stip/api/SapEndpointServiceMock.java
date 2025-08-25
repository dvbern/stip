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

package ch.dvbern.stip.api;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import ch.dvbern.stip.api.buchhaltung.type.SapStatus;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerChangeResponse;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerCreateResponse;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerHEADER;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerReadResponse;
import ch.dvbern.stip.api.sap.generated.business_partner.ReturnCode;
import ch.dvbern.stip.api.sap.generated.general.ReturnCodeID;
import ch.dvbern.stip.api.sap.generated.import_status.ImportStatusReadResponse;
import ch.dvbern.stip.api.sap.generated.import_status.ImportStatusReadResponse.DELIVERY;
import ch.dvbern.stip.api.sap.generated.vendor_posting.VendorPostingCreateResponse;
import ch.dvbern.stip.api.sap.service.BusinessPartnerChangeMapper;
import ch.dvbern.stip.api.sap.service.BusinessPartnerCreateMapper;
import ch.dvbern.stip.api.sap.service.BusinessPartnerReadMapper;
import ch.dvbern.stip.api.sap.service.GeneralMapper;
import ch.dvbern.stip.api.sap.service.SapEndpointService;
import ch.dvbern.stip.api.sap.service.VendorPostingCreateMapper;
import ch.dvbern.stip.api.zahlungsverbindung.entity.Zahlungsverbindung;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.RequestScoped;

@Mock
@RequestScoped
public class SapEndpointServiceMock extends SapEndpointService {
    public static final String SUCCESS_STRING = "S";
    public static final String WARNING_STRING = "W";
    public static final String ERROR_STRING = "E";
    public static final String INFO_STRING = "I";

    BusinessPartnerCreateResponse businessPartnerCreateResponse = null;
    BusinessPartnerChangeResponse businessPartnerChangeResponse = null;
    BusinessPartnerReadResponse businessPartnerReadResponse = null;
    ImportStatusReadResponse importStatusReadResponse = null;
    VendorPostingCreateResponse vendorPostingCreateResponse = null;

    Random random = new Random();

    public SapEndpointServiceMock() {
        super(
            null,
            null,
            null,
            null,
            null
        );
        setBusinessPartnerCreateResponse(SUCCESS_STRING);
        setBusinessPartnerChangeResponse(SUCCESS_STRING);
        setBusinessPartnerReadResponse(SUCCESS_STRING, random.nextInt());
        setImportStatusReadResponse(SUCCESS_STRING, SapStatus.SUCCESS);
        setVendorPostingCreateResponse(SUCCESS_STRING);
    }

    public SapEndpointServiceMock(
    BusinessPartnerCreateMapper businessPartnerCreateMapper,
    BusinessPartnerChangeMapper businessPartnerChangeMapper,
    BusinessPartnerReadMapper businessPartnerReadMapper,
    VendorPostingCreateMapper vendorPostingCreateMapper,
    GeneralMapper generalMapper
    ) {
        super(
            businessPartnerCreateMapper,
            businessPartnerChangeMapper,
            businessPartnerReadMapper,
            vendorPostingCreateMapper,
            generalMapper
        );
        setBusinessPartnerCreateResponse(SUCCESS_STRING);
        setBusinessPartnerChangeResponse(SUCCESS_STRING);
        setBusinessPartnerReadResponse(SUCCESS_STRING, random.nextInt());
        setImportStatusReadResponse(SUCCESS_STRING, SapStatus.SUCCESS);
        setVendorPostingCreateResponse(SUCCESS_STRING);
    }

    public void setBusinessPartnerCreateResponse(final String returnCodeString) {
        var returnCodes = new ArrayList<ReturnCode>();
        var returnCode = new ReturnCode();
        returnCode.setTYPE(returnCodeString);
        returnCodes.add(returnCode);
        BusinessPartnerCreateResponse createResponse = new BusinessPartnerCreateResponse();
        createResponse.setRETURNCODE(returnCodes);
        this.businessPartnerCreateResponse = createResponse;
    }

    public void setBusinessPartnerChangeResponse(final String returnCodeString) {
        var returnCodes = new ArrayList<ReturnCode>();
        var returnCode = new ReturnCode();
        returnCode.setTYPE(returnCodeString);
        returnCodes.add(returnCode);
        BusinessPartnerChangeResponse changeResponse = new BusinessPartnerChangeResponse();
        changeResponse.setRETURNCODE(returnCodes);
        this.businessPartnerChangeResponse = changeResponse;
    }

    public void setBusinessPartnerReadResponse(final String returnCodeString, final Integer businessPartnerId) {
        var returnCodes = new ArrayList<ReturnCode>();
        var returnCode = new ReturnCode();
        returnCode.setTYPE(returnCodeString);
        returnCodes.add(returnCode);
        BusinessPartnerReadResponse readResponse = new BusinessPartnerReadResponse();
        readResponse.setRETURNCODE(returnCodes);
        var bpHeader = new BusinessPartnerHEADER();
        bpHeader.setBPARTNER(businessPartnerId.toString());
        var businessPartner = new BusinessPartnerReadResponse.BUSINESSPARTNER();
        businessPartner.setHEADER(bpHeader);
        readResponse.setBUSINESSPARTNER(businessPartner);
        this.businessPartnerReadResponse = readResponse;
    }

    public void setImportStatusReadResponse(final String returnCodeString, final SapStatus status) {
        var returnCodes = new ArrayList<ReturnCodeID>();
        var returnCode = new ReturnCodeID();
        returnCode.setTYPE(returnCodeString);
        returnCodes.add(returnCode);
        ImportStatusReadResponse readResponse = new ImportStatusReadResponse();
        readResponse.setRETURNCODE(returnCodes);
        var deliverys = new ArrayList<DELIVERY>();
        var delivery = new DELIVERY();
        delivery.setSTATUS(BigInteger.valueOf(status.ordinal()));
        deliverys.add(delivery);
        readResponse.setDELIVERY(deliverys);
        this.importStatusReadResponse = readResponse;
    }

    public void setVendorPostingCreateResponse(final String returnCodeString) {
        var returnCodes = new ArrayList<ch.dvbern.stip.api.sap.generated.general.ReturnCode>();
        var returnCode = new ch.dvbern.stip.api.sap.generated.general.ReturnCode();
        returnCode.setTYPE(returnCodeString);
        returnCodes.add(returnCode);
        VendorPostingCreateResponse postingCreateResponse = new VendorPostingCreateResponse();
        postingCreateResponse.setRETURNCODE(returnCodes);
        this.vendorPostingCreateResponse = postingCreateResponse;
    }

    @Override
    public BusinessPartnerCreateResponse createBusinessPartner(
        Zahlungsverbindung zahlungsverbindung,
        BigDecimal sapDeliveryId
    ) {
        return businessPartnerCreateResponse;
    }

    @Override
    public BusinessPartnerChangeResponse changeBusinessPartner(
        Zahlungsverbindung zahlungsverbindung,
        BigDecimal sapDeliveryId
    ) {
        return businessPartnerChangeResponse;
    }

    @Override
    public BusinessPartnerReadResponse readBusinessPartner(
        Zahlungsverbindung zahlungsverbindung,
        BigDecimal sapDeliveryId
    ) {
        return businessPartnerReadResponse;
    }

    @Override
    public ImportStatusReadResponse readImportStatus(BigDecimal deliveryid) {
        return importStatusReadResponse;
    }

    @Override
    public VendorPostingCreateResponse createVendorPosting(
        Zahlungsverbindung zahlungsverbindung,
        Integer amount,
        BigDecimal sapDeliveryId,
        String qrIbanAddlInfo,
        String refDocNo
    ) {
        return vendorPostingCreateResponse;
    }
}
