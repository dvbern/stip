package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.NotificationDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/notifications")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface NotificationResource {

    @GET
    @Produces({ "application/json", "text/plain" })
    Response getNotifications();
}
