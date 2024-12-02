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

import ch.dvbern.stip.api.ausbildung.service.AusbildungsgangService;
import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.generated.api.AusbildungsgangResource;
import ch.dvbern.stip.generated.dto.AusbildungsgangCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_UPDATE;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungsgangResourceImpl implements AusbildungsgangResource {
    private final AusbildungsgangService ausbildungsgangService;

    @Override
    @RolesAllowed(AUSBILDUNG_CREATE)
    @AllowAll
    public AusbildungsgangDto createAusbildungsgang(AusbildungsgangCreateDto ausbildungsgangCreateDto) {
        return ausbildungsgangService.createAusbildungsgang(ausbildungsgangCreateDto);
    }

    @Override
    @RolesAllowed(AUSBILDUNG_DELETE)
    @AllowAll
    public void deleteAusbildungsgang(UUID ausbildungsgangId) {
        ausbildungsgangService.deleteAusbildungsgang(ausbildungsgangId);
    }

    @Override
    @RolesAllowed(AUSBILDUNG_READ)
    @AllowAll
    public AusbildungsgangDto getAusbildungsgang(UUID ausbildungsgangId) {
        return ausbildungsgangService.findById(ausbildungsgangId);
    }

    @Override
    @RolesAllowed(AUSBILDUNG_UPDATE)
    @AllowAll
    public AusbildungsgangDto updateAusbildungsgang(
        UUID ausbildungsgangId,
        AusbildungsgangUpdateDto ausbildungsgangUpdateDto
    ) {
        return ausbildungsgangService.updateAusbildungsgang(ausbildungsgangId, ausbildungsgangUpdateDto);
    }
}
