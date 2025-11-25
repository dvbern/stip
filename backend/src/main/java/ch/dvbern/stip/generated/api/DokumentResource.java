package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.CustomDokumentTypCreateDto;
import java.io.File;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentAblehnenRequestDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDto;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDto;
import java.util.UUID;
import ch.dvbern.stip.generated.dto.UnterschriftenblattDokumentDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface DokumentResource {

    @POST
    @Path("/gesuchDokument/customGesuchDokument")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchDokumentDto createCustomDokumentTyp(@Valid @NotNull CustomDokumentTypCreateDto customDokumentTypCreateDto);

    @POST
    @Path("/gesuchDokument/gs/{gesuchTrancheId}/{dokumentTyp}")
    @Consumes({ "multipart/form-data" })
    @Produces({ "text/plain" })
    io.smallrye.mutiny.Uni<Response> createDokumentGS(@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@FormParam(value = "fileUpload")  org.jboss.resteasy.reactive.multipart.FileUpload fileUpload);

    @POST
    @Path("/gesuchDokument/sb/{gesuchTrancheId}/{dokumentTyp}")
    @Consumes({ "multipart/form-data" })
    @Produces({ "text/plain" })
    io.smallrye.mutiny.Uni<Response> createDokumentSB(@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@FormParam(value = "fileUpload")  org.jboss.resteasy.reactive.multipart.FileUpload fileUpload);

    @POST
    @Path("/unterschriftenblatt/{gesuchId}/{unterschriftenblattTyp}")
    @Consumes({ "multipart/form-data" })
    @Produces({ "text/plain" })
    io.smallrye.mutiny.Uni<Response> createUnterschriftenblatt(@PathParam("unterschriftenblattTyp") ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp unterschriftenblattTyp,@PathParam("gesuchId") UUID gesuchId,@FormParam(value = "fileUpload")  org.jboss.resteasy.reactive.multipart.FileUpload fileUpload);

    @DELETE
    @Path("/gesuchDokument/customGesuchDokument/{customDokumentTypId}")
    @Produces({ "text/plain" })
    void deleteCustomDokumentTyp(@PathParam("customDokumentTypId") UUID customDokumentTypId);

    @DELETE
    @Path("/dokument/gs/{dokumentId}")
    @Produces({ "text/plain" })
    void deleteDokumentGS(@PathParam("dokumentId") UUID dokumentId);

    @DELETE
    @Path("/dokument/sb/{dokumentId}")
    @Produces({ "text/plain" })
    void deleteDokumentSB(@PathParam("dokumentId") UUID dokumentId);

    @DELETE
    @Path("/unterschriftenblatt/dokument/{dokumentId}")
    @Produces({ "text/plain" })
    void deleteUnterschriftenblattDokument(@PathParam("dokumentId") UUID dokumentId);

    @PATCH
    @Path("/gesuchDokument/{gesuchDokumentId}/ablehnen")
    @Consumes({ "application/json" })
    @Produces({ "text/plain" })
    void gesuchDokumentAblehnen(@PathParam("gesuchDokumentId") UUID gesuchDokumentId,@Valid GesuchDokumentAblehnenRequestDto gesuchDokumentAblehnenRequestDto);

    @PATCH
    @Path("/gesuchDokument/{gesuchDokumentId}/akzeptieren")
    @Produces({ "text/plain" })
    void gesuchDokumentAkzeptieren(@PathParam("gesuchDokumentId") UUID gesuchDokumentId);

    @GET
    @Path("/customGesuchDokument/gs/{customDokumentTypId}")
    @Produces({ "application/json", "text/plain" })
    NullableGesuchDokumentDto getCustomGesuchDokumentForTypGS(@PathParam("customDokumentTypId") UUID customDokumentTypId);

    @GET
    @Path("/customGesuchDokument/sb/{customDokumentTypId}")
    @Produces({ "application/json", "text/plain" })
    NullableGesuchDokumentDto getCustomGesuchDokumentForTypSB(@PathParam("customDokumentTypId") UUID customDokumentTypId);

    @GET
    @Path("/dokument/{dokumentArt}/download")
    @Produces({ "application/octet-stream" })
    org.jboss.resteasy.reactive.RestMulti<io.vertx.mutiny.core.buffer.Buffer> getDokument(@QueryParam("token") @NotNull   String token,@PathParam("dokumentArt") ch.dvbern.stip.api.dokument.type.DokumentArt dokumentArt);

    @GET
    @Path("/dokument/{dokumentId}")
    @Produces({ "application/json", "text/plain" })
    FileDownloadTokenDto getDokumentDownloadToken(@PathParam("dokumentId") UUID dokumentId);

    @GET
    @Path("/gesuchDokument/gs/{gesuchTrancheId}/{dokumentTyp}")
    @Produces({ "application/json", "text/plain" })
    NullableGesuchDokumentDto getGesuchDokumentForTypGS(@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/gesuchDokument/sb/{gesuchTrancheId}/{dokumentTyp}")
    @Produces({ "application/json", "text/plain" })
    NullableGesuchDokumentDto getGesuchDokumentForTypSB(@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/gesuchDokument/{gesuchDokumentId}/kommentare/gs")
    @Produces({ "application/json", "text/plain" })
    List<GesuchDokumentKommentarDto> getGesuchDokumentKommentareGS(@PathParam("gesuchDokumentId") UUID gesuchDokumentId);

    @GET
    @Path("/gesuchDokument/{gesuchDokumentId}/kommentare/sb")
    @Produces({ "application/json", "text/plain" })
    List<GesuchDokumentKommentarDto> getGesuchDokumentKommentareSB(@PathParam("gesuchDokumentId") UUID gesuchDokumentId);

    @GET
    @Path("/unterschriftenblatt/{gesuchId}")
    @Produces({ "application/json", "text/plain" })
    List<UnterschriftenblattDokumentDto> getUnterschriftenblaetterForGesuch(@PathParam("gesuchId") UUID gesuchId);

    @POST
    @Path("/customGesuchDokument/gs/{customDokumentTypId}")
    @Consumes({ "multipart/form-data" })
    @Produces({ "text/plain" })
    io.smallrye.mutiny.Uni<Response> uploadCustomGesuchDokumentGS(@PathParam("customDokumentTypId") UUID customDokumentTypId,@FormParam(value = "fileUpload")  org.jboss.resteasy.reactive.multipart.FileUpload fileUpload);

    @POST
    @Path("/customGesuchDokument/sb/{customDokumentTypId}")
    @Consumes({ "multipart/form-data" })
    @Produces({ "text/plain" })
    io.smallrye.mutiny.Uni<Response> uploadCustomGesuchDokumentSB(@PathParam("customDokumentTypId") UUID customDokumentTypId,@FormParam(value = "fileUpload")  org.jboss.resteasy.reactive.multipart.FileUpload fileUpload);
}
