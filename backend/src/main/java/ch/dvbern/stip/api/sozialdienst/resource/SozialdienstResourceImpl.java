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

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.common.authorization.SozialdienstAuthorizer;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.generated.api.SozialdienstResource;
import ch.dvbern.stip.generated.dto.SozialdienstAdminCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminUpdateDto;
import ch.dvbern.stip.generated.dto.SozialdienstCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstDto;
import ch.dvbern.stip.generated.dto.SozialdienstUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class SozialdienstResourceImpl implements SozialdienstResource {

    private final SozialdienstService sozialdienstService;

    private final SozialdienstAuthorizer sozialdienstAuthorizer;

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public SozialdienstDto createSozialdienst(SozialdienstCreateDto sozialdienstCreateDto) {
        return sozialdienstService.createSozialdienst(sozialdienstCreateDto);
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public SozialdienstDto deleteSozialdienst(UUID sozialdienstId) {
        return sozialdienstService.deleteSozialdienst(sozialdienstId);
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_GESUCHSTELLER, OidcConstants.ROLE_ADMIN })
    @Override
    public List<SozialdienstDto> getAllSozialdienste() {
        return sozialdienstService.getAllSozialdienst();
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public SozialdienstDto getSozialdienst(UUID sozialdienstId) {
        return sozialdienstService.getSozialdienstById(sozialdienstId);
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public SozialdienstAdminDto replaceSozialdienstAdmin(
        UUID sozialdienstId,
        SozialdienstAdminCreateDto sozialdienstAdminCreateDto
    ) {
        return sozialdienstService.replaceSozialdienstAdmin(sozialdienstId, sozialdienstAdminCreateDto);
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public SozialdienstDto updateSozialdienst(SozialdienstUpdateDto sozialdienstUpdateDto) {
        return sozialdienstService.updateSozialdienst(sozialdienstUpdateDto);
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public SozialdienstAdminDto updateSozialdienstAdmin(
        UUID sozialdienstId,
        SozialdienstAdminUpdateDto sozialdienstAdminUpdateDto
    ) {
        final var sozialdienst = sozialdienstService.getSozialdienstById(sozialdienstId);
        return sozialdienstService.updateSozialdienstAdmin(sozialdienstAdminUpdateDto, sozialdienst);
    }
}
