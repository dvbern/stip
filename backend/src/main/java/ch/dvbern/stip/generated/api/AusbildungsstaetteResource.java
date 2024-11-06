package ch.dvbern.stip.generated.api;

import java.util.UUID;

import ch.dvbern.stip.generated.dto.AusbildungsstaetteCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteUpdateDto;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;


@Path("/ausbildungsstaette")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface AusbildungsstaetteResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response createAusbildungsstaette(@Valid AusbildungsstaetteCreateDto ausbildungsstaetteCreateDto);

    @DELETE
    @Path("/{ausbildungsstaetteId}")
    @Produces({ "text/plain" })
    Response deleteAusbildungsstaette(@PathParam("ausbildungsstaetteId") UUID ausbildungsstaetteId);

    @GET
    @Path("/{ausbildungsstaetteId}")
    @Produces({ "application/json", "text/plain" })
    Response getAusbildungsstaette(@PathParam("ausbildungsstaetteId") UUID ausbildungsstaetteId);

    @GET
    @Produces({ "application/json", "text/plain" })
    Response getAusbildungsstaetten();

    @PATCH
    @Path("/{ausbildungsstaetteId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response updateAusbildungsstaette(@PathParam("ausbildungsstaetteId") UUID ausbildungsstaetteId,@Valid AusbildungsstaetteUpdateDto ausbildungsstaetteUpdateDto);
}
