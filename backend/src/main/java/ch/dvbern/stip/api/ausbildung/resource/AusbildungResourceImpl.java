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

package ch.dvbern.stip.api.ausbildung.resource;

import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.service.AusbildungService;
import ch.dvbern.stip.api.common.authorization.AusbildungAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.generated.api.AusbildungResource;
import ch.dvbern.stip.generated.dto.AusbildungCreateResponseDto;
import ch.dvbern.stip.generated.dto.AusbildungDto;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_UPDATE;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class AusbildungResourceImpl implements AusbildungResource {
    private final AusbildungService ausbildungService;
    private final AusbildungAuthorizer ausbildungAuthorizer;

    @Override
    @RolesAllowed(AUSBILDUNG_CREATE)
    public AusbildungCreateResponseDto createAusbildung(AusbildungUpdateDto ausbildungUpdateDto) {
        ausbildungAuthorizer.canCreate(ausbildungUpdateDto.getFallId());
        return ausbildungService.createAusbildung(ausbildungUpdateDto);
    }

    @Override
    @RolesAllowed(AUSBILDUNG_READ)
    public AusbildungDto getAusbildung(UUID ausbildungId) {
        ausbildungAuthorizer.canRead(ausbildungId);
        return ausbildungService.getAusbildungById(ausbildungId);
    }

    @Override
    @RolesAllowed(AUSBILDUNG_UPDATE)
    public AusbildungDto updateAusbildung(UUID ausbildungId, AusbildungUpdateDto ausbildungUpdateDto) {
        ausbildungAuthorizer.canUpdate(ausbildungId);
        return ausbildungService.patchAusbildung(ausbildungId, ausbildungUpdateDto);
    }
}
