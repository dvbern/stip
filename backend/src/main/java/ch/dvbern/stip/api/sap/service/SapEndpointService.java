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

import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerChangeRequest;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerChangeResponse;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerCreateRequest;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerCreateResponse;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerReadRequest;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerReadResponse;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerSearchRequest;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerSearchResponse;
import ch.dvbern.stip.api.sap.generated.business_partner.OsBusinessPartnerChangeService;
import ch.dvbern.stip.api.sap.generated.business_partner.OsBusinessPartnerCreateService;
import ch.dvbern.stip.api.sap.generated.business_partner.OsBusinessPartnerReadService;
import ch.dvbern.stip.api.sap.generated.business_partner.OsBusinessPartnerSearchService;
import ch.dvbern.stip.api.sap.generated.import_status.ImportStatusReadRequest;
import ch.dvbern.stip.api.sap.generated.import_status.ImportStatusReadResponse;
import ch.dvbern.stip.api.sap.generated.import_status.OsImportStatusReadService;
import ch.dvbern.stip.api.sap.generated.vendor_posting.OsVendorPostingCreateService;
import ch.dvbern.stip.api.sap.generated.vendor_posting.VendorPostingCreateRequest;
import ch.dvbern.stip.api.sap.generated.vendor_posting.VendorPostingCreateResponse;
import ch.dvbern.stip.api.sap.util.SOAPLoggingHandler;
import ch.dvbern.stip.api.sap.util.SapMapperUtil;
import io.quarkus.arc.profile.UnlessBuildProfile;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.BadRequestException;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.handler.MessageContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Slf4j
@RequestScoped
@UnlessBuildProfile("test")
@RequiredArgsConstructor
public class SapEndpointService {
    private final BusinessPartnerCreateMapper businessPartnerCreateMapper;
    private final BusinessPartnerChangeMapper businessPartnerChangeMapper;
    private final BusinessPartnerReadMapper businessPartnerReadMapper;
    private final BusinessPartnerSearchMapper businessPartnerSearchMapper;
    private final VendorPostingCreateMapper vendorPostingCreateMapper;

    private static final int MAX_LENGTH_REF_DOC_NO = 16;
    private static final long MAX_DELIVERY_ID = 0x1FFFFFFFFFFFFL;

    @Getter
    @ConfigProperty(name = "kstip.sap.system-id")
    BigInteger systemid;

    @ConfigProperty(name = "kstip.sap.auth-header-value")
    String authHeaderValue;

    @ConfigProperty(name = "kstip.sap.connectionTimeout")
    Integer connectionTimeout;

    @ConfigProperty(name = "kstip.sap.receiveTimeout")
    Integer receiveTimeout;

    private void configureLogHandler(BindingProvider port) {
        var handlerChain = port.getBinding().getHandlerChain();
        handlerChain.add(new SOAPLoggingHandler());
        port.getBinding().setHandlerChain(handlerChain);
    }

    private void configureTimeouts(BindingProvider port) {
        // Set timeout until a connection is established
        port.getRequestContext().put("javax.xml.ws.client.connectionTimeout", String.valueOf(connectionTimeout));
        // Set timeout until the response is received
        port.getRequestContext().put("javax.xml.ws.client.receiveTimeout", String.valueOf(receiveTimeout));
    }

    private void configurePortParams(BindingProvider port) {
        configureLogHandler(port);
        configureTimeouts(port);
    }

