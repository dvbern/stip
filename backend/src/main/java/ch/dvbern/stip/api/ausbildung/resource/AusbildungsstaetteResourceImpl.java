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
import ch.dvbern.stip.api.common.authorization.AusbildungsstaetteAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.generated.api.AusbildungsstaetteResource;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNGSSTAETTE_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNGSSTAETTE_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNGSSTAETTE_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNGSSTAETTE_UPDATE;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class AusbildungsstaetteResourceImpl implements AusbildungsstaetteResource {
    private final AusbildungsstaetteAuthorizer ausbildungsstaetteAuthorizer;
    private final AusbildungsstaetteService ausbildungsstaetteService;

    @Override
    @RolesAllowed(AUSBILDUNGSSTAETTE_CREATE)
    public AusbildungsstaetteDto createAusbildungsstaette(AusbildungsstaetteCreateDto ausbildungsstaette) {
        ausbildungsstaetteAuthorizer.canCreate();
        return ausbildungsstaetteService.createAusbildungsstaette(ausbildungsstaette);
    }

    @Override
    @RolesAllowed(AUSBILDUNGSSTAETTE_DELETE)
    public void deleteAusbildungsstaette(UUID ausbildungsstaetteId) {
        ausbildungsstaetteAuthorizer.canDelete();
        ausbildungsstaetteService.deleteAusbildungsstaette(ausbildungsstaetteId);
    }

    @Override
    @RolesAllowed(AUSBILDUNGSSTAETTE_READ)
    public AusbildungsstaetteDto getAusbildungsstaette(UUID ausbildungsstaetteId) {
        ausbildungsstaetteAuthorizer.canRead();
        return ausbildungsstaetteService.findById(ausbildungsstaetteId);
    }

    @Override
    @RolesAllowed(AUSBILDUNGSSTAETTE_READ)
    public List<AusbildungsstaetteDto> getAusbildungsstaetten() {
        ausbildungsstaetteAuthorizer.canRead();
        return ausbildungsstaetteService.getAusbildungsstaetten();
    }

    @Override
    @RolesAllowed(AUSBILDUNGSSTAETTE_UPDATE)
    public AusbildungsstaetteDto updateAusbildungsstaette(
        UUID ausbildungsstaetteId,
        AusbildungsstaetteUpdateDto ausbildungsstaette
    ) {
        ausbildungsstaetteAuthorizer.canUpdate();
        return ausbildungsstaetteService.updateAusbildungsstaette(ausbildungsstaetteId, ausbildungsstaette);
    }
}
