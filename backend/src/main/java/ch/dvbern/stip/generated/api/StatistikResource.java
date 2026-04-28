package ch.dvbern.stip.generated.api;

import java.io.File;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import ch.dvbern.stip.generated.dto.StatistikDto;
import java.util.UUID;
import ch.dvbern.stip.generated.dto.ValidationReportDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/statistik")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface StatistikResource {

    @POST
    @Produces({ "text/plain" })
    void createStatistikJob(@QueryParam("year") @NotNull   Integer year);

    @GET
    @Produces({ "application/json", "text/plain" })
    List<StatistikDto> getAllStatistiks();

    @GET
    @Path("/download")
    @Produces({ "application/octet-stream", "application/json", "text/plain" })
    org.jboss.resteasy.reactive.RestMulti<io.vertx.mutiny.core.buffer.Buffer> getStatistikDownload(@QueryParam("token") @NotNull   String token);

    @GET
    @Path("/{statistikId}/token")
    @Produces({ "application/json", "text/plain" })
    FileDownloadTokenDto getStatistikDownloadToken(@PathParam("statistikId") UUID statistikId);
}
