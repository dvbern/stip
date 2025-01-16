package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.AusgewaehlterGrundDto;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.FallDashboardItemDto;
import java.io.File;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchInfoDto;
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
    @Path("/{gesuchTrancheId}/bearbeitungAbschliessen")
    @Produces({ "application/json", "text/plain" })
    GesuchDto bearbeitungAbschliessen(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @POST
    @Path("/status/bereit-fuer-bearbeitung/{gesuchTrancheId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchDto changeGesuchStatusToBereitFuerBearbeitung(@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@Valid KommentarDto kommentarDto);

    @POST
    @Path("/status/in-bearbeitung/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchDto changeGesuchStatusToInBearbeitung(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @POST
    @Path("/status/negative-verfuegung/{gesuchTrancheId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchDto changeGesuchStatusToNegativeVerfuegung(@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@Valid @NotNull AusgewaehlterGrundDto ausgewaehlterGrundDto);

    @POST
    @Path("/status/verfuegt/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchDto changeGesuchStatusToVerfuegt(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @POST
    @Path("/status/versendet/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchDto changeGesuchStatusToVersendet(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    UUID createGesuch(@Valid @NotNull GesuchCreateDto gesuchCreateDto);

    @DELETE
    @Path("/{gesuchId}")
    @Produces({ "text/plain" })
    void deleteGesuch(@PathParam("gesuchId") UUID gesuchId);

    @PATCH
    @Path("/{gesuchTrancheId}/einreichen")
    @Produces({ "application/json", "text/plain" })
    GesuchDto gesuchEinreichen(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @PATCH
    @Path("/{gesuchTrancheId}/fehlendeDokumente")
    @Produces({ "application/json", "text/plain" })
    GesuchDto gesuchFehlendeDokumenteUebermitteln(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @PATCH
    @Path("/{gesuchTrancheId}/fehlendeDokumenteEinreichen")
    @Produces({ "application/json", "text/plain" })
    GesuchDto gesuchTrancheFehlendeDokumenteEinreichen(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @PATCH
    @Path("/{gesuchTrancheId}/gesuchZurueckweisen")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchDto gesuchZurueckweisen(@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@Valid KommentarDto kommentarDto);

    @GET
    @Path("/{gesuchId}/berechnung")
    @Produces({ "application/json", "text/plain" })
    BerechnungsresultatDto getBerechnungForGesuch(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/{gesuchId}/berechnungsblatt")
    @Produces({ "application/octet-stream", "application/json", "text/plain" })
    io.smallrye.mutiny.Uni<Response> getBerechnungsBlattForGesuch(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/{gesuchId}/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchDto getGesuch(@PathParam("gesuchId") UUID gesuchId,@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/{gesuchId}/info")
    @Produces({ "application/json", "text/plain" })
    GesuchInfoDto getGesuchInfo(@PathParam("gesuchId") UUID gesuchId);

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
    @Path("/changes/{gesuchId}")
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto getInitialTrancheChangesByGesuchId(@PathParam("gesuchId") UUID gesuchId);

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
