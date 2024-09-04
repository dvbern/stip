package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.change.BusinessPartnerChangeRequest;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.change.BusinessPartnerChangeRequestMapper;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.create.BusinessPartnerCreateRequest;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.create.BusinessPartnerCreateRequestMapper;
import ch.dvbern.stip.api.auszahlung.sap.importstatus.ImportStatusReadRequest;
import ch.dvbern.stip.api.auszahlung.sap.importstatus.ImportStatusReadResponse;
import ch.dvbern.stip.api.auszahlung.sap.importstatus.SenderParms;
import ch.dvbern.stip.api.auszahlung.sap.util.SapEndpointName;
import ch.dvbern.stip.api.auszahlung.sap.util.SoapUtils;
import ch.dvbern.stip.api.auszahlung.sap.vendorposting.VendorPostingCreateRequest;
import ch.dvbern.stip.api.auszahlung.sap.vendorposting.VendorPostingCreateRequestMapper;
import ch.dvbern.stip.api.auszahlung.service.sap.SAPUtils;
import ch.dvbern.stip.generated.dto.AuszahlungDto;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.soap.SOAPException;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@ApplicationScoped
public class AuszahlungSapService {
    @Location("AuszahlungSapService/SST_077_BusinessPartnerChange.xml")
    public Template SST_077_BusinessPartnerChange;

    @Location("AuszahlungSapService/SST_003_VendorPostingCreate.xml")
    public Template SST_003_VendorPostingCreate;

    @Location("AuszahlungSapService/SST_074_BusinessPartnerSearch.xml")
    public Template SST_074_BusinessPartnerSearch;

    private final AuszahlungMapper auszahlungMapper;

    @ConfigProperty(name = "stip_sap_sysid")
    Integer sysid;

    @RestClient
    ImportStatusReadClient importStatusReadClient;

    @RestClient
    BusinessPartnerCreateClient businessPartnerCreateClient;

    @RestClient
    BusinessPartnerChangeClient businessPartnerChangeClient;

    @RestClient
    VendorPostingCreateClient vendorPostingCreateClient;

    @RestClient
    SearchBusinessPartnerClient searchBusinessPartnerClient;

    private String buildPayload(Template template, String deliveryId, String businessPartnerId, String extId, Auszahlung auszahlung, Map<String,Object> additionalData){
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("sysId", sysid);
        dataMap.put("deliveryId", deliveryId);
        dataMap.put("auszahlung",auszahlung);
        dataMap.put("businessPartnerId", businessPartnerId);
        dataMap.put("extId", extId);
        dataMap.put("additionalData", additionalData);
        return template.data("data", dataMap).render();
    }

    private ImportStatusReadResponse  getAndParseGetSAPImportStatusResponse(Integer deliveryId) throws JAXBException, SOAPException, IOException {
        ImportStatusReadRequest request = new ImportStatusReadRequest();
        SenderParms senderParms = new SenderParms();
        senderParms.setSYSID(BigInteger.valueOf(sysid));
        request.setSENDER(senderParms);
        ImportStatusReadRequest.FILTERPARMS filterparms = new ImportStatusReadRequest.FILTERPARMS();
        filterparms.setDELIVERYID(BigDecimal.valueOf(deliveryId));
        request.setFILTERPARMS(filterparms);

        String xmlRequest = SoapUtils.buildXmlRequest(request,ImportStatusReadRequest.class, SapEndpointName.IMPORT_STATUS);
        String xmlResponse = importStatusReadClient.getImportStatus(xmlRequest);
        return SoapUtils.parseSoapResponse(xmlResponse, ImportStatusReadResponse.class);
    }

    public Response getImportStatus(@Valid @Positive @NotNull Integer deliveryId){
        try{
            return Response.status(HttpStatus.SC_OK).entity(getAndParseGetSAPImportStatusResponse(deliveryId)).build();

        }
        catch(WebApplicationException | JAXBException | SOAPException | IOException ex){
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        }
    }

    public Response createBusinessPartner(@Valid AuszahlungDto dto, BigDecimal deliveryId){
        try{
            Auszahlung auszahlung = auszahlungMapper.toEntity(dto);
            BusinessPartnerCreateRequestMapper businessPartnerCreateRequestMapper = new BusinessPartnerCreateRequestMapper();
            BusinessPartnerCreateRequest request = businessPartnerCreateRequestMapper.toBusinessPartnerCreateRequest(auszahlung, BigInteger.valueOf(sysid), deliveryId);
            String xmlRequest = SoapUtils.buildXmlRequest(request,BusinessPartnerCreateRequest.class, SapEndpointName.BUSINESPARTNER);


            String response = businessPartnerCreateClient.createBusinessPartner(
                xmlRequest);
            return Response.status(HttpStatus.SC_OK).entity(response).build();
        }
        catch(WebApplicationException ex){
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } catch (SOAPException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response changeBusinessPartner(@Valid AuszahlungDto dto, BigDecimal deliveryId){
        try{
            Auszahlung auszahlung = auszahlungMapper.toEntity(dto);

            BusinessPartnerChangeRequestMapper mapper = new BusinessPartnerChangeRequestMapper();
            BusinessPartnerChangeRequest request = mapper.toBusinessPartnerChangeRequest(auszahlung, BigInteger.valueOf(sysid), deliveryId);

            String response = businessPartnerChangeClient.changeBusinessPartner(SoapUtils.buildXmlRequest(request,BusinessPartnerChangeRequest.class,SapEndpointName.BUSINESPARTNER));
            return Response.status(HttpStatus.SC_OK).entity(response).build();
        }
        catch(WebApplicationException ex){
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } catch (SOAPException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response createAuszahlung(@Valid AuszahlungDto dto, BigDecimal deliveryId){
        try{
            Auszahlung auszahlung = auszahlungMapper.toEntity(dto);
            VendorPostingCreateRequestMapper mapper = new VendorPostingCreateRequestMapper();
            VendorPostingCreateRequest request = mapper.toVendorPostingCreateRequest(auszahlung, BigInteger.valueOf(sysid), deliveryId);

            String response = vendorPostingCreateClient.createVendorPosting(SoapUtils.buildXmlRequest(request,VendorPostingCreateRequest.class, SapEndpointName.VENDORPOSTING));
            return Response.status(HttpStatus.SC_OK).entity(response).build();
        }
        catch (WebApplicationException ex){
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } catch (SOAPException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response searchBusinessPartner(@Valid AuszahlungDto auszahlungDto){
        try{
            Auszahlung data = auszahlungMapper.toEntity(auszahlungDto);
            String response = searchBusinessPartnerClient.searchBusinessPartner(buildPayload(SST_074_BusinessPartnerSearch,
                SAPUtils.generateDeliveryId(),
                null,
                SAPUtils.generateExtId(),
                data,null));
            return Response.status(HttpStatus.SC_OK).entity(response).build();

        }
        catch (WebApplicationException ex){
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        }
    }

}
