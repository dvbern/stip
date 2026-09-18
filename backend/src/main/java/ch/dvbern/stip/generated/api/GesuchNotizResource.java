package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.GesuchNotizCreateDto;
import ch.dvbern.stip.generated.dto.GesuchNotizDto;
import ch.dvbern.stip.generated.dto.GesuchNotizUpdateDto;
import ch.dvbern.stip.generated.dto.JuristischeAbklaerungNotizAntwortDto;
import java.util.UUID;
import ch.dvbern.stip.generated.dto.ValidationReportDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.ResponseStatus;



import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")
public interface GesuchNotizResource {

    @PATCH
    @Path("/gesuch/notiz/juristischeAbklaerung/{notizId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchNotizDto answerJuristischeAbklaerungNotiz(@PathParam("notizId") UUID notizId,@Valid @NotNull JuristischeAbklaerungNotizAntwortDto juristischeAbklaerungNotizAntwortDto);

    @POST
    @Path("/gesuch/notiz/create")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchNotizDto createNotiz(@Valid @NotNull GesuchNotizCreateDto gesuchNotizCreateDto);

    @DELETE
    @Path("/gesuch/notiz/{notizId}")
    @Produces({ "text/plain" })
    void deleteNotiz(@PathParam("notizId") UUID notizId);

    @GET
    @Path("/gesuch/notiz/{notizId}")
    @Produces({ "application/json", "text/plain" })
    GesuchNotizDto getNotiz(@PathParam("notizId") UUID notizId);

    @GET
    @Path("/gesuch/{gesuchId}/notiz/all")
    @Produces({ "application/json", "text/plain" })
    List<GesuchNotizDto> getNotizen(@PathParam("gesuchId") UUID gesuchId);

    @PATCH
    @Path("/gesuch/notiz")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchNotizDto updateNotiz(@Valid @NotNull GesuchNotizUpdateDto gesuchNotizUpdateDto);
}
