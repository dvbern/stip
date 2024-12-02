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

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.service.AusbildungsstaetteService;
import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.generated.api.AusbildungsstaetteResource;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_UPDATE;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungsstaetteResourceImpl implements AusbildungsstaetteResource {
    private final AusbildungsstaetteService ausbildungsstaetteService;

    @Override
    @RolesAllowed(AUSBILDUNG_CREATE)
    @AllowAll
    public AusbildungsstaetteDto createAusbildungsstaette(AusbildungsstaetteCreateDto ausbildungsstaette) {
        return ausbildungsstaetteService.createAusbildungsstaette(ausbildungsstaette);
    }

    @Override
    @RolesAllowed(AUSBILDUNG_DELETE)
    @AllowAll
    public void deleteAusbildungsstaette(UUID ausbildungsstaetteId) {
        ausbildungsstaetteService.deleteAusbildungsstaette(ausbildungsstaetteId);
    }

    @Override
    @RolesAllowed(AUSBILDUNG_READ)
    @AllowAll
    public AusbildungsstaetteDto getAusbildungsstaette(UUID ausbildungsstaetteId) {
        return ausbildungsstaetteService.findById(ausbildungsstaetteId);
    }

    @Override
    @RolesAllowed(AUSBILDUNG_READ)
    @AllowAll
    public List<AusbildungsstaetteDto> getAusbildungsstaetten() {
        return ausbildungsstaetteService.getAusbildungsstaetten();
    }

    @Override
    @RolesAllowed(AUSBILDUNG_UPDATE)
    @AllowAll
    public AusbildungsstaetteDto updateAusbildungsstaette(
        UUID ausbildungsstaetteId,
        AusbildungsstaetteUpdateDto ausbildungsstaette
    ) {
        return ausbildungsstaetteService.updateAusbildungsstaette(ausbildungsstaetteId, ausbildungsstaette);
    }
}
