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

package ch.dvbern.stip.api.gesuchsperioden.resource;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodenService;
import ch.dvbern.stip.generated.api.GesuchsperiodeResource;
import ch.dvbern.stip.generated.dto.GesuchsperiodeCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeWithDatenDto;
import ch.dvbern.stip.generated.dto.NullableGesuchsperiodeWithDatenDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_UPDATE;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class GesuchsperiodeResourceImpl implements GesuchsperiodeResource {
    private final GesuchsperiodenService gesuchsperiodenService;

    @RolesAllowed(STAMMDATEN_CREATE)
    @Override
    @AllowAll
    public GesuchsperiodeWithDatenDto createGesuchsperiode(GesuchsperiodeCreateDto createGesuchsperiodeDto) {
        return gesuchsperiodenService.createGesuchsperiode(createGesuchsperiodeDto);
    }

    @RolesAllowed(STAMMDATEN_DELETE)
    @Override
    @AllowAll
    public void deleteGesuchsperiode(UUID gesuchsperiodeId) {
        gesuchsperiodenService.deleteGesuchsperiode(gesuchsperiodeId);
    }

    @RolesAllowed(STAMMDATEN_READ)
    @Override
    @AllowAll
    public List<GesuchsperiodeDto> getAktiveGesuchsperioden() {
        return gesuchsperiodenService.getAllActive();
    }

    @RolesAllowed(STAMMDATEN_READ)
    @Override
    @AllowAll
    public GesuchsperiodeWithDatenDto getGesuchsperiode(UUID gesuchsperiodeId) {
        return gesuchsperiodenService.getGesuchsperiode(gesuchsperiodeId)
            .orElseThrow(NotFoundException::new);
    }

    @RolesAllowed(STAMMDATEN_READ)
    @Override
    @AllowAll
    public List<GesuchsperiodeDto> getGesuchsperioden() {
        return gesuchsperiodenService.getAllGesuchsperioden();
    }

    @RolesAllowed(STAMMDATEN_READ)
    @Override
    @AllowAll
    public NullableGesuchsperiodeWithDatenDto getLatest() {
        final var gesuchsperiode = gesuchsperiodenService.getLatest();
        return new NullableGesuchsperiodeWithDatenDto(gesuchsperiode);
    }

    @Override
    @AllowAll
    @RolesAllowed(STAMMDATEN_UPDATE)
    public GesuchsperiodeWithDatenDto publishGesuchsperiode(UUID gesuchperiodeId) {
        return gesuchsperiodenService.publishGesuchsperiode(gesuchperiodeId);
    }

    @RolesAllowed(STAMMDATEN_UPDATE)
    @Override
    @AllowAll
    public GesuchsperiodeWithDatenDto updateGesuchsperiode(
        UUID gesuchsperiodeId,
        GesuchsperiodeUpdateDto gesuchsperiodeUpdateDto
    ) {
        return gesuchsperiodenService.updateGesuchsperiode(gesuchsperiodeId, gesuchsperiodeUpdateDto);
    }
}
