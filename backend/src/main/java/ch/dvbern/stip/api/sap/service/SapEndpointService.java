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
import java.time.LocalDate;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.sap.generated.businesspartner.change.BusinessPartnerChangeRequest;
import ch.dvbern.stip.api.sap.generated.businesspartner.change.BusinessPartnerChangeResponse;
import ch.dvbern.stip.api.sap.generated.businesspartner.create.BusinessPartnerCreateRequest;
import ch.dvbern.stip.api.sap.generated.businesspartner.create.BusinessPartnerCreateResponse;
import ch.dvbern.stip.api.sap.generated.businesspartner.read.BusinessPartnerReadRequest;
import ch.dvbern.stip.api.sap.generated.businesspartner.read.BusinessPartnerReadResponse;
import ch.dvbern.stip.api.sap.generated.importstatus.ImportStatusReadRequest;
import ch.dvbern.stip.api.sap.generated.importstatus.ImportStatusReadResponse;
import ch.dvbern.stip.api.sap.generated.importstatus.SenderParms;
import ch.dvbern.stip.api.sap.generated.vendorposting.VendorPostingCreateRequest;
import ch.dvbern.stip.api.sap.generated.vendorposting.VendorPostingCreateResponse;
import ch.dvbern.stip.api.sap.service.endpoints.clients.BusinessPartnerChangeClient;
import ch.dvbern.stip.api.sap.service.endpoints.clients.BusinessPartnerCreateClient;
import ch.dvbern.stip.api.sap.service.endpoints.clients.BusinessPartnerReadClient;
import ch.dvbern.stip.api.sap.service.endpoints.clients.ImportStatusReadClient;
import ch.dvbern.stip.api.sap.service.endpoints.clients.VendorPostingCreateClient;
import ch.dvbern.stip.api.sap.service.endpoints.util.SapEndpointName;
import ch.dvbern.stip.api.sap.service.endpoints.util.SoapUtils;
import ch.dvbern.stip.api.sap.service.mapper.BusinessPartnerChangeRequestMapper;
import ch.dvbern.stip.api.sap.service.mapper.BusinessPartnerCreateRequestMapper;
import ch.dvbern.stip.api.sap.service.mapper.BusniessPartnerReadRequestMapper;
import ch.dvbern.stip.api.sap.service.mapper.VendorPostingCreateRequestMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@RequiredArgsConstructor
@ApplicationScoped
public class SapEndpointService {

    @Inject
    ConfigService configService;

    @RestClient
    ImportStatusReadClient importStatusReadClient;

    @RestClient
    BusinessPartnerCreateClient businessPartnerCreateClient;

    @RestClient
    BusinessPartnerChangeClient businessPartnerChangeClient;

    @RestClient
    VendorPostingCreateClient vendorPostingCreateClient;

    @RestClient
    BusinessPartnerReadClient businessPartnerReadClient;

    private ImportStatusReadResponse getAndParseGetSAPImportStatusResponse(BigDecimal deliveryId) {
        var request = new ImportStatusReadRequest();
        var senderParms = new SenderParms();
        senderParms.setSYSID(BigInteger.valueOf(configService.getSystemid()));
        request.setSENDER(senderParms);
        var filterparms = new ImportStatusReadRequest.FILTERPARMS();
        filterparms.setDELIVERYID(deliveryId);
        request.setFILTERPARMS(filterparms);

        final var xmlRequest =
            SoapUtils.buildXmlRequest(request, ImportStatusReadRequest.class, SapEndpointName.IMPORT_STATUS);
        final var xmlResponse = importStatusReadClient.getImportStatus(xmlRequest);
        return SoapUtils.parseSoapResponse(xmlResponse, ImportStatusReadResponse.class);
    }

