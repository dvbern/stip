package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangUpdateDto;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/ausbildungsgang")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface AusbildungsgangResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "text/plain" })
    Response createAusbildungsgang(@Valid @NotNull AusbildungsgangUpdateDto ausbildungsgangUpdateDto);

    @DELETE
    @Path("/{ausbildungsgangId}")
    @Produces({ "text/plain" })
    Response deleteAusbildungsgang(@PathParam("ausbildungsgangId") UUID ausbildungsgangId);

    @GET
    @Path("/{ausbildungsgangId}")
    @Produces({ "application/json", "text/plain" })
    Response getAusbildungsgang(@PathParam("ausbildungsgangId") UUID ausbildungsgangId);

    @PUT
    @Path("/{ausbildungsgangId}")
    @Consumes({ "application/json" })
    @Produces({ "text/plain" })
    Response updateAusbildungsgang(@PathParam("ausbildungsgangId") UUID ausbildungsgangId,@Valid @NotNull AusbildungsgangUpdateDto ausbildungsgangUpdateDto);
}
