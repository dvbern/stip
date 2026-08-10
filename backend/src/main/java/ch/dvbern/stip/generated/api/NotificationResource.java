package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.NotificationDto;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/fall")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface NotificationResource {

    @GET
    @Path("/{fallId}/notifications")
    @Produces({ "application/json", "text/plain" })
    List<NotificationDto> getNotificationsForFall(@PathParam("fallId") UUID fallId);

    @PATCH
    @Path("/notifications/{notificationId}/markAsRead")
    @Produces({ "text/plain" })
    void markNotificationAsRead(@PathParam("notificationId") UUID notificationId);
}