    private void configureAuthHeader(BindingProvider port) {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Authorization", Collections.singletonList("Basic " + authHeaderValue));
        port.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, headers);
    }

    private void configurePort(BindingProvider port) {
        configureAuthHeader(port);
        configurePortParams(port);
    }

    public static BigDecimal generateDeliveryId(final BigInteger systemid) {
        SecureRandom secureRandom = new SecureRandom();
        final var uniqueId = BigDecimal.valueOf(Math.abs(secureRandom.nextLong() & MAX_DELIVERY_ID)).setScale(0);

        return new BigDecimal(String.format("%d%d", systemid, uniqueId.longValue()));
    }

    public BusinessPartnerCreateResponse createBusinessPartner(
        Fall fall,
        BigDecimal sapDeliveryId
    ) {
        final OsBusinessPartnerCreateService businessPartnerCreateService = new OsBusinessPartnerCreateService();
        final var port = businessPartnerCreateService.getHTTPSPort();
        configurePort((BindingProvider) port);

        final BusinessPartnerCreateRequest businessPartnerCreateRequest =
            businessPartnerCreateMapper.toBusinessPartnerCreateRequest(getSystemid(), sapDeliveryId, fall);
        businessPartnerCreateRequest.getBUSINESSPARTNER().setHEADER(businessPartnerCreateMapper.getHeader());
        return port.osBusinessPartnerCreate(businessPartnerCreateRequest);
    }

    public BusinessPartnerChangeResponse changeBusinessPartner(
        Fall fall,
        BigDecimal sapDeliveryId
    ) {
        final OsBusinessPartnerChangeService businessPartnerChangeService = new OsBusinessPartnerChangeService();
        final var port = businessPartnerChangeService.getHTTPSPort();
        configurePort((BindingProvider) port);

        final BusinessPartnerChangeRequest businessPartnerChangeRequest =
            businessPartnerChangeMapper.toBusinessPartnerChangeRequest(getSystemid(), sapDeliveryId, fall);
        return port.osBusinessPartnerChange(businessPartnerChangeRequest);
    }

    public BusinessPartnerReadResponse readBusinessPartnerByDeliveryId(
        BigDecimal sapDeliveryId
    ) {
        final OsBusinessPartnerReadService businessPartnerReadService = new OsBusinessPartnerReadService();
        final var port = businessPartnerReadService.getHTTPSPort();
        configurePort((BindingProvider) port);

        final BusinessPartnerReadRequest businessPartnerReadRequest =
            businessPartnerReadMapper.toBusinessPartnerReadRequestDeliveryId(getSystemid(), sapDeliveryId);
        return port.osBusinessPartnerRead(businessPartnerReadRequest);
    }

    public BusinessPartnerReadResponse readBusinessPartnerByBusinessPartnerId(
        Integer businessPartnerId
    ) {
        final OsBusinessPartnerReadService businessPartnerReadService = new OsBusinessPartnerReadService();
        final var port = businessPartnerReadService.getHTTPSPort();
        configurePort((BindingProvider) port);

        final BusinessPartnerReadRequest businessPartnerReadRequest =
            businessPartnerReadMapper.toBusinessPartnerReadRequestBusinessPartnerId(getSystemid(), businessPartnerId);
        return port.osBusinessPartnerRead(businessPartnerReadRequest);
    }

    public BusinessPartnerSearchResponse searchBusinessPartner(
        String sozialversicherungsnummer
    ) {
        final OsBusinessPartnerSearchService businessPartnerSearchService = new OsBusinessPartnerSearchService();
        final var port = businessPartnerSearchService.getHTTPSPort();
        configurePort((BindingProvider) port);

        final BusinessPartnerSearchRequest businessPartnerSearchRequest =
            businessPartnerSearchMapper.toBusinessPartnerSearchRequest(getSystemid(), sozialversicherungsnummer);
        return port.osBusinessPartnerSearch(businessPartnerSearchRequest);
    }

    public ImportStatusReadResponse readImportStatus(BigDecimal deliveryid) {
        final OsImportStatusReadService importStatusReadService = new OsImportStatusReadService();
        final var port = importStatusReadService.getHTTPSPort();
        configurePort((BindingProvider) port);

        final ImportStatusReadRequest importStatusReadRequest = new ImportStatusReadRequest();
        importStatusReadRequest.setSENDER(SapMapperUtil.getImportStatusReadSenderParms(getSystemid()));
        importStatusReadRequest.setFILTERPARMS(new ImportStatusReadRequest.FILTERPARMS());
        importStatusReadRequest.getFILTERPARMS().setDELIVERYID(deliveryid.setScale(0));

        return port.osImportStatusRead(importStatusReadRequest);
    }

    public VendorPostingCreateResponse createVendorPosting(
        Fall fall,
        Integer amount,
        BigDecimal sapDeliveryId,
        String qrIbanAddlInfo,
        String refDocNo
    ) {
        final OsVendorPostingCreateService vendorPostingCreateService = new OsVendorPostingCreateService();
        final var port = vendorPostingCreateService.getHTTPSPort();
        configurePort((BindingProvider) port);

        final VendorPostingCreateRequest vendorPostingCreateRequest =
            vendorPostingCreateMapper
                .toVendorPostingCreateRequest(getSystemid(), sapDeliveryId, amount, qrIbanAddlInfo, fall);

        XMLGregorianCalendar docDate;
        XMLGregorianCalendar pstngDate;
        try {
            docDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(LocalDate.now().toString());
            pstngDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(LocalDate.now().toString());
        } catch (DatatypeConfigurationException e) {
            throw new BadRequestException(e);
        }

        if (refDocNo.length() > MAX_LENGTH_REF_DOC_NO) {
            refDocNo = refDocNo.substring(0, MAX_LENGTH_REF_DOC_NO);
        }

        vendorPostingCreateRequest.getVENDORPOSTING()
            .get(0)
            .setHEADER(vendorPostingCreateMapper.toHeader(refDocNo, docDate, pstngDate));

        return port.osVendorPostingCreate(vendorPostingCreateRequest);
    }
}
