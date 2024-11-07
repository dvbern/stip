/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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
