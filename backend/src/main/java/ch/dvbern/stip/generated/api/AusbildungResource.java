package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.AusbildungDto;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/ausbildung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface AusbildungResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    AusbildungDto createAusbildung(@Valid @NotNull AusbildungUpdateDto ausbildungUpdateDto);

    @GET
    @Path("/{ausbildungId}")
    @Produces({ "application/json", "text/plain" })
    AusbildungDto getAusbildung(@PathParam("ausbildungId") UUID ausbildungId);

    @PATCH
    @Path("/{ausbildungId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    AusbildungDto updateAusbildung(@PathParam("ausbildungId") UUID ausbildungId,@Valid @NotNull AusbildungUpdateDto ausbildungUpdateDto);
}
