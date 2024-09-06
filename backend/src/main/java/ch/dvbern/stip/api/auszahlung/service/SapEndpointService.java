package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.change.BusinessPartnerChangeRequest;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.change.BusinessPartnerChangeRequestMapper;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.change.BusinessPartnerChangeResponse;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.create.BusinessPartnerCreateRequest;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.create.BusinessPartnerCreateRequestMapper;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.create.BusinessPartnerCreateResponse;
import ch.dvbern.stip.api.auszahlung.sap.importstatus.ImportStatusReadRequest;
import ch.dvbern.stip.api.auszahlung.sap.importstatus.ImportStatusReadResponse;
import ch.dvbern.stip.api.auszahlung.sap.importstatus.SenderParms;
import ch.dvbern.stip.api.auszahlung.sap.util.SapEndpointName;
import ch.dvbern.stip.api.auszahlung.sap.util.SoapUtils;
import ch.dvbern.stip.api.auszahlung.sap.vendorposting.VendorPostingCreateRequest;
import ch.dvbern.stip.api.auszahlung.sap.vendorposting.VendorPostingCreateRequestMapper;
import ch.dvbern.stip.api.auszahlung.sap.vendorposting.VendorPostingCreateResponse;
import ch.dvbern.stip.api.config.service.ConfigService;
import io.quarkus.jaxb.runtime.JaxbContextProducer;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;


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
    @Inject
    JaxbContextProducer jaxbContext;


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

    public Response createBusinessPartner(@Valid Auszahlung auszahlung,String extId, BigDecimal deliveryId){
        try{
            final var businessPartnerCreateRequestMapper = new BusinessPartnerCreateRequestMapper();
            var request = businessPartnerCreateRequestMapper.toBusinessPartnerCreateRequest(auszahlung, BigInteger.valueOf(configService.getSystemid()), deliveryId);
            request.getSENDER().setSYSID(BigInteger.valueOf(configService.getSystemid()));
            request.getSENDER().setDELIVERYID(deliveryId);
            request.getBUSINESSPARTNER().setIDKEYS(new BusinessPartnerCreateRequest.BUSINESSPARTNER.IDKEYS());
            request.getBUSINESSPARTNER().getIDKEYS().setEXTID(String.valueOf(extId));
            final var xmlRequest = SoapUtils.buildXmlRequest(request,BusinessPartnerCreateRequest.class, SapEndpointName.BUSINESPARTNER);

            final var response = businessPartnerCreateClient.createBusinessPartner(
                xmlRequest);
            final var result = SoapUtils.parseSoapResponse(response, BusinessPartnerCreateResponse.class);
            return Response.status(HttpStatus.SC_OK).entity(result).build();
        }
        catch(WebApplicationException ex){
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        } catch (JAXBException|SOAPException|IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response changeBusinessPartner(@Valid Auszahlung auszahlung, Integer businessPartnerId, BigDecimal deliveryId){
        try{
            final var  mapper = new BusinessPartnerChangeRequestMapper();
            var request = mapper.toBusinessPartnerChangeRequest(auszahlung, BigInteger.valueOf(configService.getSystemid()), deliveryId);
            request.getBUSINESSPARTNER().setHEADER(new BusinessPartnerChangeRequest.BUSINESSPARTNER.HEADER());
            request.getSENDER().setDELIVERYID(deliveryId);
            request.getSENDER().setSYSID(BigInteger.valueOf(configService.getSystemid()));
            request.getBUSINESSPARTNER().getHEADER().setBPARTNER(businessPartnerId);
            final var response = businessPartnerChangeClient.changeBusinessPartner(SoapUtils.buildXmlRequest(request,BusinessPartnerChangeRequest.class,SapEndpointName.BUSINESPARTNER));
            final var result = SoapUtils.parseSoapResponse(response, BusinessPartnerChangeResponse.class);
            return Response.status(HttpStatus.SC_OK).entity(result).build();
        }
        catch(WebApplicationException ex){
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        } catch (JAXBException|SOAPException|IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response createVendorPosting(@Valid Auszahlung auszahlung, Integer businessPartnerId,BigDecimal deliveryId){
        try{
            final var  mapper = new VendorPostingCreateRequestMapper();
            var request = mapper.toVendorPostingCreateRequest(auszahlung, BigInteger.valueOf(configService.getSystemid()), deliveryId);
            request.getSENDER().setSYSID(BigInteger.valueOf(configService.getSystemid()));
            request.getSENDER().setDELIVERYID(deliveryId);
            request.getVENDORPOSTING().forEach(vendorposting -> {
                vendorposting.getVENDOR().setVENDORNO(String.valueOf(businessPartnerId));
            });
            final var response = vendorPostingCreateClient.createVendorPosting(SoapUtils.buildXmlRequest(request,VendorPostingCreateRequest.class, SapEndpointName.VENDORPOSTING));
            final var result = SoapUtils.parseSoapResponse(response, VendorPostingCreateResponse.class);
            return Response.status(HttpStatus.SC_OK).entity(result).build();
        }
        catch (WebApplicationException ex){
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        } catch (JAXBException| SOAPException|IOException e ) {
            throw new RuntimeException(e);
        }
    }

}
