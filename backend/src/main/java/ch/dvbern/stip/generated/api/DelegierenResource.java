package ch.dvbern.stip.generated.api;

import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/delegieren/{gesuchId}/{sozialdienstId}")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface DelegierenResource {

    @POST
    @Produces({ "text/plain" })
    void gesuchDelegieren(@PathParam("gesuchId") UUID gesuchId,@PathParam("sozialdienstId") UUID sozialdienstId);
}
