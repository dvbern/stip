package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.ApplyDemoDataResponseDto;
import ch.dvbern.stip.generated.dto.DemoDataSlimDto;

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

    @GET
    @Produces({ "application/json", "text/plain" })
    DemoDataSlimDto getAllDemoData();
}
