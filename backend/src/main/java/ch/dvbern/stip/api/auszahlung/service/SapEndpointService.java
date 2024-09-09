package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.change.BusinessPartnerChangeRequest;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.change.BusinessPartnerChangeRequestMapper;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.change.BusinessPartnerChangeResponse;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.create.BusinessPartnerCreateRequest;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.create.BusinessPartnerCreateRequestMapper;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.create.BusinessPartnerCreateResponse;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.read.BusinessPartnerReadRequest;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.read.BusinessPartnerReadResponse;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.read.BusniessPartnerReadRequestMapper;
import ch.dvbern.stip.api.auszahlung.sap.importstatus.ImportStatusReadRequest;
import ch.dvbern.stip.api.auszahlung.sap.importstatus.ImportStatusReadResponse;
import ch.dvbern.stip.api.auszahlung.sap.importstatus.SenderParms;
import ch.dvbern.stip.api.auszahlung.sap.util.SapEndpointName;
import ch.dvbern.stip.api.auszahlung.sap.util.SoapUtils;
import ch.dvbern.stip.api.auszahlung.sap.vendorposting.VendorPostingCreateRequest;
import ch.dvbern.stip.api.auszahlung.sap.vendorposting.VendorPostingCreateRequestMapper;
import ch.dvbern.stip.api.auszahlung.sap.vendorposting.VendorPostingCreateResponse;
import ch.dvbern.stip.api.config.service.ConfigService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.soap.SOAPException;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@RequiredArgsConstructor
@ApplicationScoped
public class SapEndpointService {

    private final AuszahlungMapper auszahlungMapper;

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


    private ImportStatusReadResponse  getAndParseGetSAPImportStatusResponse(BigDecimal deliveryId) throws JAXBException, SOAPException, IOException {
        var request = new ImportStatusReadRequest();
        var senderParms = new SenderParms();
        senderParms.setSYSID(BigInteger.valueOf(configService.getSystemid()));
        request.setSENDER(senderParms);
        var filterparms = new ImportStatusReadRequest.FILTERPARMS();
        filterparms.setDELIVERYID(deliveryId);
        request.setFILTERPARMS(filterparms);

        final var xmlRequest = SoapUtils.buildXmlRequest(request,ImportStatusReadRequest.class, SapEndpointName.IMPORT_STATUS);
        final var xmlResponse = importStatusReadClient.getImportStatus(xmlRequest);
        return SoapUtils.parseSoapResponse(xmlResponse, ImportStatusReadResponse.class);
    }

    public Response getImportStatus(@Valid @Positive @NotNull BigDecimal deliveryId){
        try{
            return Response.status(HttpStatus.SC_OK).entity(getAndParseGetSAPImportStatusResponse(deliveryId)).build();

        }
        catch(WebApplicationException | JAXBException | SOAPException | IOException ex){
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        }
    }

    public Response readBusniessPartner(String extId){
        try{
            final var businessPartnerReadMapper = new BusniessPartnerReadRequestMapper();
            var request = businessPartnerReadMapper.toBusinessPartnerReadRequest(extId,BigInteger.valueOf(configService.getSystemid()));
            request.getSENDER().setSYSID(BigInteger.valueOf(configService.getSystemid()));

            final var xmlRequest = SoapUtils.buildXmlRequest(request, BusinessPartnerReadRequest.class, SapEndpointName.BUSINESPARTNER);
            final var response = businessPartnerReadClient.readBusinessPartner(
                xmlRequest);
            final var result = SoapUtils.parseSoapResponse(response, BusinessPartnerReadResponse.class);
            return Response.status(HttpStatus.SC_OK).entity(result).build();
        }
        catch(WebApplicationException | JAXBException | SOAPException | IOException ex){
            return Response.status(HttpStatus.SC_BAD_REQUEST).entity(ex).build();
        }
    }

