package ch.dvbern.stip.generated.api;

import java.util.UUID;

import ch.dvbern.stip.generated.dto.AusbildungsgangCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangUpdateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;


@Path("/ausbildungsgang")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface AusbildungsgangResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response createAusbildungsgang(@Valid @NotNull AusbildungsgangCreateDto ausbildungsgangCreateDto);

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
    @Produces({ "application/json", "text/plain" })
    Response updateAusbildungsgang(@PathParam("ausbildungsgangId") UUID ausbildungsgangId,@Valid @NotNull AusbildungsgangUpdateDto ausbildungsgangUpdateDto);
}
