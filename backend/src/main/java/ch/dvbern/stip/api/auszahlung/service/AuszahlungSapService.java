package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.service.sap.SAPUtils;
import ch.dvbern.stip.generated.dto.AuszahlungDto;
import ch.dvbern.stip.generated.dto.AuszahlungImportStatusLogDto;
import ch.dvbern.stip.generated.dto.GetAuszahlungImportStatusResponseDto;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@ApplicationScoped
public class AuszahlungSapService {
    @Location("AuszahlungSapService/SST_073_ImportStatusRead.xml")
    public Template SST_073_ImportStatusRead;

    @Location("AuszahlungSapService/SST_009_BusinessPartnerCreate.xml")
    public Template SST_009_BusinessPartnerCreate;

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

    private GetAuszahlungImportStatusResponseDto getAndParseGetSAPImportStatusResponse(Integer deliveryId) throws JAXBException {
        GetAuszahlungImportStatusRequest data = new GetAuszahlungImportStatusRequest();
        data.setDeliveryId(deliveryId);
        data.setSysId(sysid);
        String response = importStatusReadClient.getImportStatus(buildPayload(
            SST_073_ImportStatusRead,
            String.valueOf(deliveryId),
            null,
            null,
            null,null));
        JAXBContext context = JAXBContext.newInstance(GetAuszahlungImportStatusResponse.class);
        String shortendResponse = response.substring(response.indexOf("<POSITION>"), response.indexOf("</POSITION>")+ "</POSITION>".length());
        StringReader reader = new StringReader(shortendResponse);
        final var parsed = (GetAuszahlungImportStatusResponse) context.createUnmarshaller()
            .unmarshal(reader);
        GetAuszahlungImportStatusResponseDto result = new GetAuszahlungImportStatusResponseDto();
        result.setStatus(parsed.getSTATUS());
        List<AuszahlungImportStatusLogDto> logs = parsed.getLOGS().stream().map(x -> new AuszahlungImportStatusLogDto(x.getDATETIME(), x.getMESSAGE())).toList();
        result.setLogs(logs);
        return result;
    }

    public Response getImportStatus(@Valid @Positive @NotNull Integer deliveryId){
        try{
            return Response.status(HttpStatus.SC_OK).entity(getAndParseGetSAPImportStatusResponse(deliveryId)).build();

        }
        catch(WebApplicationException | JAXBException ex){
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        }
    }

    public Response createBusinessPartner(@Valid AuszahlungDto dto){
        try{
            Auszahlung data = auszahlungMapper.toEntity(dto);
            String response = businessPartnerCreateClient.createBusinessPartner(
                buildPayload(
                    SST_009_BusinessPartnerCreate,
                    SAPUtils.generateDeliveryId(),
                    null,
                    SAPUtils.generateExtId(),
                    data,null
                ));
            return Response.status(HttpStatus.SC_OK).entity(response).build();
        }
        catch(WebApplicationException ex){
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        }
    }

    public Response changeBusinessPartner(@Valid AuszahlungDto dto){
        try{
            Auszahlung data = auszahlungMapper.toEntity(dto);
            String response = businessPartnerChangeClient.changeBusinessPartner(buildPayload(SST_077_BusinessPartnerChange,
                SAPUtils.generateDeliveryId(),
                null,
                SAPUtils.generateExtId(),
                data,null));
            return Response.status(HttpStatus.SC_OK).entity(response).build();
        }
        catch(WebApplicationException ex){
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        }
    }

    public Response createAuszahlung(@Valid AuszahlungDto dto){
        try{
            Auszahlung data = auszahlungMapper.toEntity(dto);
            String response = vendorPostingCreateClient.createVendorPosting(buildPayload(SST_003_VendorPostingCreate,
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
