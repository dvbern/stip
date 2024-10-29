package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.GesuchNotizCreateDto;
import ch.dvbern.stip.generated.dto.GesuchNotizUpdateDto;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/gesuch")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface GesuchNotizResource {

    @POST
    @Path("/notiz/create")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response createNotiz(@Valid @NotNull GesuchNotizCreateDto gesuchNotizCreateDto);

    @DELETE
    @Path("/notiz/{notizId}")
    @Produces({ "text/plain" })
    Response deleteNotiz(@PathParam("notizId") UUID notizId);

    @GET
    @Path("/notiz/{notizId}")
    @Produces({ "application/json", "text/plain" })
    Response getNotiz(@PathParam("notizId") UUID notizId);

    @GET
    @Path("/{gesuchId}/notiz/all")
    @Produces({ "application/json", "text/plain" })
    Response getNotizen(@PathParam("gesuchId") UUID gesuchId);

    @PATCH
    @Path("/notiz")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response updateNotiz(@Valid @NotNull GesuchNotizUpdateDto gesuchNotizUpdateDto);
}
