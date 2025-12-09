package ch.dvbern.stip.generated.api;

import java.io.File;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import java.util.UUID;
import ch.dvbern.stip.generated.dto.VerfuegungDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/verfuegung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface VerfuegungResource {

    @GET
    @Path("/dokument/download")
    @Produces({ "application/octet-stream" })
    org.jboss.resteasy.reactive.RestMulti<io.vertx.mutiny.core.buffer.Buffer> getVerfuegungDokument(@QueryParam("token") @NotNull   String token);

    @GET
    @Path("/dokument/{verfuegungDokumentId}/token")
    @Produces({ "application/json", "text/plain" })
    FileDownloadTokenDto getVerfuegungDokumentDownloadToken(@PathParam("verfuegungDokumentId") UUID verfuegungDokumentId);

    @GET
    @Path("/{gesuchId}/verfuegungen")
    @Produces({ "application/json", "text/plain" })
    List<VerfuegungDto> getVerfuegungen(@PathParam("gesuchId") UUID gesuchId);
}
