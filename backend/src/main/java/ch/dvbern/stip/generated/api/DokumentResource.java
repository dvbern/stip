package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.DokumentDto;
import java.io.File;
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
    @Path("/gesuch/{gesuchId}/dokument/{dokumentTyp}")
    @Consumes({ "multipart/form-data" })
    @Produces({ "text/plain" })
    io.smallrye.mutiny.Uni<Response> createDokument(@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("gesuchId") UUID gesuchId,@FormParam(value = "fileUpload")  org.jboss.resteasy.reactive.multipart.FileUpload fileUpload);

    @DELETE
    @Path("/gesuch/{gesuchId}/dokument/{dokumentTyp}/{dokumentId}")
    @Produces({ "text/plain" })
    Response deleteDokument(@PathParam("dokumentId") UUID dokumentId,@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/dokument/{dokumentId}/download")
    @Produces({ "application/octet-stream" })
    org.jboss.resteasy.reactive.RestMulti<io.vertx.mutiny.core.buffer.Buffer> getDokument(@PathParam("dokumentId") UUID dokumentId,@QueryParam("token") @NotNull   String token);

    @GET
    @Path("/gesuch/{gesuchId}/dokument/{dokumentTyp}/{dokumentId}")
    @Produces({ "text/plain" })
    Response getDokumentDownloadToken(@PathParam("gesuchId") UUID gesuchId,@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("dokumentId") UUID dokumentId);

    @GET
    @Path("/gesuch/{gesuchId}/dokument/{dokumentTyp}")
    @Produces({ "application/json", "text/plain" })
    Response getDokumenteForTyp(@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("gesuchId") UUID gesuchId);
}
