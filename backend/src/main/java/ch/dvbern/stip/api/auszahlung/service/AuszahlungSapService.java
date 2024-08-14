package ch.dvbern.stip.api.auszahlung.service;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class AuszahlungSapService {
    @Location("AuszahlungSapService/SST_073_ImportStatusRead.xml")
    public Template SST_073_ImportStatusRead;

    public String renderRequest(String sysId, String deliveryId){
        return SST_073_ImportStatusRead.data("sysId",sysId,"deliveryId",deliveryId).render();
    }

}