    public Response getImportStatus(@Valid @Positive @NotNull BigDecimal deliveryId) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(getAndParseGetSAPImportStatusResponse(deliveryId)).build();

        } catch (WebApplicationException webApplicationException) {
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        }
    }

    public Response readBusniessPartner(String extId) {
        final var businessPartnerReadMapper = new BusniessPartnerReadRequestMapper();
        var request = businessPartnerReadMapper
            .toBusinessPartnerReadRequest(extId, BigInteger.valueOf(configService.getSystemid()));
        request.getSENDER().setSYSID(BigInteger.valueOf(configService.getSystemid()));

        String xmlRequest = null;
        xmlRequest =
            SoapUtils.buildXmlRequest(request, BusinessPartnerReadRequest.class, SapEndpointName.BUSINESPARTNER);
        String xmlResponse = null;

        try {
            xmlResponse = businessPartnerReadClient.readBusinessPartner(
                xmlRequest
            );
        } catch (WebApplicationException webApplicationException) {
            return Response.status(HttpStatus.SC_BAD_REQUEST).entity(webApplicationException).build();
        }
        final var result = SoapUtils.parseSoapResponse(xmlResponse, BusinessPartnerReadResponse.class);
        return Response.status(HttpStatus.SC_OK).entity(result).build();
    }

    /*
     * Note: specific values are still hardcoded or commented - these have to be discussed in the near future
     */

    /**
     * Important: it can take up to 48 hours until a newly created user will be set active in SAP!
     */
    public Response createBusinessPartner(@Valid Auszahlung auszahlung, String extId, BigDecimal deliveryId) {
        final var businessPartnerCreateRequestMapper = new BusinessPartnerCreateRequestMapper();
        var request = businessPartnerCreateRequestMapper
            .toBusinessPartnerCreateRequest(auszahlung, BigInteger.valueOf(configService.getSystemid()), deliveryId);
        request.getSENDER().setSYSID(BigInteger.valueOf(configService.getSystemid()));
        request.getSENDER().setDELIVERYID(deliveryId);
        request.getBUSINESSPARTNER().setIDKEYS(new BusinessPartnerCreateRequest.BUSINESSPARTNER.IDKEYS());
        request.getBUSINESSPARTNER().getIDKEYS().setEXTID(String.valueOf(extId));
        request.getBUSINESSPARTNER().setHEADER(new BusinessPartnerCreateRequest.BUSINESSPARTNER.HEADER());
        request.getBUSINESSPARTNER().getHEADER().setPARTNCAT("1"); // todo KSTIP-1229: set correct category
        request.getBUSINESSPARTNER().getPERSDATA().setCORRESPONDLANGUAGEISO("DE"); // todo: set correct language iso
        request.getBUSINESSPARTNER().getORGDATA().setLANGUISO("DE");// todo KSTIP-1229: set correct language iso
        request.getBUSINESSPARTNER().getORGDATA().setNAME1("");// todo KSTIP-1229: set correct name 1
        request.getBUSINESSPARTNER().getORGDATA().setNAME2("");// todo KSTIP-1229: set correct name 2
        request.getBUSINESSPARTNER().getORGDATA().setNAME3("");// todo KSTIP-1229: set correct name 3
        request.getBUSINESSPARTNER().getORGDATA().setNAME4("");// todo KSTIP-1229: set correct name 4
        request.getBUSINESSPARTNER().getADDRESS().get(0).setADRKIND("XXDEFAULT");
        request.getBUSINESSPARTNER().getADDRESS().get(0).setCOUNTRY("CH");// todo KSTIP-1229: set iso country (max 3
                                                                          // instead of "Schweiz"

        String xmlRequest = null;
        xmlRequest =
            SoapUtils.buildXmlRequest(request, BusinessPartnerCreateRequest.class, SapEndpointName.BUSINESPARTNER);

        String xmlResponse = null;
        try {
            xmlResponse = businessPartnerCreateClient.createBusinessPartner(
                xmlRequest
            );
        } catch (WebApplicationException webApplicationException) {
            return Response.status(HttpStatus.SC_BAD_REQUEST).entity(webApplicationException).build();
        }

        final var result = SoapUtils.parseSoapResponse(xmlResponse, BusinessPartnerCreateResponse.class);
        return Response.status(HttpStatus.SC_OK).entity(result).build();

    }

    /*
     * Note: specific values are still hardcoded or commented - these have to be discussed in the near future
     */
    public Response changeBusinessPartner(
        @Valid Auszahlung auszahlung,
        Integer businessPartnerId,
        BigDecimal deliveryId
    ) {
        final var mapper = new BusinessPartnerChangeRequestMapper();
        var request = mapper
            .toBusinessPartnerChangeRequest(auszahlung, BigInteger.valueOf(configService.getSystemid()), deliveryId);
        request.getBUSINESSPARTNER().setHEADER(new BusinessPartnerChangeRequest.BUSINESSPARTNER.HEADER());
        request.getSENDER().setDELIVERYID(deliveryId);
        request.getSENDER().setSYSID(BigInteger.valueOf(configService.getSystemid()));
        request.getBUSINESSPARTNER().getHEADER().setBPARTNER(businessPartnerId);

        request.getBUSINESSPARTNER().setIDKEYS(new BusinessPartnerChangeRequest.BUSINESSPARTNER.IDKEYS());

        // request.getBUSINESSPARTNER().setHEADER(new BusinessPartnerChangeRequest.BUSINESSPARTNER.HEADER());
        // request.getBUSINESSPARTNER().getHEADER().setBPARTNER("1"); //todo KSTIP-1229: set correct category
        request.getBUSINESSPARTNER().getPERSDATA().setCORRESPONDLANGUAGEISO("DE"); // todo KSTIP-1229: set correct
                                                                                   // language iso
        request.getBUSINESSPARTNER().getORGDATA().setLANGUISO("DE");// todo KSTIP-1229: set correct language iso
        request.getBUSINESSPARTNER().getORGDATA().setNAME1("");// todo KSTIP-1229: set correct name 1
        request.getBUSINESSPARTNER().getORGDATA().setNAME2("");// todo KSTIP-1229: set correct name 2
        request.getBUSINESSPARTNER().getORGDATA().setNAME3("");// todo KSTIP-1229: set correct name 3
        request.getBUSINESSPARTNER().getORGDATA().setNAME4("");// todo KSTIP-1229: set correct name 4
        request.getBUSINESSPARTNER().getADDRESS().get(0).setADRKIND("XXDEFAULT");
        request.getBUSINESSPARTNER().getADDRESS().get(0).setCOUNTRY("CH");// todo KSTIP-1229: set iso country (max 3
                                                                          // instead of "Schweiz"

        String xmlRequest = null;
        xmlRequest =
            SoapUtils.buildXmlRequest(request, BusinessPartnerChangeRequest.class, SapEndpointName.BUSINESPARTNER);
        String xmlResponse = null;
        try {
            xmlResponse = businessPartnerChangeClient.changeBusinessPartner(xmlRequest);
        } catch (WebApplicationException webApplicationException) {
            return Response.status(HttpStatus.SC_BAD_REQUEST).entity(webApplicationException).build();
        }

        final var result = SoapUtils.parseSoapResponse(xmlResponse, BusinessPartnerChangeResponse.class);
        return Response.status(HttpStatus.SC_OK).entity(result).build();
    }

    /*
     * Note: specific values are still hardcoded or commented - these have to be discussed in the near future
     */
    public Response createVendorPosting(
        @Valid Auszahlung auszahlung,
        Integer businessPartnerId,
        BigDecimal deliveryId
    ) {
        final var mapper = new VendorPostingCreateRequestMapper();
        var request = mapper
            .toVendorPostingCreateRequest(auszahlung, BigInteger.valueOf(configService.getSystemid()), deliveryId);
        request.getSENDER().setSYSID(BigInteger.valueOf(configService.getSystemid()));
        request.getSENDER().setDELIVERYID(deliveryId);

        request.getVENDORPOSTING().forEach(vendorposting -> {
            vendorposting.getVENDOR().setVENDORNO(String.valueOf(businessPartnerId));
            vendorposting.getVENDOR().setAMTDOCCUR(BigDecimal.valueOf(7162.0));
            vendorposting.getVENDOR().setZTERM("ZB04");

            vendorposting.getGLACCOUNT().add(new VendorPostingCreateRequest.VENDORPOSTING.GLACCOUNT());
            vendorposting.getGLACCOUNT().get(0).setGLACCOUNT("363710000");
            vendorposting.getGLACCOUNT().get(0).setITEMNOACC(BigDecimal.valueOf(1));
            vendorposting.getPOSITION().add(new VendorPostingCreateRequest.VENDORPOSTING.POSITION());
            vendorposting.getPOSITION().get(0).setITEMNOACC(BigDecimal.valueOf(1));
            vendorposting.getPOSITION().get(0).setAMTDOCCUR(BigDecimal.valueOf(17779.0));

            vendorposting.setHEADER(new VendorPostingCreateRequest.VENDORPOSTING.HEADER());
            vendorposting.getHEADER().setDOCTYPE("YK");
            vendorposting.getHEADER().setCOMPCODE("4800");
            vendorposting.getHEADER().setHEADERTXT("4890");
            vendorposting.getHEADER().setREFDOCNO("0001544323");

            XMLGregorianCalendar date;
            try {
                LocalDate localDate = LocalDate.now();
                date = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendarDate(
                        localDate.getYear(),
                        localDate.getMonthValue(),
                        localDate.getDayOfMonth(),
                        DatatypeConstants.FIELD_UNDEFINED
                    );
            } catch (DatatypeConfigurationException e) {
                throw new RuntimeException(e);
            }
            vendorposting.getHEADER().setDOCDATE(date);
            vendorposting.getHEADER().setPSTNGDATE(date);
            vendorposting.getHEADER().setCURRENCY("CHF");
        });

        String xmlRequest = null;
        xmlRequest =
            SoapUtils.buildXmlRequest(request, VendorPostingCreateRequest.class, SapEndpointName.VENDORPOSTING);
        String xmlResponse = null;

        try {
            xmlResponse = vendorPostingCreateClient.createVendorPosting(xmlRequest);
        } catch (WebApplicationException webApplicationException) {
            return Response.status(HttpStatus.SC_BAD_REQUEST).entity(webApplicationException).build();
        }

        final var result = SoapUtils.parseSoapResponse(xmlResponse, VendorPostingCreateResponse.class);
        return Response.status(HttpStatus.SC_OK).entity(result).build();
    }

}
