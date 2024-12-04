package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.FallDto;

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
    @Path("/gs")
    @Produces({ "application/json", "text/plain" })
    FallDto createFallForGs();

    @GET
    @Path("/sb")
    @Produces({ "application/json", "text/plain" })
    List<FallDto> getFaelleForSb();

    @GET
    @Path("/gs")
    @Produces({ "application/json", "text/plain" })
    FallDto getFallForGs();
}
