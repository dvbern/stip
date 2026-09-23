package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.AusgewaehlterGrundDto;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.BeschwerdeVerlaufEntryCreateDto;
import ch.dvbern.stip.generated.dto.BeschwerdeVerlaufEntryDto;
import ch.dvbern.stip.generated.dto.EinreichedatumAendernRequestDto;
import ch.dvbern.stip.generated.dto.EinreichedatumStatusDto;
import ch.dvbern.stip.generated.dto.FallDashboardItemDto;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchCreateResponseDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchHeaderDto;
import ch.dvbern.stip.generated.dto.GesuchInfoDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDto;
import ch.dvbern.stip.generated.dto.GesuchZurueckweisenResponseDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
import java.time.LocalDate;
import ch.dvbern.stip.generated.dto.NachfristAendernRequestDto;
import ch.dvbern.stip.generated.dto.PaginatedSbGesucheDashboardDto;
import ch.dvbern.stip.generated.dto.StatusprotokollEntryDto;
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
public interface GesuchResource {

    @PATCH
    @Path("/gesuch/{gesuchTrancheId}/bearbeitungAbschliessen")
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto bearbeitungAbschliessen(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/gesuch/{gesuchId}/einreichedatum")
    @Produces({ "application/json", "text/plain" })
    EinreichedatumStatusDto canEinreichedatumAendern(@PathParam("gesuchId") UUID gesuchId);

    @POST
    @Path("/gesuch/status/bearbeitung-as-aenderung/{gesuchTrancheId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto changeGesuchStatusToBearbeitungAsAenderung(@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@Valid @NotNull KommentarDto kommentarDto);

    @POST
    @Path("/gesuch/status/bereit-fuer-bearbeitung/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto changeGesuchStatusToBereitFuerBearbeitung(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @POST
    @Path("/gesuch/status/datenschutzbrief-druckbereit/{gesuchTrancheId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto changeGesuchStatusToDatenschutzbriefDruckbereit(@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@Valid @NotNull KommentarDto kommentarDto);

    @POST
    @Path("/gesuch/status/in-bearbeitung/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto changeGesuchStatusToInBearbeitung(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @POST
    @Path("/gesuch/status/negative-verfuegung/{gesuchTrancheId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchDto changeGesuchStatusToNegativeVerfuegung(@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@Valid @NotNull AusgewaehlterGrundDto ausgewaehlterGrundDto);

    @POST
    @Path("/gesuch/status/verfuegt/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchDto changeGesuchStatusToVerfuegt(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @POST
    @Path("/gesuch/status/unterschriftenblatt-erhalten/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchDto changeGesuchStatusToVerfuegungDruckbereit(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @POST
    @Path("/gesuch/status/versendet/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchDto changeGesuchStatusToVersendet(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @POST
    @Path("/gesuch/{gesuchId}/beschwerde-entscheid")
    @Consumes({ "multipart/form-data" })
    @Produces({ "text/plain" })
    io.smallrye.mutiny.Uni<Response> createBeschwerdeEntscheid(@PathParam("gesuchId") UUID gesuchId,@FormParam(value = "kommentar")  String kommentar,@FormParam(value = "beschwerdeErfolgreich")  Boolean beschwerdeErfolgreich,@FormParam(value = "fileUpload")  org.jboss.resteasy.reactive.multipart.FileUpload fileUpload);

    @POST
    @Path("/gesuch/{gesuchId}/beschwerde")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    BeschwerdeVerlaufEntryDto createBeschwerdeVerlaufEntry(@PathParam("gesuchId") UUID gesuchId,@Valid BeschwerdeVerlaufEntryCreateDto beschwerdeVerlaufEntryCreateDto);

    @POST
    @Path("/gesuch")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchCreateResponseDto createGesuch(@Valid @NotNull GesuchCreateDto gesuchCreateDto);

    @POST
    @Path("/gesuch/{gesuchTrancheId}/manuelle-verfuegung")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto createManuelleVerfuegung(@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@FormParam(value = "fileUpload")  org.jboss.resteasy.reactive.multipart.FileUpload fileUpload,@FormParam(value = "kommentar")  String kommentar);

    @DELETE
    @Path("/gesuch/{gesuchId}")
    @Produces({ "text/plain" })
    void deleteGesuch(@PathParam("gesuchId") UUID gesuchId);

    @PATCH
    @Path("/gesuch/{gesuchId}/einreichedatum")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchDto einreichedatumManuellAendern(@PathParam("gesuchId") UUID gesuchId,@Valid EinreichedatumAendernRequestDto einreichedatumAendernRequestDto);

    @PATCH
    @Path("/gesuch/{gesuchTrancheId}/einreichen/gs")
    @Produces({ "application/json", "text/plain" })
    GesuchDto gesuchEinreichenGs(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @PATCH
    @Path("/gesuch/{gesuchTrancheId}/fehlendeDokumente")
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto gesuchFehlendeDokumenteUebermitteln(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @PATCH
    @Path("/gesuch/{gesuchTrancheId}/pruefen/jur")
    @Produces({ "application/json", "text/plain" })
    GesuchDto gesuchManuellPruefenJur(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @PATCH
    @Path("/gesuch/{gesuchTrancheId}/pruefen/sb")
    @Produces({ "application/json", "text/plain" })
    GesuchDto gesuchManuellPruefenSB(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @PATCH
    @Path("/gesuch/{gesuchTrancheId}/fehlendeDokumenteEinreichen")
    @Produces({ "application/json", "text/plain" })
    GesuchDto gesuchTrancheFehlendeDokumenteEinreichen(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @PATCH
    @Path("/gesuch/{gesuchTrancheId}/gesuchZurueckweisenAenderungUndo")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchZurueckweisenResponseDto gesuchZurueckweisenAenderungUndo(@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@Valid KommentarDto kommentarDto);

    @GET
    @Path("/gesuch/{aenderungId}/aenderung/gs/changes")
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto getAenderungChangesGs(@PathParam("aenderungId") UUID aenderungId,@QueryParam("revision")   Integer revision);

    @GET
    @Path("/gesuch/{aenderungId}/aenderung/sb/changes")
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto getAenderungChangesSb(@PathParam("aenderungId") UUID aenderungId,@QueryParam("revision")   Integer revision);

    @GET
    @Path("/gesuch/{gesuchId}/beschwerde")
    @Produces({ "application/json", "text/plain" })
    List<BeschwerdeVerlaufEntryDto> getAllBeschwerdeVerlaufEntrys(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/gesuch/{gesuchId}/berechnung/sb")
    @Produces({ "application/json", "text/plain" })
    BerechnungsresultatDto getBerechnungForGesuchSb(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/gesuch/berechnung/{verfuegungId}/gs")
    @Produces({ "application/json", "text/plain" })
    BerechnungsresultatDto getBerechnungForVerfuegungGs(@PathParam("verfuegungId") UUID verfuegungId);

    @GET
    @Path("/gesuch/berechnung/{verfuegungId}/sb")
    @Produces({ "application/json", "text/plain" })
    BerechnungsresultatDto getBerechnungForVerfuegungSb(@PathParam("verfuegungId") UUID verfuegungId);

    @GET
    @Path("/gesuch/{gesuchId}/berechnungsblatt/token")
    @Produces({ "application/json", "text/plain" })
    FileDownloadTokenDto getBerechnungsblattDownloadToken(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/gesuch/eingereicht/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchDto getEingereichtTranche(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/gesuch/gs/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchDto getGesuchGS(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/gesuch/gs/header/{gesuchId}")
    @Produces({ "application/json", "text/plain" })
    GesuchHeaderDto getGesuchHeaderGs(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/gesuch/sb/header/{gesuchId}")
    @Produces({ "application/json", "text/plain" })
    GesuchHeaderDto getGesuchHeaderSb(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/gesuch/{gesuchId}/info/gs")
    @Produces({ "application/json", "text/plain" })
    GesuchInfoDto getGesuchInfoGs(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/gesuch/{gesuchId}/info/sb")
    @Produces({ "application/json", "text/plain" })
    GesuchInfoDto getGesuchInfoSb(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/gesuch/sb/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto getGesuchSB(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/gesuch/benutzer/me/gs")
    @Produces({ "application/json", "text/plain" })
    List<GesuchDto> getGesucheGs();

    @GET
    @Path("/gesuch/benutzer/me/sb/{getGesucheSBQueryType}")
    @Produces({ "application/json", "text/plain" })
    PaginatedSbGesucheDashboardDto getGesucheSb(@PathParam("getGesucheSBQueryType") ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType getGesucheSBQueryType,@QueryParam("typ") @NotNull   ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp typ,@QueryParam("page") @NotNull   Integer page,@QueryParam("pageSize") @NotNull   Integer pageSize,@QueryParam("bearbeitbar")   Boolean bearbeitbar,@QueryParam("zugewiesen")   Boolean zugewiesen,@QueryParam("fallNummer")   String fallNummer,@QueryParam("piaNachname")   String piaNachname,@QueryParam("piaVorname")   String piaVorname,@QueryParam("piaGeburtsdatum")   LocalDate piaGeburtsdatum,@QueryParam("status")   String status,@QueryParam("bearbeiter")   String bearbeiter,@QueryParam("letzteAktivitaetFrom")   LocalDate letzteAktivitaetFrom,@QueryParam("letzteAktivitaetTo")   LocalDate letzteAktivitaetTo,@QueryParam("sortColumn")   ch.dvbern.stip.api.gesuch.type.SbGesucheDashboardColumn sortColumn,@QueryParam("sortOrder")   ch.dvbern.stip.api.gesuch.type.SortOrder sortOrder);

    @GET
    @Path("/gesuch/benutzer/me/gs-dashboard")
    @Produces({ "application/json", "text/plain" })
    FallDashboardItemDto getGsDashboard();

    @GET
    @Path("/gesuch/changes/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto getInitialTrancheChanges(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/gesuch/benutzer/me/sozialdienst-mitarbeiter-dashboard/{fallId}")
    @Produces({ "application/json", "text/plain" })
    FallDashboardItemDto getSozialdienstMitarbeiterDashboard(@PathParam("fallId") UUID fallId);

    @GET
    @Path("/gesuch/{gesuchId}/statusprotokoll")
    @Produces({ "application/json", "text/plain" })
    List<StatusprotokollEntryDto> getStatusProtokoll(@PathParam("gesuchId") UUID gesuchId);

    @PATCH
    @Path("/gesuch/{gesuchTrancheId}/set-gesuchsperiode")
    @Produces({ "application/json", "text/plain" })
    GesuchDto setGesuchsperiodeForGesuch(@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@QueryParam("gesuchsperiodeId") @NotNull   UUID gesuchsperiodeId);

    @PATCH
    @Path("/gesuch/{gesuchId}/gs")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    void updateGesuchGS(@PathParam("gesuchId") UUID gesuchId,@Valid @NotNull GesuchUpdateDto gesuchUpdateDto);

    @PATCH
    @Path("/gesuch/{gesuchId}/sb")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    void updateGesuchSB(@PathParam("gesuchId") UUID gesuchId,@Valid @NotNull GesuchUpdateDto gesuchUpdateDto);

    @PATCH
    @Path("/gesuch/{gesuchId}/nachfristDokumente")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    void updateNachfristDokumente(@PathParam("gesuchId") UUID gesuchId,@Valid NachfristAendernRequestDto nachfristAendernRequestDto);
}
