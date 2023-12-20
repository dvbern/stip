package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.AusbildungsstaetteDto;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/ausbildungsstaette")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface AusbildungsstaetteResource {

    @GET
    @Path("/{ausbildungsstaetteId}")
    @Produces({ "application/json", "text/plain" })
    Response getAusbildungsstaette(@PathParam("ausbildungsstaetteId") UUID ausbildungsstaetteId);

    @GET
    @Produces({ "application/json", "text/plain" })
    Response getAusbildungsstaetten();
}
