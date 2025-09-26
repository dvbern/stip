package ch.dvbern.stip.generated.api;

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

    @GET
    @Path("/download")
    @Produces({ "application/octet-stream" })
    org.jboss.resteasy.reactive.RestMulti<java.io.ByteArrayOutputStream> getDatenschutzbrief(@QueryParam("token") @NotNull   String token);

    @GET
    @Path("/{elternId}/token")
    @Produces({ "application/json", "text/plain" })
    FileDownloadTokenDto getDatenschutzbriefDownloadToken(@PathParam("elternId") UUID elternId);
}
