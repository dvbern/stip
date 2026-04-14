package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.DatenschutzbriefCreateDto;
import ch.dvbern.stip.generated.dto.DatenschutzbriefOverviewDto;
import java.io.File;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import java.util.UUID;
import ch.dvbern.stip.generated.dto.ValidationReportDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/datenschutzbrief")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface DatenschutzbriefResource {

    @POST
    @Path("/{gesuchId}/token")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    FileDownloadTokenDto createAndGetDatenschutzbriefDownloadToken(@PathParam("gesuchId") UUID gesuchId,@Valid DatenschutzbriefCreateDto datenschutzbriefCreateDto);

    @GET
    @Path("/{gesuchId}/all")
    @Produces({ "application/json", "text/plain" })
    List<DatenschutzbriefOverviewDto> getAllDatenschutzbriefs(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/download")
    @Produces({ "application/octet-stream" })
    org.jboss.resteasy.reactive.RestMulti<io.vertx.mutiny.core.buffer.Buffer> getDatenschutzbrief(@QueryParam("token") @NotNull   String token);

    @GET
    @Path("/{gesuchId}/token")
    @Produces({ "application/json", "text/plain" })
    FileDownloadTokenDto getDatenschutzbriefDownloadToken(@PathParam("gesuchId") UUID gesuchId,@QueryParam("datenschutzbriefId") @NotNull   UUID datenschutzbriefId);
}
