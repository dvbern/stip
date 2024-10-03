package ch.dvbern.stip.generated.api;

import java.util.UUID;

import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
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


@Path("/gesuch")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface GesuchResource {

    @POST
    @Path("/status/in-bearbeitung/{gesuchId}")
    @Produces({ "application/json", "text/plain" })
    Response changeGesuchStatusToInBearbeitung(@PathParam("gesuchId") UUID gesuchId);

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

    @PATCH
    @Path("/{gesuchId}/fehlendeDokumente")
    @Produces({ "application/json", "text/plain" })
    Response gesuchFehlendeDokumenteUebermitteln(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/{gesuchId}/berechnung")
    @Produces({ "application/json", "text/plain" })
    Response getBerechnungForGesuch(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/{gesuchId}/current")
    @Produces({ "application/json", "text/plain" })
    Response getCurrentGesuch(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/{gesuchId}/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    Response getGesuch(@PathParam("gesuchId") UUID gesuchId,@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/fall/{fallId}")
    @Produces({ "application/json", "text/plain" })
    Response getGesucheForFall(@PathParam("fallId") UUID fallId);

    @GET
    @Path("/benutzer/me/gs")
    @Produces({ "application/json", "text/plain" })
    Response getGesucheGs();

    @GET
    @Path("/benutzer/me/sb/{getGesucheSBQueryType}")
    @Produces({ "application/json", "text/plain" })
    Response getGesucheSb(@PathParam("getGesucheSBQueryType") ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType getGesucheSBQueryType);

    @GET
    @Path("/benutzer/me/gs-dashboard")
    @Produces({ "application/json", "text/plain" })
    Response getGsDashboard();

    @GET
    @Path("/{aenderungId}/aenderung/gs/changes")
    @Produces({ "application/json", "text/plain" })
    Response getGsTrancheChanges(@PathParam("aenderungId") UUID aenderungId);

    @GET
    @Path("/{aenderungId}/aenderung/sb/changes")
    @Produces({ "application/json", "text/plain" })
    Response getSbTrancheChanges(@PathParam("aenderungId") UUID aenderungId);

    @GET
    @Path("/{gesuchId}/statusprotokoll")
    @Produces({ "application/json", "text/plain" })
    Response getStatusProtokoll(@PathParam("gesuchId") UUID gesuchId);

    @PATCH
    @Path("/{gesuchId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response updateGesuch(@PathParam("gesuchId") UUID gesuchId,@Valid @NotNull GesuchUpdateDto gesuchUpdateDto);
}
