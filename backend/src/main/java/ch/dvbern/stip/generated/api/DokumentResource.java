package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.DokumentDto;
import java.io.File;
import ch.dvbern.stip.generated.dto.GesuchDokumentAblehnenRequestDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDto;
import java.util.UUID;

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
    @Path("/dokument/{gesuchTrancheId}/{dokumentTyp}")
    @Consumes({ "multipart/form-data" })
    @Produces({ "text/plain" })
    io.smallrye.mutiny.Uni<Response> createDokument(@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@FormParam(value = "fileUpload")  org.jboss.resteasy.reactive.multipart.FileUpload fileUpload);

    @DELETE
    @Path("/dokument/{gesuchTrancheId}/{dokumentTyp}/{dokumentId}")
    @Produces({ "text/plain" })
    Response deleteDokument(@PathParam("dokumentId") UUID dokumentId,@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @PATCH
    @Path("/gesuchDokument/{gesuchDokumentId}/ablehnen")
    @Consumes({ "application/json" })
    @Produces({ "text/plain" })
    Response gesuchDokumentAblehnen(@PathParam("gesuchDokumentId") UUID gesuchDokumentId,@Valid GesuchDokumentAblehnenRequestDto gesuchDokumentAblehnenRequestDto);

    @PATCH
    @Path("/gesuchDokument/{gesuchDokumentId}/akzeptieren")
    @Produces({ "text/plain" })
    Response gesuchDokumentAkzeptieren(@PathParam("gesuchDokumentId") UUID gesuchDokumentId);

    @GET
    @Path("/dokument/download")
    @Produces({ "application/octet-stream" })
    org.jboss.resteasy.reactive.RestMulti<io.vertx.mutiny.core.buffer.Buffer> getDokument(@QueryParam("token") @NotNull   String token);

    @GET
    @Path("/dokument/{gesuchTrancheId}/{dokumentTyp}/{dokumentId}")
    @Produces({ "text/plain" })
    Response getDokumentDownloadToken(@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("dokumentId") UUID dokumentId);

    @GET
    @Path("/dokument/{gesuchTrancheId}/{dokumentTyp}")
    @Produces({ "application/json", "text/plain" })
    Response getDokumenteForTyp(@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/gesuchDokument/{gesuchTrancheId}/{dokumentTyp}/kommentare")
    @Produces({ "application/json", "text/plain" })
    Response getGesuchDokumentKommentare(@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("gesuchTrancheId") UUID gesuchTrancheId);
}
