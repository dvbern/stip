package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.FallDto;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/fall")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface FallResource {

    @POST
    @Path("/benutzer/{benutzerId}")
    @Produces({ "text/plain" })
    Response createFall(@PathParam("benutzerId") UUID benutzerId);

    @GET
    @Path("/{fallId}")
    @Produces({ "application/json", "text/plain" })
    Response getFall(@PathParam("fallId") UUID fallId);

    @GET
    @Path("/benutzer/{benutzerId}")
    @Produces({ "application/json", "text/plain" })
    Response getFallForBenutzer(@PathParam("benutzerId") UUID benutzerId);
}
