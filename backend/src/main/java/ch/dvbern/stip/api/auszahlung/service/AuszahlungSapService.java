package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.generated.dto.GetAuszahlungImportStatusRequestDto;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class AuszahlungSapService {
    @Location("AuszahlungSapService/SST_073_ImportStatusRead.xml")
    public Template SST_073_ImportStatusRead;

    @Location("AuszahlungSapService/SST_009_BusinessPartnerCreate.xml")
    public Template SST_009_BusinessPartnerCreate;

    public String getImportStatus(GetAuszahlungImportStatusRequestDto dto){
        return SST_073_ImportStatusRead.data("dto",dto).render();
    }

    public String createBusinessPartner(String sysId, String deliveryId){
        return SST_009_BusinessPartnerCreate.data("sysId",sysId,"deliveryId",deliveryId).render();
    }

}
