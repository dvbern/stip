package ch.dvbern.stip.generated.api;

import java.io.File;
import ch.dvbern.stip.generated.dto.GesuchDokumentAblehnenRequestDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDto;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDto;
import java.util.UUID;
import ch.dvbern.stip.generated.dto.UnterschriftenblattDokumentDto;

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
    @Path("/gesuchDokument/{gesuchTrancheId}/{dokumentTyp}")
    @Consumes({ "multipart/form-data" })
    @Produces({ "text/plain" })
    io.smallrye.mutiny.Uni<Response> createDokument(@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@FormParam(value = "fileUpload")  org.jboss.resteasy.reactive.multipart.FileUpload fileUpload);

    @POST
    @Path("/unterschriftenblatt/{gesuchId}/{unterschriftenblattTyp}")
    @Consumes({ "multipart/form-data" })
    @Produces({ "text/plain" })
    io.smallrye.mutiny.Uni<Response> createUnterschriftenblatt(@PathParam("unterschriftenblattTyp") ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp unterschriftenblattTyp,@PathParam("gesuchId") UUID gesuchId,@FormParam(value = "fileUpload")  org.jboss.resteasy.reactive.multipart.FileUpload fileUpload);

    @DELETE
    @Path("/dokument/{dokumentId}")
    @Produces({ "text/plain" })
    void deleteDokument(@PathParam("dokumentId") UUID dokumentId);

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
    @Path("/dokument/download")
    @Produces({ "application/octet-stream" })
    org.jboss.resteasy.reactive.RestMulti<io.vertx.mutiny.core.buffer.Buffer> getDokument(@QueryParam("token") @NotNull   String token);

    @GET
    @Path("/dokument/{dokumentId}")
    @Produces({ "text/plain" })
    String getDokumentDownloadToken(@PathParam("dokumentId") UUID dokumentId);

    @GET
    @Path("/gesuchDokument/{gesuchTrancheId}/{dokumentTyp}/kommentare")
    @Produces({ "application/json", "text/plain" })
    List<GesuchDokumentKommentarDto> getGesuchDokumentKommentare(@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/gesuchDokument/{gesuchTrancheId}/{dokumentTyp}")
    @Produces({ "application/json", "text/plain" })
    NullableGesuchDokumentDto getGesuchDokumenteForTyp(@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/unterschriftenblatt/{gesuchId}")
    @Produces({ "application/json", "text/plain" })
    List<UnterschriftenblattDokumentDto> getUnterschriftenblaetterForGesuch(@PathParam("gesuchId") UUID gesuchId);
}
