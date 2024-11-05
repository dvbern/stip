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

package ch.dvbern.stip.api.stammdaten.resource;

import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.stammdaten.service.LandService;
import ch.dvbern.stip.generated.api.StammdatenResource;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_READ;

@RequestScoped
@RequiredArgsConstructor
public class StammdatenResourceImpl implements StammdatenResource {

    private final LandService landService;

    @Override
    @AllowAll
    @RolesAllowed(STAMMDATEN_READ)
    public Response getLaender() {
        var laender = landService.getAllLaender();
        return Response.ok(laender).build();
    }
}