    /*
    Note: specific values are still hardcoded or commented - these have to be discussed in the near future
 */
    public Response createBusinessPartner(@Valid Auszahlung auszahlung,String extId, BigDecimal deliveryId){
        try{
            final var businessPartnerCreateRequestMapper = new BusinessPartnerCreateRequestMapper();
            var request = businessPartnerCreateRequestMapper.toBusinessPartnerCreateRequest(auszahlung, BigInteger.valueOf(configService.getSystemid()), deliveryId);
            request.getSENDER().setSYSID(BigInteger.valueOf(configService.getSystemid()));
            request.getSENDER().setDELIVERYID(deliveryId);
            request.getBUSINESSPARTNER().setIDKEYS(new BusinessPartnerCreateRequest.BUSINESSPARTNER.IDKEYS());
            request.getBUSINESSPARTNER().getIDKEYS().setEXTID(String.valueOf(extId));
            request.getBUSINESSPARTNER().setHEADER(new BusinessPartnerCreateRequest.BUSINESSPARTNER.HEADER());
            request.getBUSINESSPARTNER().getHEADER().setPARTNCAT("1"); //todo: set correct category
            request.getBUSINESSPARTNER().getPERSDATA().setCORRESPONDLANGUAGEISO("DE"); //todo: set correct language iso
            request.getBUSINESSPARTNER().getORGDATA().setLANGUISO("DE");//todo: set correct language iso
            request.getBUSINESSPARTNER().getORGDATA().setNAME1("");//todo: set correct name 1
            request.getBUSINESSPARTNER().getORGDATA().setNAME2("");//todo: set correct name 2
            request.getBUSINESSPARTNER().getORGDATA().setNAME3("");//todo: set correct name 3
            request.getBUSINESSPARTNER().getORGDATA().setNAME4("");//todo: set correct name 4
            request.getBUSINESSPARTNER().getADDRESS().get(0).setADRKIND("XXDEFAULT");
            request.getBUSINESSPARTNER().getADDRESS().get(0).setCOUNTRY("CH");//todo: set iso country (max 3 instead of "Schweiz"

            final var xmlRequest = SoapUtils.buildXmlRequest(request,BusinessPartnerCreateRequest.class, SapEndpointName.BUSINESPARTNER);
            final var response = businessPartnerCreateClient.createBusinessPartner(
                xmlRequest);
            final var result = SoapUtils.parseSoapResponse(response, BusinessPartnerCreateResponse.class);
            return Response.status(HttpStatus.SC_OK).entity(result).build();
        }
        catch(WebApplicationException ex){
            return Response.status(HttpStatus.SC_BAD_REQUEST).entity(ex).build();
        } catch (JAXBException|SOAPException|IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    Note: specific values are still hardcoded or commented - these have to be discussed in the near future
     */
    public Response changeBusinessPartner(@Valid Auszahlung auszahlung, Integer businessPartnerId, BigDecimal deliveryId){
        try{
            final var  mapper = new BusinessPartnerChangeRequestMapper();
            var request = mapper.toBusinessPartnerChangeRequest(auszahlung, BigInteger.valueOf(configService.getSystemid()), deliveryId);
            request.getBUSINESSPARTNER().setHEADER(new BusinessPartnerChangeRequest.BUSINESSPARTNER.HEADER());
            request.getSENDER().setDELIVERYID(deliveryId);
            request.getSENDER().setSYSID(BigInteger.valueOf(configService.getSystemid()));
            request.getBUSINESSPARTNER().getHEADER().setBPARTNER(businessPartnerId);

            request.getBUSINESSPARTNER().setIDKEYS(new BusinessPartnerChangeRequest.BUSINESSPARTNER.IDKEYS());

            //request.getBUSINESSPARTNER().setHEADER(new BusinessPartnerChangeRequest.BUSINESSPARTNER.HEADER());
            //request.getBUSINESSPARTNER().getHEADER().setBPARTNER("1"); //todo: set correct category
            request.getBUSINESSPARTNER().getPERSDATA().setCORRESPONDLANGUAGEISO("DE"); //todo: set correct language iso
            request.getBUSINESSPARTNER().getORGDATA().setLANGUISO("DE");//todo: set correct language iso
            request.getBUSINESSPARTNER().getORGDATA().setNAME1("");//todo: set correct name 1
            request.getBUSINESSPARTNER().getORGDATA().setNAME2("");//todo: set correct name 2
            request.getBUSINESSPARTNER().getORGDATA().setNAME3("");//todo: set correct name 3
            request.getBUSINESSPARTNER().getORGDATA().setNAME4("");//todo: set correct name 4
            request.getBUSINESSPARTNER().getADDRESS().get(0).setADRKIND("XXDEFAULT");
            request.getBUSINESSPARTNER().getADDRESS().get(0).setCOUNTRY("CH");//todo: set iso country (max 3 instead of "Schweiz"

            final var response = businessPartnerChangeClient.changeBusinessPartner(SoapUtils.buildXmlRequest(request,BusinessPartnerChangeRequest.class,SapEndpointName.BUSINESPARTNER));
            final var result = SoapUtils.parseSoapResponse(response, BusinessPartnerChangeResponse.class);
            return Response.status(HttpStatus.SC_OK).entity(result).build();
        }
        catch(WebApplicationException ex){
            return Response.status(HttpStatus.SC_BAD_REQUEST).entity(ex).build();
        } catch (JAXBException|SOAPException|IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    Note: specific values are still hardcoded or commented - these have to be discussed in the near future
 */
    public Response createVendorPosting(@Valid Auszahlung auszahlung, Integer businessPartnerId,BigDecimal deliveryId){
        try{
            final var  mapper = new VendorPostingCreateRequestMapper();
            var request = mapper.toVendorPostingCreateRequest(auszahlung, BigInteger.valueOf(configService.getSystemid()), deliveryId);
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
                    date = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(localDate.getYear(),localDate.getMonthValue(), localDate.getDayOfMonth(), DatatypeConstants. FIELD_UNDEFINED);
                } catch (DatatypeConfigurationException e) {
                    throw new RuntimeException(e);
                }
                vendorposting.getHEADER().setDOCDATE(date);
                vendorposting.getHEADER().setPSTNGDATE(date);
                vendorposting.getHEADER().setCURRENCY("CHF");
            });

            final var response = vendorPostingCreateClient.createVendorPosting(SoapUtils.buildXmlRequest(request,VendorPostingCreateRequest.class, SapEndpointName.VENDORPOSTING));
            final var result = SoapUtils.parseSoapResponse(response, VendorPostingCreateResponse.class);
            return Response.status(HttpStatus.SC_OK).entity(result).build();
        }
        catch (WebApplicationException ex){
            return Response.status(HttpStatus.SC_BAD_REQUEST).entity(ex).build();
        } catch (JAXBException| SOAPException|IOException e ) {
            throw new RuntimeException(e);
        }
    }

}
