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

package ch.dvbern.stip.api.land.resource;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.LandAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.land.service.LandService;
import ch.dvbern.stip.generated.api.LandResource;
import ch.dvbern.stip.generated.dto.LandDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_UPDATE;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class LandResourceImpl implements LandResource {
    private final LandAuthorizer landAuthorizer;
    private final LandService landService;

    @Override
    @RolesAllowed(STAMMDATEN_CREATE)
    public LandDto createLand(LandDto landDto) {
        landAuthorizer.canCreate();
        return landService.createLand(landDto);
    }

    @Override
    @RolesAllowed(STAMMDATEN_READ)
    public List<LandDto> getLaender() {
        landAuthorizer.canGetLaender();
        return landService.getAllLaender();
    }

    @Override
    @RolesAllowed(STAMMDATEN_UPDATE)
    public LandDto updateLand(UUID landId, LandDto landDto) {
        landAuthorizer.canUpdate();
        return landService.updateLand(landId, landDto);
    }
}
