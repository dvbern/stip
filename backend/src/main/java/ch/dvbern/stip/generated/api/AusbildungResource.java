package ch.dvbern.stip.generated.api;

import java.util.UUID;

import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;


@Path("/ausbildung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface AusbildungResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response createAusbildung(@Valid @NotNull AusbildungUpdateDto ausbildungUpdateDto);

    @GET
    @Path("/{ausbildungId}")
    @Produces({ "application/json", "text/plain" })
    Response getAusbildung(@PathParam("ausbildungId") UUID ausbildungId);

    @PATCH
    @Path("/{ausbildungId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response updateAusbildung(@PathParam("ausbildungId") UUID ausbildungId,@Valid @NotNull AusbildungUpdateDto ausbildungUpdateDto);
}
