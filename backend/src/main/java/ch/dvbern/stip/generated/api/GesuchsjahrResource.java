package ch.dvbern.stip.generated.api;

import java.util.UUID;

import ch.dvbern.stip.generated.dto.GesuchsjahrCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsjahrUpdateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;


@Path("/gesuchsjahr")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface GesuchsjahrResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response createGesuchsjahr(@Valid @NotNull GesuchsjahrCreateDto gesuchsjahrCreateDto);

    @DELETE
    @Path("/{gesuchsjahrId}")
    @Produces({ "text/plain" })
    Response deleteGesuchsjahr(@PathParam("gesuchsjahrId") UUID gesuchsjahrId);

    @GET
    @Path("/{gesuchsjahrId}")
    @Produces({ "application/json", "text/plain" })
    Response getGesuchsjahr(@PathParam("gesuchsjahrId") UUID gesuchsjahrId);

    @GET
    @Produces({ "application/json", "text/plain" })
    Response getGesuchsjahre();

    @PATCH
    @Path("/publish/{gesuchsjahrId}")
    @Produces({ "application/json" })
    Response publishGesuchsjahr(@PathParam("gesuchsjahrId") UUID gesuchsjahrId);

    @PATCH
    @Path("/{gesuchsjahrId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response updateGesuchsjahr(@PathParam("gesuchsjahrId") UUID gesuchsjahrId,@Valid GesuchsjahrUpdateDto gesuchsjahrUpdateDto);
}
