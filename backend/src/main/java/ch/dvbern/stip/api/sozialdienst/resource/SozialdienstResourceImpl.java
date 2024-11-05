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

package ch.dvbern.stip.api.sozialdienst.resource;

import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.common.authorization.SozialdienstAuthorizer;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.generated.api.SozialdienstResource;
import ch.dvbern.stip.generated.dto.SozialdienstAdminCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminUpdateDto;
import ch.dvbern.stip.generated.dto.SozialdienstCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class SozialdienstResourceImpl implements SozialdienstResource {

    private final SozialdienstService sozialdienstService;

    private final SozialdienstAuthorizer sozialdienstAuthorizer;

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public Response createSozialdienst(SozialdienstCreateDto sozialdienstCreateDto) {
        final var sozialdienst = sozialdienstService.createSozialdienst(sozialdienstCreateDto);
        return Response.ok(sozialdienst).build();
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public Response deleteSozialdienst(UUID sozialdienstId) {
        final var sozialdienst = sozialdienstService.deleteSozialdienst(sozialdienstId);
        return Response.ok().entity(sozialdienst).build();
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public Response getAllSozialdienste() {
        final var sozialdienste = sozialdienstService.getAllSozialdienst();
        return Response.ok().entity(sozialdienste).build();
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public Response getSozialdienst(UUID sozialdienstId) {
        final var sozialdienst = sozialdienstService.getSozialdienstById(sozialdienstId);
        return Response.ok().entity(sozialdienst).build();
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public Response replaceSozialdienstAdmin(
        UUID sozialdienstId,
        SozialdienstAdminCreateDto sozialdienstAdminCreateDto
    ) {
        final var updated = sozialdienstService.replaceSozialdienstAdmin(sozialdienstId, sozialdienstAdminCreateDto);
        return Response.ok().entity(updated).build();
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public Response updateSozialdienst(SozialdienstUpdateDto sozialdienstUpdateDto) {
        final var updated = sozialdienstService.updateSozialdienst(sozialdienstUpdateDto);
        return Response.ok().entity(updated).build();
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public Response updateSozialdienstAdmin(
        UUID sozialdienstId,
        SozialdienstAdminUpdateDto sozialdienstAdminUpdateDto
    ) {
        final var sozialdienst = sozialdienstService.getSozialdienstById(sozialdienstId);
        final var updated = sozialdienstService.updateSozialdienstAdmin(sozialdienstAdminUpdateDto, sozialdienst);
        return Response.ok().entity(updated).build();
    }
}
