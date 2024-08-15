package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.generated.dto.GetAuszahlungImportStatusRequestDto;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@RequiredArgsConstructor
@ApplicationScoped
public class AuszahlungSapService {
    @Location("AuszahlungSapService/SST_073_ImportStatusRead.xml")
    public Template SST_073_ImportStatusRead;

    @Location("AuszahlungSapService/SST_009_BusinessPartnerCreate.xml")
    public Template SST_009_BusinessPartnerCreate;

    @RestClient
    ImportStatusReadClient importStatusReadClient;

    private String buildPayload(Template template, Object data){
        return template.data("dto", data).render();
    }

    public String getImportStatus(GetAuszahlungImportStatusRequestDto dto){
        return importStatusReadClient.getImportStatus(buildPayload(SST_073_ImportStatusRead,dto));
    }

    public String createBusinessPartner(String sysId, String deliveryId){
        return SST_009_BusinessPartnerCreate.data("sysId",sysId,"deliveryId",deliveryId).render();
    }

}
