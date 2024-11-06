package ch.dvbern.stip.generated.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;


@Path("/stammdaten/land")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface StammdatenResource {

    @GET
    @Produces({ "application/json", "text/plain" })
    Response getLaender();
}
