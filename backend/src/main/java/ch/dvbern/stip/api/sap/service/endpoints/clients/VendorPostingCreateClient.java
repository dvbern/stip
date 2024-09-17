package ch.dvbern.stip.api.sap.service.endpoints.clients;

import ch.dvbern.stip.api.sap.util.RequestHeaderProviderUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@ApplicationScoped
@Produces(MediaType.TEXT_XML)
@Consumes(MediaType.TEXT_XML)
@RegisterRestClient(configKey = "AuszahlungSapVendorPostingCreateEndpoint")
@ClientHeaderParam(name = "Authorization", value = "{getAuthHeader}")
public interface VendorPostingCreateClient extends RequestHeaderProviderUtil{
    @POST
    String createVendorPosting(String xml);
}
