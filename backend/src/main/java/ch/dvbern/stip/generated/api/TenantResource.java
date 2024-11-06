package ch.dvbern.stip.generated.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;


@Path("/tenant/current")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface TenantResource {

    @GET
    @Produces({ "application/json", "text/plain" })
    Response getCurrentTenant();
}
