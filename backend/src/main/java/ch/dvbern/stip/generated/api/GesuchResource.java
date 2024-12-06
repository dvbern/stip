package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.AusgewaehlterGrundDto;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.FallDashboardItemDto;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
import java.time.LocalDate;
import ch.dvbern.stip.generated.dto.PaginatedSbDashboardDto;
import ch.dvbern.stip.generated.dto.StatusprotokollEntryDto;
import java.util.UUID;

import jakarta.ws.rs.*;

import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/gesuch")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface GesuchResource {

    @PATCH
    @Path("/{gesuchId}/bearbeitungAbschliessen")
    @Produces({ "application/json", "text/plain" })
    void bearbeitungAbschliessen(@PathParam("gesuchId") UUID gesuchId);

    @POST
    @Path("/status/bereit-fuer-bearbeitung/{gesuchId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchDto changeGesuchStatusToBereitFuerBearbeitung(@PathParam("gesuchId") UUID gesuchId,@Valid KommentarDto kommentarDto);

    @POST
    @Path("/status/in-bearbeitung/{gesuchId}")
    @Produces({ "application/json", "text/plain" })
    GesuchDto changeGesuchStatusToInBearbeitung(@PathParam("gesuchId") UUID gesuchId);

    @POST
    @Path("/status/negative-verfuegung/{gesuchId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchDto changeGesuchStatusToNegativeVerfuegung(@PathParam("gesuchId") UUID gesuchId,@Valid @NotNull AusgewaehlterGrundDto ausgewaehlterGrundDto);

    @POST
    @Path("/status/verfuegt/{gesuchId}")
    @Produces({ "application/json", "text/plain" })
    GesuchDto changeGesuchStatusToVerfuegt(@PathParam("gesuchId") UUID gesuchId);

    @POST
    @Path("/status/versendet/{gesuchId}")
    @Produces({ "application/json", "text/plain" })
    GesuchDto changeGesuchStatusToVersendet(@PathParam("gesuchId") UUID gesuchId);

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    UUID createGesuch(@Valid @NotNull GesuchCreateDto gesuchCreateDto);

    @DELETE
    @Path("/{gesuchId}")
    @Produces({ "text/plain" })
    void deleteGesuch(@PathParam("gesuchId") UUID gesuchId);

    @PATCH
    @Path("/{gesuchId}/einreichen")
    @Produces({ "application/json", "text/plain" })
    void gesuchEinreichen(@PathParam("gesuchId") UUID gesuchId);

    @PATCH
    @Path("/{gesuchId}/fehlendeDokumente")
    @Produces({ "application/json", "text/plain" })
    void gesuchFehlendeDokumenteUebermitteln(@PathParam("gesuchId") UUID gesuchId);

    @PATCH
    @Path("/{gesuchTrancheId}/fehlendeDokumenteEinreichen")
    @Produces({ "application/json", "text/plain" })
    void gesuchTrancheFehlendeDokumenteEinreichen(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @PATCH
    @Path("/{gesuchId}/gesuchZurueckweisen")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    void gesuchZurueckweisen(@PathParam("gesuchId") UUID gesuchId,@Valid KommentarDto kommentarDto);

    @GET
    @Path("/{gesuchId}/berechnung")
    @Produces({ "application/json", "text/plain" })
    BerechnungsresultatDto getBerechnungForGesuch(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/{gesuchId}/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchDto getGesuch(@PathParam("gesuchId") UUID gesuchId,@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/benutzer/me/gs")
    @Produces({ "application/json", "text/plain" })
    List<GesuchDto> getGesucheGs();

    @GET
    @Path("/benutzer/me/sb/{getGesucheSBQueryType}")
    @Produces({ "application/json", "text/plain" })
    PaginatedSbDashboardDto getGesucheSb(@PathParam("getGesucheSBQueryType") ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType getGesucheSBQueryType,@QueryParam("typ") @NotNull   ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp typ,@QueryParam("page") @NotNull   Integer page,@QueryParam("pageSize") @NotNull   Integer pageSize,@QueryParam("fallNummer")   String fallNummer,@QueryParam("piaNachname")   String piaNachname,@QueryParam("piaVorname")   String piaVorname,@QueryParam("piaGeburtsdatum")   LocalDate piaGeburtsdatum,@QueryParam("status")   String status,@QueryParam("bearbeiter")   String bearbeiter,@QueryParam("letzteAktivitaetFrom")   LocalDate letzteAktivitaetFrom,@QueryParam("letzteAktivitaetTo")   LocalDate letzteAktivitaetTo,@QueryParam("sortColumn")   ch.dvbern.stip.api.gesuch.type.SbDashboardColumn sortColumn,@QueryParam("sortOrder")   ch.dvbern.stip.api.gesuch.type.SortOrder sortOrder);

    @GET
    @Path("/{aenderungId}/aenderung/gs/changes")
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto getGsAenderungChangesInBearbeitung(@PathParam("aenderungId") UUID aenderungId);

    @GET
    @Path("/benutzer/me/gs-dashboard")
    @Produces({ "application/json", "text/plain" })
    List<FallDashboardItemDto> getGsDashboard();

    @GET
    @Path("/changes/{trancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto getInitialTrancheChangesByTrancheId(@PathParam("trancheId") UUID trancheId);

    @GET
    @Path("/{aenderungId}/aenderung/sb/changes")
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto getSbAenderungChanges(@PathParam("aenderungId") UUID aenderungId);

    @GET
    @Path("/{gesuchId}/statusprotokoll")
    @Produces({ "application/json", "text/plain" })
    List<StatusprotokollEntryDto> getStatusProtokoll(@PathParam("gesuchId") UUID gesuchId);

    @PATCH
    @Path("/{gesuchId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    void updateGesuch(@PathParam("gesuchId") UUID gesuchId,@Valid @NotNull GesuchUpdateDto gesuchUpdateDto);
}
