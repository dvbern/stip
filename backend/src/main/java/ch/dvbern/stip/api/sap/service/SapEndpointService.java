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
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerChangeRequest;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerChangeResponse;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerCreateRequest;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerCreateResponse;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerReadRequest;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerReadResponse;
import ch.dvbern.stip.api.sap.generated.business_partner.OsBusinessPartnerChangeService;
import ch.dvbern.stip.api.sap.generated.business_partner.OsBusinessPartnerCreateService;
import ch.dvbern.stip.api.sap.generated.business_partner.OsBusinessPartnerReadService;
import ch.dvbern.stip.api.sap.generated.import_status.ImportStatusReadRequest;
import ch.dvbern.stip.api.sap.generated.import_status.ImportStatusReadResponse;
import ch.dvbern.stip.api.sap.generated.import_status.OsImportStatusReadService;
import ch.dvbern.stip.api.sap.generated.vendor_posting.OsVendorPostingCreateService;
import ch.dvbern.stip.api.sap.generated.vendor_posting.VendorPostingCreateRequest;
import ch.dvbern.stip.api.sap.generated.vendor_posting.VendorPostingCreateResponse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.handler.MessageContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Slf4j
@RequestScoped
@RequiredArgsConstructor
public class SapEndpointService {
    private final BusinessPartnerCreateMapper businessPartnerCreateMapper;
    private final BusinessPartnerChangeMapper businessPartnerChangeMapper;
    private final BusinessPartnerReadMapper businessPartnerReadMapper;
    private final VendorPostingCreateMapper vendorPostingCreateMapper;
    private final GeneralMapper generalMapper;

    @ConfigProperty(name = "kstip.sap.system-id")
    BigInteger systemid;

    @ConfigProperty(name = "kstip.sap.auth-header-value")
    String authHeaderValue;

    private void setAuthHeader(BindingProvider port) {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Authorization", Collections.singletonList("Basic " + authHeaderValue));
        port.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, headers);
    }

    public static BigDecimal generateDeliveryId() {
        SecureRandom secureRandom = new SecureRandom();
        return BigDecimal.valueOf(Math.abs(secureRandom.nextLong())).setScale(0);
    }

    public BusinessPartnerCreateResponse createBusinessPartner(Auszahlung auszahlung, BigDecimal sapDeliveryId) {
        final OsBusinessPartnerCreateService businessPartnerCreateService = new OsBusinessPartnerCreateService();
        final var port = businessPartnerCreateService.getHTTPSPort();
        this.setAuthHeader((BindingProvider) port);

        final BusinessPartnerCreateRequest businessPartnerCreateRequest =
            businessPartnerCreateMapper.toBusinessPartnerCreateRequest(systemid, sapDeliveryId, auszahlung);
        businessPartnerCreateRequest.getBUSINESSPARTNER().setHEADER(businessPartnerCreateMapper.getHeader());
        return port.osBusinessPartnerCreate(businessPartnerCreateRequest);
    }

    public BusinessPartnerChangeResponse changeBusinessPartner(Auszahlung auszahlung, BigDecimal sapDeliveryId) {
        final OsBusinessPartnerChangeService businessPartnerChangeService = new OsBusinessPartnerChangeService();
        final var port = businessPartnerChangeService.getHTTPSPort();
        this.setAuthHeader((BindingProvider) port);

        final BusinessPartnerChangeRequest businessPartnerChangeRequest =
            businessPartnerChangeMapper.toBusinessPartnerCreateRequest(systemid, sapDeliveryId, auszahlung);
        businessPartnerChangeRequest.getBUSINESSPARTNER()
            .setHEADER(businessPartnerChangeMapper.getHeader(auszahlung.getSapBusinessPartnerId()));
        return port.osBusinessPartnerChange(businessPartnerChangeRequest);
    }

    public BusinessPartnerReadResponse readBusinessPartner(Auszahlung auszahlung) {
        final OsBusinessPartnerReadService businessPartnerReadService = new OsBusinessPartnerReadService();
        final var port = businessPartnerReadService.getHTTPSPort();
        this.setAuthHeader((BindingProvider) port);

        final BusinessPartnerReadRequest businessPartnerReadRequest =
            businessPartnerReadMapper.toBusinessPartnerReadRequest(systemid, auszahlung);
        return port.osBusinessPartnerRead(businessPartnerReadRequest);
    }

    // public BusinessPartnerSearchResponse searchBusinessPartner(Auszahlung auszahlung) {
    // final OsBusinessPartnerSearchService businessPartnerSearchService = new OsBusinessPartnerSearchService();
    // final var port = businessPartnerSearchService.getHTTPSPort();
    // this.setAuthHeader((BindingProvider) port);
    //
    // final BusinessPartnerSearchRequest businessPartnerSearchRequest = new BusinessPartnerSearchRequest();
    // businessPartnerSearchRequest.setSENDER(businessPartnerReadMapper.getSenderParms(systemid, null));
    //
    // return port.osBusinessPartnerSearch(businessPartnerSearchRequest);
    // }

    public ImportStatusReadResponse readImportStatus(BigDecimal deliveryid) {
        final OsImportStatusReadService importStatusReadService = new OsImportStatusReadService();
        final var port = importStatusReadService.getHTTPSPort();
        this.setAuthHeader((BindingProvider) port);

        final ImportStatusReadRequest importStatusReadRequest = new ImportStatusReadRequest();
        importStatusReadRequest.setSENDER(generalMapper.getSenderParms(systemid));
        importStatusReadRequest.setFILTERPARMS(new ImportStatusReadRequest.FILTERPARMS());
        importStatusReadRequest.getFILTERPARMS().setDELIVERYID(deliveryid.setScale(0));

        return port.osImportStatusRead(importStatusReadRequest);
    }

    public VendorPostingCreateResponse createVendorPosting(
        Auszahlung auszahlung,
        Integer amount,
        BigDecimal sapDeliveryId,
        String qrIbanAddlInfo
    ) {
        final OsVendorPostingCreateService vendorPostingCreateService = new OsVendorPostingCreateService();
        final var port = vendorPostingCreateService.getHTTPSPort();
        this.setAuthHeader((BindingProvider) port);

        final VendorPostingCreateRequest vendorPostingCreateRequest =
            vendorPostingCreateMapper
                .toVendorPostingCreateRequest(systemid, sapDeliveryId, amount, qrIbanAddlInfo, auszahlung);

        XMLGregorianCalendar docDate;
        XMLGregorianCalendar pstngDate;
        try {
            docDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(LocalDate.now().toString());
            pstngDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(LocalDate.now().toString());
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }

        vendorPostingCreateRequest.getVENDORPOSTING()
            .get(0)
            .setHEADER(vendorPostingCreateMapper.toHeader(docDate, pstngDate));

        return port.osVendorPostingCreate(vendorPostingCreateRequest);
    }
}
