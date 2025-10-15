package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.AdminDokumenteDto;
import ch.dvbern.stip.generated.dto.AusgewaehlterGrundDto;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.BeschwerdeVerlaufEntryCreateDto;
import ch.dvbern.stip.generated.dto.BeschwerdeVerlaufEntryDto;
import ch.dvbern.stip.generated.dto.EinreichedatumAendernRequestDto;
import ch.dvbern.stip.generated.dto.EinreichedatumStatusDto;
import ch.dvbern.stip.generated.dto.FallDashboardItemDto;
import java.io.File;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchCreateResponseDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchInfoDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDto;
import ch.dvbern.stip.generated.dto.GesuchZurueckweisenResponseDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
import java.time.LocalDate;
import ch.dvbern.stip.generated.dto.NachfristAendernRequestDto;
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
    GesuchWithChangesDto bearbeitungAbschliessen(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/{gesuchId}/einreichedatum")
    @Produces({ "application/json", "text/plain" })
    EinreichedatumStatusDto canEinreichedatumAendern(@PathParam("gesuchId") UUID gesuchId);

    @POST
    @Path("/status/bereit-fuer-bearbeitung/{gesuchTrancheId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto changeGesuchStatusToBereitFuerBearbeitung(@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@Valid KommentarDto kommentarDto);

    @POST
    @Path("/status/datenschutzbrief-druckbereit/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto changeGesuchStatusToDatenschutzbriefDruckbereit(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @POST
    @Path("/status/in-bearbeitung/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto changeGesuchStatusToInBearbeitung(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

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
    @Path("/status/unterschriftenblatt-erhalten/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchDto changeGesuchStatusToVersandbereit(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @POST
    @Path("/status/versendet/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchDto changeGesuchStatusToVersendet(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @POST
    @Path("/{gesuchId}/beschwerde-entscheid")
    @Consumes({ "multipart/form-data" })
    @Produces({ "text/plain" })
    io.smallrye.mutiny.Uni<Response> createBeschwerdeEntscheid(@PathParam("gesuchId") UUID gesuchId,@FormParam(value = "kommentar")  String kommentar,@FormParam(value = "beschwerdeErfolgreich")  Boolean beschwerdeErfolgreich,@FormParam(value = "fileUpload")  org.jboss.resteasy.reactive.multipart.FileUpload fileUpload);

    @POST
    @Path("/{gesuchId}/beschwerde")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    BeschwerdeVerlaufEntryDto createBeschwerdeVerlaufEntry(@PathParam("gesuchId") UUID gesuchId,@Valid BeschwerdeVerlaufEntryCreateDto beschwerdeVerlaufEntryCreateDto);

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchCreateResponseDto createGesuch(@Valid @NotNull GesuchCreateDto gesuchCreateDto);

    @POST
    @Path("/{gesuchTrancheId}/manuelle-verfuegung")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto createManuelleVerfuegung(@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@FormParam(value = "fileUpload")  org.jboss.resteasy.reactive.multipart.FileUpload fileUpload,@FormParam(value = "kommentar")  String kommentar);

    @DELETE
    @Path("/{gesuchId}")
    @Produces({ "text/plain" })
    void deleteGesuch(@PathParam("gesuchId") UUID gesuchId);

    @PATCH
    @Path("/{gesuchId}/einreichedatum")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchDto einreichedatumManuellAendern(@PathParam("gesuchId") UUID gesuchId,@Valid EinreichedatumAendernRequestDto einreichedatumAendernRequestDto);

    @PATCH
    @Path("/{gesuchTrancheId}/einreichen/gs")
    @Produces({ "application/json", "text/plain" })
    GesuchDto gesuchEinreichenGs(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @PATCH
    @Path("/{gesuchTrancheId}/fehlendeDokumente")
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto gesuchFehlendeDokumenteUebermitteln(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @PATCH
    @Path("/{gesuchTrancheId}/pruefen/jur")
    @Produces({ "application/json", "text/plain" })
    GesuchDto gesuchManuellPruefenJur(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @PATCH
    @Path("/{gesuchTrancheId}/pruefen/sb")
    @Produces({ "application/json", "text/plain" })
    GesuchDto gesuchManuellPruefenSB(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @PATCH
    @Path("/{gesuchTrancheId}/fehlendeDokumenteEinreichen")
    @Produces({ "application/json", "text/plain" })
    GesuchDto gesuchTrancheFehlendeDokumenteEinreichen(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @PATCH
    @Path("/{gesuchTrancheId}/gesuchZurueckweisen")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchZurueckweisenResponseDto gesuchZurueckweisen(@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@Valid KommentarDto kommentarDto);

    @GET
    @Path("/{gesuchId}/adminDokumente")
    @Produces({ "application/json", "text/plain" })
    AdminDokumenteDto getAdminDokumente(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/{gesuchId}/beschwerde")
    @Produces({ "application/json", "text/plain" })
    List<BeschwerdeVerlaufEntryDto> getAllBeschwerdeVerlaufEntrys(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/{gesuchId}/berechnung")
    @Produces({ "application/json", "text/plain" })
    BerechnungsresultatDto getBerechnungForGesuch(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/berechnungsblatt")
    @Produces({ "application/octet-stream" })
    org.jboss.resteasy.reactive.RestMulti<io.vertx.mutiny.core.buffer.Buffer> getBerechnungsBlattForGesuch(@QueryParam("token") @NotNull   String token);

    @GET
    @Path("/{gesuchId}/berechnungsblatt/token")
    @Produces({ "application/json", "text/plain" })
    FileDownloadTokenDto getBerechnungsblattDownloadToken(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/gs/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchDto getGesuchGS(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/{gesuchId}/info")
    @Produces({ "application/json", "text/plain" })
    GesuchInfoDto getGesuchInfo(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/sb/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto getGesuchSB(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

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
    FallDashboardItemDto getGsDashboard();

    @GET
    @Path("/changes/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto getInitialTrancheChanges(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/{aenderungId}/aenderung/sb/changes")
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto getSbAenderungChanges(@PathParam("aenderungId") UUID aenderungId,@QueryParam("revision")   Integer revision);

    @GET
    @Path("/benutzer/me/sozialdienst-mitarbeiter-dashboard/{fallId}")
    @Produces({ "application/json", "text/plain" })
    FallDashboardItemDto getSozialdienstMitarbeiterDashboard(@PathParam("fallId") UUID fallId);

    @GET
    @Path("/{gesuchId}/statusprotokoll")
    @Produces({ "application/json", "text/plain" })
    List<StatusprotokollEntryDto> getStatusProtokoll(@PathParam("gesuchId") UUID gesuchId);

    @PATCH
    @Path("/{gesuchTrancheId}/set-gesuchsperiode")
    @Produces({ "application/json", "text/plain" })
    GesuchDto setGesuchsperiodeForGesuch(@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@QueryParam("gesuchsperiodeId") @NotNull   UUID gesuchsperiodeId);

    @PATCH
    @Path("/{gesuchId}/gs")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    void updateGesuchGS(@PathParam("gesuchId") UUID gesuchId,@Valid @NotNull GesuchUpdateDto gesuchUpdateDto);

    @PATCH
    @Path("/{gesuchId}/sb")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    void updateGesuchSB(@PathParam("gesuchId") UUID gesuchId,@Valid @NotNull GesuchUpdateDto gesuchUpdateDto);

    @PATCH
    @Path("/{gesuchId}/nachfristDokumente")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    void updateNachfristDokumente(@PathParam("gesuchId") UUID gesuchId,@Valid NachfristAendernRequestDto nachfristAendernRequestDto);
}
