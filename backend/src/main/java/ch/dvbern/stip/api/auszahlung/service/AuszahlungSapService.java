package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.generated.dto.AuszahlungImportStatusLogDto;
import ch.dvbern.stip.generated.dto.GetAuszahlungImportStatusRequestDto;
import ch.dvbern.stip.generated.dto.GetAuszahlungImportStatusResponseDto;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.StringReader;
import java.util.List;

@RequiredArgsConstructor
@ApplicationScoped
public class AuszahlungSapService {
    @Location("AuszahlungSapService/SST_073_ImportStatusRead.xml")
    public Template SST_073_ImportStatusRead;

    @Location("AuszahlungSapService/SST_009_BusinessPartnerCreate.xml")
    public Template SST_009_BusinessPartnerCreate;

    private final GetAuszahlungImportStatusRequestMapper getAuszahlungImportStatusRequestMapper;

    @ConfigProperty(name = "stip_sap_sysid")
    Integer sysid;

    @RestClient
    ImportStatusReadClient importStatusReadClient;

    private String buildPayload(Template template, GetAuszahlungImportStatusRequest data){
        return template.data("dto", data).render();
    }

    private GetAuszahlungImportStatusResponseDto getAndParseSAPResponse(GetAuszahlungImportStatusRequestDto dto) throws JAXBException {
        GetAuszahlungImportStatusRequest data = getAuszahlungImportStatusRequestMapper.toGetAuszahlungImportStatusRequest(dto);
        data.setSysId(sysid);
        String response = importStatusReadClient.getImportStatus(buildPayload(SST_073_ImportStatusRead,data));
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

    public Response getImportStatus(GetAuszahlungImportStatusRequestDto dto){
        try{
            return Response.status(HttpStatus.SC_OK).entity(getAndParseSAPResponse(dto)).build();

        }
        catch(WebApplicationException | JAXBException ex){
            return Response.status(HttpStatus.SC_BAD_REQUEST).build();
        }
    }

    public String createBusinessPartner(String sysId, String deliveryId){
        return SST_009_BusinessPartnerCreate.data("sysId",sysId,"deliveryId",deliveryId).render();
    }

}
