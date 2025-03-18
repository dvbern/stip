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

import ch.dvbern.stip.api.common.authorization.SteuerdatenAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.steuerdaten.service.SteuerdatenMapper;
import ch.dvbern.stip.api.steuerdaten.service.SteuerdatenService;
import ch.dvbern.stip.generated.api.SteuerdatenResource;
import ch.dvbern.stip.generated.dto.NeskoGetSteuerdatenRequestDto;
import ch.dvbern.stip.generated.dto.SteuerdatenDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ch.dvbern.stip.api.common.util.OidcPermissions.SB_GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SB_GESUCH_UPDATE;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
@Validated
public class SteuerdatenResourceImpl implements SteuerdatenResource {
    private final SteuerdatenService steuerdatenService;
    private final SteuerdatenMapper steuerdatenMapper;
    private final SteuerdatenAuthorizer steuerdatenAuthorizer;

    @Override
    @RolesAllowed(SB_GESUCH_READ)
    public List<SteuerdatenDto> getSteuerdaten(UUID gesuchTrancheId) {
        steuerdatenAuthorizer.canRead();
        return steuerdatenService.getSteuerdaten(gesuchTrancheId).stream().map(steuerdatenMapper::toDto).toList();
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public List<SteuerdatenDto> updateSteuerdaten(
        UUID gesuchTrancheId,
        List<SteuerdatenDto> steuerdatenDto
    ) {
        steuerdatenAuthorizer.canUpdate(gesuchTrancheId);
        return steuerdatenService.updateSteuerdaten(gesuchTrancheId, steuerdatenDto)
            .stream()
            .map(steuerdatenMapper::toDto)
            .toList();
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public List<SteuerdatenDto> updateSteuerdatenFromNesko(
        UUID gesuchTrancheId,
        NeskoGetSteuerdatenRequestDto neskoGetSteuerdatenRequestDto
    ) {
        steuerdatenAuthorizer.canUpdate(gesuchTrancheId);
        return steuerdatenService.updateSteuerdatenFromNesko(
            gesuchTrancheId,
            neskoGetSteuerdatenRequestDto.getSteuerdatenTyp(),
            neskoGetSteuerdatenRequestDto.getToken(),
            neskoGetSteuerdatenRequestDto.getSteuerjahr()
        ).stream().map(steuerdatenMapper::toDto).toList();
    }
}
