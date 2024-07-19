package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import java.util.UUID;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/gesuch")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface GesuchResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "text/plain" })
    Response createGesuch(@Valid @NotNull GesuchCreateDto gesuchCreateDto);

    @DELETE
    @Path("/{gesuchId}")
    @Produces({ "text/plain" })
    Response deleteGesuch(@PathParam("gesuchId") UUID gesuchId);

    @PATCH
    @Path("/{gesuchId}/einreichen")
    @Produces({ "application/json", "text/plain" })
    Response gesuchEinreichen(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/{gesuchId}/einreichen/validieren")
    @Produces({ "application/json", "text/plain" })
    Response gesuchEinreichenValidieren(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/{gesuchId}/berechnung")
    @Produces({ "application/json", "text/plain" })
    Response getBerechnungForGesuch(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/{gesuchId}")
    @Produces({ "application/json", "text/plain" })
    Response getGesuch(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/{gesuchId}/dokumente")
    @Produces({ "application/json", "text/plain" })
    Response getGesuchDokumente(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/fall/{fallId}")
    @Produces({ "application/json", "text/plain" })
    Response getGesucheForFall(@PathParam("fallId") UUID fallId);

    @GET
    @Path("/benutzer/me/gs")
    @Produces({ "application/json", "text/plain" })
    Response getGesucheGs();

    @GET
    @Path("/benutzer/me/sb/{getGesucheSBQueryTyp}")
    @Produces({ "application/json", "text/plain" })
    Response getGesucheSb(@PathParam("getGesucheSBQueryTyp") ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType getGesucheSBQueryTyp);

    @GET
    @Path("/{gesuchId}/requiredDokumente")
    @Produces({ "application/json", "text/plain" })
    Response getRequiredGesuchDokumentTyp(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/{gesuchId}/statusprotokoll")
    @Produces({ "application/json", "text/plain" })
    Response getStatusProtokoll(@PathParam("gesuchId") UUID gesuchId);

    @PATCH
    @Path("/{gesuchId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response updateGesuch(@PathParam("gesuchId") UUID gesuchId,@Valid @NotNull GesuchUpdateDto gesuchUpdateDto);

    @GET
    @Path("/validatePages/{gesuchId}")
    @Produces({ "application/json" })
    Response validateGesuchPages(@PathParam("gesuchId") UUID gesuchId);
}
