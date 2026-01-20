package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.DarlehenDto;
import ch.dvbern.stip.generated.dto.DarlehenGsResponseDto;
import ch.dvbern.stip.generated.dto.DarlehenUpdateGsDto;
import ch.dvbern.stip.generated.dto.DarlehenUpdateSbDto;
import java.io.File;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
import java.time.LocalDate;
import ch.dvbern.stip.generated.dto.NullableDarlehenDokumentDto;
import ch.dvbern.stip.generated.dto.PaginatedSbDarlehenDashboardDto;
import java.util.UUID;
import ch.dvbern.stip.generated.dto.ValidationReportDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/darlehen")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface DarlehenResource {

    @POST
    @Path("/{fallId}")
    @Produces({ "application/json", "text/plain" })
    DarlehenDto createDarlehen(@PathParam("fallId") UUID fallId);

    @POST
    @Path("/dokument/{darlehenId}/{dokumentType}")
    @Consumes({ "multipart/form-data" })
    @Produces({ "text/plain" })
    io.smallrye.mutiny.Uni<Response> createDarlehenDokument(@PathParam("darlehenId") UUID darlehenId,@PathParam("dokumentType") ch.dvbern.stip.api.darlehen.type.DarlehenDokumentType dokumentType,@FormParam(value = "fileUpload")  org.jboss.resteasy.reactive.multipart.FileUpload fileUpload);

    @POST
    @Path("/{darlehenId}/ablehnen")
    @Produces({ "application/json", "text/plain" })
    DarlehenDto darlehenAblehen(@PathParam("darlehenId") UUID darlehenId);

    @POST
    @Path("/{darlehenId}/akzeptieren")
    @Produces({ "application/json", "text/plain" })
    DarlehenDto darlehenAkzeptieren(@PathParam("darlehenId") UUID darlehenId);

    @POST
    @Path("/{darlehenId}/eingeben")
    @Produces({ "application/json", "text/plain" })
    DarlehenDto darlehenEingeben(@PathParam("darlehenId") UUID darlehenId);

    @POST
    @Path("/{darlehenId}/freigeben")
    @Produces({ "application/json", "text/plain" })
    DarlehenDto darlehenFreigeben(@PathParam("darlehenId") UUID darlehenId);

    @PATCH
    @Path("/{darlehenId}/gs")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    DarlehenDto darlehenUpdateGs(@PathParam("darlehenId") UUID darlehenId,@Valid @NotNull DarlehenUpdateGsDto darlehenUpdateGsDto);

    @PATCH
    @Path("/{darlehenId}/sb")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    DarlehenDto darlehenUpdateSb(@PathParam("darlehenId") UUID darlehenId,@Valid @NotNull DarlehenUpdateSbDto darlehenUpdateSbDto);

    @POST
    @Path("/{darlehenId}/zurueckweisen")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    DarlehenDto darlehenZurueckweisen(@PathParam("darlehenId") UUID darlehenId,@Valid @NotNull KommentarDto kommentarDto);

    @DELETE
    @Path("/dokument/{dokumentId}")
    @Produces({ "text/plain" })
    void deleteDarlehenDokument(@PathParam("dokumentId") UUID dokumentId);

    @DELETE
    @Path("/{darlehenId}/gs")
    @Produces({ "application/json", "text/plain" })
    void deleteDarlehenGs(@PathParam("darlehenId") UUID darlehenId);

    @GET
    @Path("/dokument/download")
    @Produces({ "application/octet-stream" })
    org.jboss.resteasy.reactive.RestMulti<io.vertx.mutiny.core.buffer.Buffer> downloadDarlehenDokument(@QueryParam("token") @NotNull   String token);

    @GET
    @Path("/getAllDarlehenGs/{fallId}")
    @Produces({ "application/json", "text/plain" })
    DarlehenGsResponseDto getAllDarlehenGs(@PathParam("fallId") UUID fallId);

    @GET
    @Path("/getAllDarlehenSb/{gesuchId}")
    @Produces({ "application/json", "text/plain" })
    List<DarlehenDto> getAllDarlehenSb(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/dashboard/{getDarlehenSbQueryType}")
    @Produces({ "application/json", "text/plain" })
    PaginatedSbDarlehenDashboardDto getDarlehenDashboardSb(@PathParam("getDarlehenSbQueryType") ch.dvbern.stip.api.darlehen.type.GetDarlehenSbQueryType getDarlehenSbQueryType,@QueryParam("page") @NotNull   Integer page,@QueryParam("pageSize") @NotNull   Integer pageSize,@QueryParam("fallNummer")   String fallNummer,@QueryParam("piaNachname")   String piaNachname,@QueryParam("piaVorname")   String piaVorname,@QueryParam("piaGeburtsdatum")   LocalDate piaGeburtsdatum,@QueryParam("status")   String status,@QueryParam("bearbeiter")   String bearbeiter,@QueryParam("letzteAktivitaetFrom")   LocalDate letzteAktivitaetFrom,@QueryParam("letzteAktivitaetTo")   LocalDate letzteAktivitaetTo,@QueryParam("sortColumn")   ch.dvbern.stip.api.darlehen.type.SbDarlehenDashboardColumn sortColumn,@QueryParam("sortOrder")   ch.dvbern.stip.api.gesuch.type.SortOrder sortOrder);

    @GET
    @Path("/dokument/{darlehenId}/{dokumentType}")
    @Produces({ "application/json", "text/plain" })
    NullableDarlehenDokumentDto getDarlehenDokument(@PathParam("darlehenId") UUID darlehenId,@PathParam("dokumentType") ch.dvbern.stip.api.darlehen.type.DarlehenDokumentType dokumentType);

    @GET
    @Path("/dokument/{dokumentId}/token")
    @Produces({ "application/json", "text/plain" })
    FileDownloadTokenDto getDarlehenDownloadToken(@PathParam("dokumentId") UUID dokumentId);

    @GET
    @Path("/{darlehenId}/gs")
    @Produces({ "application/json", "text/plain" })
    DarlehenDto getDarlehenGs(@PathParam("darlehenId") UUID darlehenId);

    @GET
    @Path("/{darlehenId}/sb")
    @Produces({ "application/json", "text/plain" })
    DarlehenDto getDarlehenSb(@PathParam("darlehenId") UUID darlehenId);
}
