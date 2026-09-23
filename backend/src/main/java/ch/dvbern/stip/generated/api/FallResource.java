package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.FallDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.ResponseStatus;



import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")
public interface FallResource {

    @POST
    @Path("/fall/gs")
    @Produces({ "application/json", "text/plain" })
    FallDto createFallForGs();

    @GET
    @Path("/fall/sb")
    @Produces({ "application/json", "text/plain" })
    List<FallDto> getFaelleForSb();

    @GET
    @Path("/fall/gs")
    @Produces({ "application/json", "text/plain" })
    FallDto getFallForGs();
}
