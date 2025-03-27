package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.DelegierungCreateDto;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/delegieren/{fallId}/{sozialdienstId}")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface DelegierenResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "text/plain" })
    void fallDelegieren(@PathParam("fallId") UUID fallId,@PathParam("sozialdienstId") UUID sozialdienstId,@Valid @NotNull DelegierungCreateDto delegierungCreateDto);
}
