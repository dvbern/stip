package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.AusgewaehlterGrundDto;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.BeschwerdeEntscheidDto;
import ch.dvbern.stip.generated.dto.BeschwerdeVerlaufEntryCreateDto;
import ch.dvbern.stip.generated.dto.BeschwerdeVerlaufEntryDto;
import ch.dvbern.stip.generated.dto.EinreichedatumAendernRequestDto;
import ch.dvbern.stip.generated.dto.EinreichedatumStatusDto;
import ch.dvbern.stip.generated.dto.FallDashboardItemDto;
import java.io.File;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
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
    @Path("/{gesuchId}/beschwerdeentscheid")
    @Consumes({ "multipart/form-data" })
    @Produces({ "text/plain" })
    io.smallrye.mutiny.Uni<Response> createBeschwerdeEntscheid(@PathParam("gesuchId") UUID gesuchId,@FormParam(value = "kommentar")  String kommentar,@FormParam(value = "fileUpload")  org.jboss.resteasy.reactive.multipart.FileUpload fileUpload,@FormParam(value = "isBeschwerdeErfolgreich")  Boolean isBeschwerdeErfolgreich);

    @POST
    @Path("/{gesuchId}/beschwerde")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    BeschwerdeVerlaufEntryDto createBeschwerdeVerlaufEntry(@PathParam("gesuchId") UUID gesuchId,@Valid BeschwerdeVerlaufEntryCreateDto beschwerdeVerlaufEntryCreateDto);

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    UUID createGesuch(@Valid @NotNull GesuchCreateDto gesuchCreateDto);

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
    @Path("/{gesuchTrancheId}/einreichen")
    @Produces({ "application/json", "text/plain" })
    GesuchDto gesuchEinreichen(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @PATCH
    @Path("/{gesuchTrancheId}/fehlendeDokumente")
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto gesuchFehlendeDokumenteUebermitteln(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

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
    @Path("/{gesuchId}/beschwerde")
    @Produces({ "application/json", "text/plain" })
    List<BeschwerdeVerlaufEntryDto> getAllBeschwerdeVerlaufEntrys(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/{gesuchId}/beschwerdeentscheid")
    @Produces({ "application/json", "text/plain" })
    List<BeschwerdeEntscheidDto> getAllBeschwerdeentscheideForGesuch(@PathParam("gesuchId") UUID gesuchId);

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

    @PATCH
    @Path("/{gesuchId}/nachfristDokumente")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    void updateNachfristDokumente(@PathParam("gesuchId") UUID gesuchId,@Valid NachfristAendernRequestDto nachfristAendernRequestDto);
}
