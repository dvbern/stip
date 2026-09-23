package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.NotificationDto;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.ResponseStatus;



import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")
public interface NotificationResource {

    @GET
    @Path("/fall/{fallId}/notifications")
    @Produces({ "application/json", "text/plain" })
    List<NotificationDto> getNotificationsForFall(@PathParam("fallId") UUID fallId);

    @PATCH
    @Path("/fall/notifications/{notificationId}/markAsRead")
    @Produces({ "text/plain" })
    void markNotificationAsRead(@PathParam("notificationId") UUID notificationId);
}
