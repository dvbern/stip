package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
import java.time.LocalDate;
import ch.dvbern.stip.generated.dto.PaginatedSbDashboardDto;
import ch.dvbern.stip.generated.dto.StatusprotokollEntryDto;
import java.util.UUID;
import ch.dvbern.stip.generated.dto.ValidationReportDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
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
    Response getGesucheSb(@PathParam("getGesucheSBQueryType") ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType getGesucheSBQueryType,@QueryParam("typ") @NotNull   ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp typ,@QueryParam("page") @NotNull   Integer page,@QueryParam("pageSize") @NotNull   Integer pageSize,@QueryParam("fallNummer")   String fallNummer,@QueryParam("piaNachname")   String piaNachname,@QueryParam("piaVorname")   String piaVorname,@QueryParam("piaGeburtsdatum")   LocalDate piaGeburtsdatum,@QueryParam("status")   ch.dvbern.stip.api.gesuch.type.Gesuchstatus status,@QueryParam("bearbeiter")   String bearbeiter,@QueryParam("letzteAktivitaetFrom")   LocalDate letzteAktivitaetFrom,@QueryParam("letzteAktivitaetTo")   LocalDate letzteAktivitaetTo,@QueryParam("sortColumn")   ch.dvbern.stip.api.gesuch.type.SbDashboardColumn sortColumn,@QueryParam("sortOrder")   ch.dvbern.stip.api.gesuch.type.SortOrder sortOrder);

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
