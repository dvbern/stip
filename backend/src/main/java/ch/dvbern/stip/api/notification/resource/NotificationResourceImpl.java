package ch.dvbern.stip.api.notification.resource;

import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.notification.service.NotificationService;
import ch.dvbern.stip.generated.api.NotificationResource;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_READ;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class NotificationResourceImpl implements NotificationResource {
    private final NotificationService notificationService;

    @RolesAllowed(GESUCH_READ)
    @AllowAll
    @Override
    public Response getNotificationsForCurrentUser() {
        return Response.ok(notificationService.getNotificationsForCurrentUser()).build();
    }
}
