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

package ch.dvbern.stip.api.fall.resource;

import java.util.List;

import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.fall.service.FallService;
import ch.dvbern.stip.generated.api.FallResource;
import ch.dvbern.stip.generated.dto.FallDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_GESUCHSTELLER;
import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_SACHBEARBEITER;
import static ch.dvbern.stip.api.common.util.OidcPermissions.FALL_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.FALL_READ;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class FallResourceImpl implements FallResource {

    private final FallService fallService;

    @RolesAllowed(FALL_CREATE)
    @Override
    @AllowAll
    public FallDto createFallForGs() {
        return fallService.createFallForGs();
    }

    @RolesAllowed({ FALL_READ, ROLE_SACHBEARBEITER })
    @Override
    @AllowAll
    public List<FallDto> getFaelleForSb() {
        return fallService.findFaelleForSb();
    }

    @RolesAllowed({ FALL_READ, ROLE_GESUCHSTELLER })
    @Override
    @AllowAll
    public FallDto getFallForGs() {
        return fallService.findFallForGs();
    }
}
