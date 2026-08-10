package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.FallHeaderDto;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/fall/header/{fallId}")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface FallHeaderResource {

    @GET
    @Produces({ "application/json", "text/plain" })
    FallHeaderDto getFallHeader(@PathParam("fallId") UUID fallId);
}
