package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.GesuchNotizCreateDto;
import ch.dvbern.stip.generated.dto.GesuchNotizUpdateDto;
import ch.dvbern.stip.generated.dto.JuristischeAbklaerungNotizAntwortDto;
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

import java.util.UUID;


@Path("/gesuch")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface GesuchNotizResource {

    @PATCH
    @Path("/notiz/juristischeAbklaerung/{notizId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response answerJuristischeAbklaerungNotiz(@PathParam("notizId") UUID notizId,@Valid @NotNull JuristischeAbklaerungNotizAntwortDto juristischeAbklaerungNotizAntwortDto);

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
    @Path("/notiz/juristischeAbklaerung/{notizId}")
    @Produces({ "application/json", "text/plain" })
    Response getJuristischeAbklaerungNotiz(@PathParam("notizId") UUID notizId);

    @GET
    @Path("/notiz/juristischeAbklaerung/{gesuchId}")
    @Produces({ "application/json", "text/plain" })
    Response getJuristischeAbklaerungNotizen(@PathParam("gesuchId") UUID gesuchId);

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
