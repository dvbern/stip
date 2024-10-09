package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/gesuch")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface GesuchResource {

    @PATCH
    @Path("/{gesuchId}/bearbeitungAbschliessen")
    @Produces({ "application/json", "text/plain" })
    Response bearbeitungAbschliessen(@PathParam("gesuchId") UUID gesuchId);

    @POST
    @Path("/status/bereit-fuer-bearbeitung/{gesuchId}")
    @Produces({ "application/json", "text/plain" })
    Response changeGesuchStatusToBereitFuerBearbeitung(@PathParam("gesuchId") UUID gesuchId);

    @POST
    @Path("/status/in-bearbeitung/{gesuchId}")
    @Produces({ "application/json", "text/plain" })
    Response changeGesuchStatusToInBearbeitung(@PathParam("gesuchId") UUID gesuchId);

    @POST
    @Path("/status/verfuegt/{gesuchId}")
    @Produces({ "application/json", "text/plain" })
    Response changeGesuchStatusToVerfuegt(@PathParam("gesuchId") UUID gesuchId);

    @POST
    @Path("/status/versendet/{gesuchId}")
    @Produces({ "application/json", "text/plain" })
    Response changeGesuchStatusToVersendet(@PathParam("gesuchId") UUID gesuchId);

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

    @PATCH
    @Path("/{gesuchId}/gesuchZurueckweisen")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response gesuchZurueckweisen(@PathParam("gesuchId") UUID gesuchId,@Valid KommentarDto kommentarDto);

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
    Response getGesucheSb(@PathParam("getGesucheSBQueryType") ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType getGesucheSBQueryType,@QueryParam("fallNummer")   String fallNummer,@QueryParam("piaNachname")   String piaNachname,@QueryParam("piaVorname")   String piaVorname,@QueryParam("piaGeburtsdatum")   String piaGeburtsdatum,@QueryParam("status")   String status,@QueryParam("bearbeiter")   String bearbeiter,@QueryParam("letzteAktivitaetFrom")   String letzteAktivitaetFrom,@QueryParam("letzteAktivitaetTo")   String letzteAktivitaetTo,@QueryParam("page")   Integer page,@QueryParam("pageSize")   Integer pageSize);

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
    @Path("/{gesuchId}/juristischAbklaeren")
    @Produces({ "application/json", "text/plain" })
    Response juristischAbklaeren(@PathParam("gesuchId") UUID gesuchId);

    @PATCH
    @Path("/{gesuchId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response updateGesuch(@PathParam("gesuchId") UUID gesuchId,@Valid @NotNull GesuchUpdateDto gesuchUpdateDto);
}
