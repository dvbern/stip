package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.LandDto;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/land")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface LandResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    LandDto createLand(@Valid @NotNull LandDto landDto);

    @GET
    @Produces({ "application/json", "text/plain" })
    List<LandDto> getLaender();

    @PATCH
    @Path("/{landId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    LandDto updateLand(@PathParam("landId") UUID landId,@Valid @NotNull LandDto landDto);
}
