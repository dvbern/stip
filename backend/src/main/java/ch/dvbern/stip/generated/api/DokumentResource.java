package ch.dvbern.stip.generated.api;

import java.util.UUID;

import ch.dvbern.stip.generated.dto.GesuchDokumentAblehnenRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;


@Path("")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface DokumentResource {

    @POST
    @Path("/gesuchDokument/{gesuchTrancheId}/{dokumentTyp}")
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
    @Path("/gesuchDokument/{gesuchTrancheId}/{dokumentTyp}/kommentare")
    @Produces({ "application/json", "text/plain" })
    Response getGesuchDokumentKommentare(@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/gesuchDokument/{gesuchTrancheId}/{dokumentTyp}")
    @Produces({ "application/json", "text/plain" })
    Response getGesuchDokumenteForTyp(@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("gesuchTrancheId") UUID gesuchTrancheId);
}
