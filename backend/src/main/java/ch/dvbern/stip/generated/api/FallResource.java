package ch.dvbern.stip.generated.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;


@Path("/fall")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface FallResource {

    @POST
    @Path("/gs")
    @Produces({ "application/json", "text/plain" })
    Response createFallForGs();

    @GET
    @Path("/sb")
    @Produces({ "application/json", "text/plain" })
    Response getFaelleForSb();

    @GET
    @Path("/gs")
    @Produces({ "application/json", "text/plain" })
    Response getFallForGs();
}
