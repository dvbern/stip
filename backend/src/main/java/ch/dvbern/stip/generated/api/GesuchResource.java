package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
import java.time.LocalDate;
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
    @Produces({ "application/json", "text/plain" })
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
    @Path("/{gesuchTrancheId}/fehlendeDokumenteEinreichen")
    @Produces({ "application/json", "text/plain" })
    Response gesuchTrancheFehlendeDokumenteEinreichen(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

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
    @Path("/{gesuchId}/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    Response getGesuch(@PathParam("gesuchId") UUID gesuchId,@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/benutzer/me/gs")
    @Produces({ "application/json", "text/plain" })
    Response getGesucheGs();

    @GET
    @Path("/benutzer/me/sb/{getGesucheSBQueryType}")
    @Produces({ "application/json", "text/plain" })
    Response getGesucheSb(@PathParam("getGesucheSBQueryType") ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType getGesucheSBQueryType,@QueryParam("typ") @NotNull   ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp typ,@QueryParam("page") @NotNull   Integer page,@QueryParam("pageSize") @NotNull   Integer pageSize,@QueryParam("fallNummer")   String fallNummer,@QueryParam("piaNachname")   String piaNachname,@QueryParam("piaVorname")   String piaVorname,@QueryParam("piaGeburtsdatum")   LocalDate piaGeburtsdatum,@QueryParam("status")   String status,@QueryParam("bearbeiter")   String bearbeiter,@QueryParam("letzteAktivitaetFrom")   LocalDate letzteAktivitaetFrom,@QueryParam("letzteAktivitaetTo")   LocalDate letzteAktivitaetTo,@QueryParam("sortColumn")   ch.dvbern.stip.api.gesuch.type.SbDashboardColumn sortColumn,@QueryParam("sortOrder")   ch.dvbern.stip.api.gesuch.type.SortOrder sortOrder);

    @GET
    @Path("/{aenderungId}/aenderung/gs/changes")
    @Produces({ "application/json", "text/plain" })
    Response getGsAenderungChangesInBearbeitung(@PathParam("aenderungId") UUID aenderungId);

    @GET
    @Path("/benutzer/me/gs-dashboard")
    @Produces({ "application/json", "text/plain" })
    Response getGsDashboard();

    @GET
    @Path("/changes/{trancheId}")
    @Produces({ "application/json", "text/plain" })
    Response getInitialTrancheChangesByTrancheId(@PathParam("trancheId") UUID trancheId);

    @GET
    @Path("/{aenderungId}/aenderung/sb/changes")
    @Produces({ "application/json", "text/plain" })
    Response getSbAenderungChanges(@PathParam("aenderungId") UUID aenderungId);

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
