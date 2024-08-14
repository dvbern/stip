package ch.dvbern.stip.api.auszahlung.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@ApplicationScoped
@Produces(MediaType.TEXT_XML)
@Consumes(MediaType.TEXT_XML)
@RegisterRestClient(configKey = "AuszahlungSapImportStatusEndpoint")
@ClientHeaderParam(name = "Authorization", value = "{getAuthHeader}")
public interface AuszahlungImportStatusSapServiceClient {
    default String getAuthHeader(){
        return ConfigProvider.getConfig().getValue("kstip.stip_sap_auth_header_value",String.class);
    }
    @POST
    String getImportStatus(String xml);
}
