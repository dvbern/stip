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

package ch.dvbern.stip.api.steuerdaten.resource;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.steuerdaten.service.SteuerdatenMapper;
import ch.dvbern.stip.api.steuerdaten.service.SteuerdatenService;
import ch.dvbern.stip.generated.api.SteuerdatenResource;
import ch.dvbern.stip.generated.dto.SteuerdatenDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
@Validated
public class SteuerdatenResourceImpl implements SteuerdatenResource {
    private final SteuerdatenService steuerdatenService;
    private final SteuerdatenMapper steuerdatenMapper;

    @Override
    @RolesAllowed({ OidcConstants.ROLE_SACHBEARBEITER, OidcConstants.ROLE_ADMIN })
    @AllowAll
    public List<SteuerdatenDto> getSteuerdaten(UUID gesuchTrancheId) {
        return steuerdatenService.getSteuerdaten(gesuchTrancheId).stream().map(steuerdatenMapper::toDto).toList();
    }

    @Override
    @RolesAllowed({ OidcConstants.ROLE_SACHBEARBEITER, OidcConstants.ROLE_ADMIN })
    @AllowAll
    public List<SteuerdatenDto> updateSteuerdaten(
        UUID gesuchTrancheId,
        List<SteuerdatenDto> steuerdatenDto
    ) {
        return steuerdatenService.updateSteuerdaten(gesuchTrancheId, steuerdatenDto)
            .stream()
            .map(steuerdatenMapper::toDto)
            .toList();
    }
}
