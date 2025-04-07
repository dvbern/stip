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

import ch.dvbern.stip.api.common.authorization.GesuchsperiodeAuthorizer;
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
    private final GesuchsperiodeAuthorizer gesuchsperiodeAuthorizer;
    private final GesuchsperiodenService gesuchsperiodenService;

    @Override
    @RolesAllowed(STAMMDATEN_CREATE)
    public GesuchsperiodeWithDatenDto createGesuchsperiode(GesuchsperiodeCreateDto createGesuchsperiodeDto) {
        gesuchsperiodeAuthorizer.canCreate();
        return gesuchsperiodenService.createGesuchsperiode(createGesuchsperiodeDto);
    }

    @Override
    @RolesAllowed(STAMMDATEN_DELETE)
    public void deleteGesuchsperiode(UUID gesuchsperiodeId) {
        gesuchsperiodeAuthorizer.canDelete(gesuchsperiodeId);
        gesuchsperiodenService.deleteGesuchsperiode(gesuchsperiodeId);
    }

    @Override
    @RolesAllowed(STAMMDATEN_READ)
    public List<GesuchsperiodeDto> getAktiveGesuchsperioden() {
        gesuchsperiodeAuthorizer.canRead();
        return gesuchsperiodenService.getAllActive();
    }

    @Override
    @RolesAllowed(STAMMDATEN_READ)
    public GesuchsperiodeWithDatenDto getGesuchsperiode(UUID gesuchsperiodeId) {
        gesuchsperiodeAuthorizer.canRead();
        return gesuchsperiodenService.getGesuchsperiode(gesuchsperiodeId)
            .orElseThrow(NotFoundException::new);
    }

    @Override
    @RolesAllowed(STAMMDATEN_READ)
    public List<GesuchsperiodeDto> getGesuchsperioden() {
        gesuchsperiodeAuthorizer.canRead();
        return gesuchsperiodenService.getAllGesuchsperioden();
    }

    @Override
    @RolesAllowed(STAMMDATEN_READ)
    public NullableGesuchsperiodeWithDatenDto getLatest() {
        gesuchsperiodeAuthorizer.canRead();
        final var gesuchsperiode = gesuchsperiodenService.getLatest();
        return new NullableGesuchsperiodeWithDatenDto(gesuchsperiode);
    }

    @Override
    @RolesAllowed(STAMMDATEN_UPDATE)
    public GesuchsperiodeWithDatenDto publishGesuchsperiode(UUID gesuchperiodeId) {
        gesuchsperiodeAuthorizer.canPublish();
        return gesuchsperiodenService.publishGesuchsperiode(gesuchperiodeId);
    }

    @Override
    @RolesAllowed(STAMMDATEN_UPDATE)
    public GesuchsperiodeWithDatenDto updateGesuchsperiode(
        UUID gesuchsperiodeId,
        GesuchsperiodeUpdateDto gesuchsperiodeUpdateDto
    ) {
        gesuchsperiodeAuthorizer.canUpdate(gesuchsperiodeId);
        return gesuchsperiodenService.updateGesuchsperiode(gesuchsperiodeId, gesuchsperiodeUpdateDto);
    }
}
