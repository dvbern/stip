package ch.dvbern.stip.generated.api;

import java.util.UUID;

import ch.dvbern.stip.generated.dto.GesuchsperiodeCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeUpdateDto;
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


@Path("/gesuchsperiode")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface GesuchsperiodeResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response createGesuchsperiode(@Valid @NotNull GesuchsperiodeCreateDto gesuchsperiodeCreateDto);

    @DELETE
    @Path("/{gesuchsperiodeId}")
    @Produces({ "text/plain" })
    Response deleteGesuchsperiode(@PathParam("gesuchsperiodeId") UUID gesuchsperiodeId);

    @GET
    @Path("/aktive")
    @Produces({ "application/json", "text/plain" })
    Response getAktiveGesuchsperioden();

    @GET
    @Path("/{gesuchsperiodeId}")
    @Produces({ "application/json", "text/plain" })
    Response getGesuchsperiode(@PathParam("gesuchsperiodeId") UUID gesuchsperiodeId);

    @GET
    @Produces({ "application/json", "text/plain" })
    Response getGesuchsperioden();

    @GET
    @Path("/latest")
    @Produces({ "application/json", "text/plain" })
    Response getLatest();

    @PATCH
    @Path("/publish/{gesuchsperiodeId}")
    @Produces({ "application/json" })
    Response publishGesuchsperiode(@PathParam("gesuchsperiodeId") UUID gesuchsperiodeId);

    @PATCH
    @Path("/{gesuchsperiodeId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response updateGesuchsperiode(@PathParam("gesuchsperiodeId") UUID gesuchsperiodeId,@Valid GesuchsperiodeUpdateDto gesuchsperiodeUpdateDto);
}
