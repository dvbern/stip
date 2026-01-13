package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.ApplyDemoDataResponseDto;
import ch.dvbern.stip.generated.dto.DemoDataListDto;
import java.io.File;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/demo-data")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface DemoDataResource {

    @POST
    @Path("/{id}")
    @Produces({ "application/json", "text/plain" })
    ApplyDemoDataResponseDto applyDemoData(@PathParam("id") String id);

    @POST
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json", "text/plain" })
    DemoDataListDto createNewDemoDataImport(@FormParam(value = "kommentar")  String kommentar,@FormParam(value = "fileUpload")  org.jboss.resteasy.reactive.multipart.FileUpload fileUpload);

    @GET
    @Produces({ "application/json", "text/plain" })
    DemoDataListDto getAllDemoData();

    @GET
    @Path("/dokument/download")
    @Produces({ "application/octet-stream" })
    org.jboss.resteasy.reactive.RestMulti<io.vertx.mutiny.core.buffer.Buffer> getDemoDataDokument(@QueryParam("token") @NotNull   String token);

    @GET
    @Path("/dokument/token")
    @Produces({ "application/json", "text/plain" })
    FileDownloadTokenDto getDemoDataDokumentDownloadToken();
}
