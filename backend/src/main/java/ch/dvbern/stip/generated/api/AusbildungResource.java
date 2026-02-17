package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.AusbildungCreateResponseDto;
import ch.dvbern.stip.generated.dto.AusbildungDto;
import ch.dvbern.stip.generated.dto.AusbildungUnterbruchAntragGSDto;
import ch.dvbern.stip.generated.dto.AusbildungUnterbruchAntragSBDto;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import ch.dvbern.stip.generated.dto.DokumentDto;
import java.io.File;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import java.util.UUID;
import ch.dvbern.stip.generated.dto.UpdateAusbildungUnterbruchAntragGSDto;
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
    @Path("/unterbruch/{ausbildungId}")
    @Produces({ "application/json", "text/plain" })
    AusbildungUnterbruchAntragGSDto createAusbildungUnterbruchAntrag(@PathParam("ausbildungId") UUID ausbildungId);

    @POST
    @Path("/unterbruch/{ausbildungUnterbruchAntragId}/dokument")
    @Consumes({ "multipart/form-data" })
    @Produces({ "text/plain" })
    io.smallrye.mutiny.Uni<Response> createAusbildungUnterbruchAntragDokument(@PathParam("ausbildungUnterbruchAntragId") UUID ausbildungUnterbruchAntragId,@FormParam(value = "fileUpload")  org.jboss.resteasy.reactive.multipart.FileUpload fileUpload);

    @DELETE
    @Path("/unterbruch/gs/{ausbildungUnterbruchAntragId}")
    @Produces({ "text/plain" })
    void deleteAusbildungUnterbruchAntrag(@PathParam("ausbildungUnterbruchAntragId") UUID ausbildungUnterbruchAntragId);

    @DELETE
    @Path("/unterbruch/dokument/{dokumentId}")
    @Produces({ "text/plain" })
    void deleteAusbildungUnterbruchAntragDokument(@PathParam("dokumentId") UUID dokumentId);

    @GET
    @Path("/unterbruch/dokument/download")
    @Produces({ "application/octet-stream", "application/json", "text/plain" })
    org.jboss.resteasy.reactive.RestMulti<io.vertx.mutiny.core.buffer.Buffer> downloadAusbildungUnterbruchAntragDokument(@QueryParam("token") @NotNull   String token);

    @PATCH
    @Path("/unterbruch/gs/{ausbildungUnterbruchAntragId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    AusbildungUnterbruchAntragGSDto einreichenAusbildungUnterbruchAntrag(@PathParam("ausbildungUnterbruchAntragId") UUID ausbildungUnterbruchAntragId,@Valid @NotNull UpdateAusbildungUnterbruchAntragGSDto updateAusbildungUnterbruchAntragGSDto);

    @GET
    @Path("/{ausbildungId}")
    @Produces({ "application/json", "text/plain" })
    AusbildungDto getAusbildung(@PathParam("ausbildungId") UUID ausbildungId);

    @GET
    @Path("/unterbruch/dokument/{dokumentId}")
    @Produces({ "application/json", "text/plain" })
    FileDownloadTokenDto getAusbildungUnterbruchAntragDokumentDownloadToken(@PathParam("dokumentId") UUID dokumentId);

    @GET
    @Path("/unterbruch/{ausbildungUnterbruchAntragId}/dokument")
    @Produces({ "application/json", "text/plain" })
    List<DokumentDto> getAusbildungUnterbruchAntragDokuments(@PathParam("ausbildungUnterbruchAntragId") UUID ausbildungUnterbruchAntragId);

    @GET
    @Path("/unterbruch/{gesuchId}/all")
    @Produces({ "application/json", "text/plain" })
    List<AusbildungUnterbruchAntragSBDto> getAusbildungUnterbruchAntragsByGesuchId(@PathParam("gesuchId") UUID gesuchId);

    @PATCH
    @Path("/{ausbildungId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    AusbildungDto updateAusbildung(@PathParam("ausbildungId") UUID ausbildungId,@Valid @NotNull AusbildungUpdateDto ausbildungUpdateDto);

    @PATCH
    @Path("/unterbruch/sb/{ausbildungUnterbruchAntragId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    AusbildungUnterbruchAntragSBDto updateAusbildungUnterbruchAntragSB(@PathParam("ausbildungUnterbruchAntragId") UUID ausbildungUnterbruchAntragId,@Valid @NotNull UpdateAusbildungUnterbruchAntragSBDto updateAusbildungUnterbruchAntragSBDto);
}
