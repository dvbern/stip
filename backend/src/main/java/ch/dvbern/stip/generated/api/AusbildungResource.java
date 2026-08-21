package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.AusbildungCreateResponseDto;
import ch.dvbern.stip.generated.dto.AusbildungDto;
import ch.dvbern.stip.generated.dto.AusbildungUnterbruchAntragSBDto;
import ch.dvbern.stip.generated.dto.AusbildungUnterbruchDashboardSBDto;
import ch.dvbern.stip.generated.dto.AusbildungUnterbruchLimitsDto;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import java.io.File;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import java.time.LocalDate;
import java.util.UUID;
import ch.dvbern.stip.generated.dto.UpdateAusbildungUnterbruchAntragSBDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/ausbildung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface AusbildungResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    AusbildungCreateResponseDto createAusbildung(@Valid @NotNull AusbildungUpdateDto ausbildungUpdateDto);

    @POST
    @Path("/create-unterbruch/{ausbildungId}/gs")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json", "text/plain" })
    io.smallrye.mutiny.Uni<Response> createAusbildungUnterbruchAntragGs(@PathParam("ausbildungId") UUID ausbildungId,@FormParam(value = "kommentarGS")  String kommentarGS,@FormParam(value = "fileUpload")  org.jboss.resteasy.reactive.multipart.FileUpload fileUpload,@FormParam(value = "startDate")  LocalDate startDate,@FormParam(value = "endDate")  LocalDate endDate);

    @POST
    @Path("/create-unterbruch/{ausbildungId}/sb")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json", "text/plain" })
    io.smallrye.mutiny.Uni<Response> createAusbildungUnterbruchSb(@PathParam("ausbildungId") UUID ausbildungId,@FormParam(value = "kommentarGS")  String kommentarGS,@FormParam(value = "fileUpload")  org.jboss.resteasy.reactive.multipart.FileUpload fileUpload,@FormParam(value = "startDate")  LocalDate startDate,@FormParam(value = "endDate")  LocalDate endDate,@FormParam(value = "status")  ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus status,@FormParam(value = "kommentarSB")  String kommentarSB,@FormParam(value = "monateOhneAnspruch")  Integer monateOhneAnspruch);

    @GET
    @Path("/unterbruch/dokument/download")
    @Produces({ "application/octet-stream" })
    org.jboss.resteasy.reactive.RestMulti<io.vertx.mutiny.core.buffer.Buffer> downloadAusbildungUnterbruchAntragDokument(@QueryParam("token") @NotNull   String token);

    @GET
    @Path("/{ausbildungId}")
    @Produces({ "application/json", "text/plain" })
    AusbildungDto getAusbildung(@PathParam("ausbildungId") UUID ausbildungId);

    @GET
    @Path("/unterbruch/dokument/{dokumentId}")
    @Produces({ "application/json", "text/plain" })
    FileDownloadTokenDto getAusbildungUnterbruchAntragDokumentDownloadToken(@PathParam("dokumentId") UUID dokumentId);

    @GET
    @Path("/unterbruch/{gesuchId}/all")
    @Produces({ "application/json", "text/plain" })
    AusbildungUnterbruchDashboardSBDto getAusbildungUnterbruchAntragsByGesuchId(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/unterbruch-limits/{ausbildungId}")
    @Produces({ "application/json", "text/plain" })
    AusbildungUnterbruchLimitsDto getAusbildungUnterbruchLimits(@PathParam("ausbildungId") UUID ausbildungId);

    @PATCH
    @Path("/{ausbildungId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    AusbildungDto updateAusbildung(@PathParam("ausbildungId") UUID ausbildungId,@Valid @NotNull AusbildungUpdateDto ausbildungUpdateDto);

    @PATCH
    @Path("/unterbruch/{ausbildungUnterbruchAntragId}/sb")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    AusbildungUnterbruchAntragSBDto updateAusbildungUnterbruchAntragSB(@PathParam("ausbildungUnterbruchAntragId") UUID ausbildungUnterbruchAntragId,@Valid @NotNull UpdateAusbildungUnterbruchAntragSBDto updateAusbildungUnterbruchAntragSBDto);
}
