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

package ch.dvbern.stip.api.gesuchsjahr.resource;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.GesuchsjahrAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.gesuchsjahr.service.GesuchsjahrService;
import ch.dvbern.stip.generated.api.GesuchsjahrResource;
import ch.dvbern.stip.generated.dto.GesuchsjahrCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsjahrDto;
import ch.dvbern.stip.generated.dto.GesuchsjahrUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.AllArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_UPDATE;

@RequestScoped
@AllArgsConstructor
@Validated
public class GesuchsjahrResourceImpl implements GesuchsjahrResource {
    private final GesuchsjahrAuthorizer gesuchsjahrAuthorizer;
    private final GesuchsjahrService gesuchsjahrService;

    @Override
    @RolesAllowed(STAMMDATEN_READ)
    public GesuchsjahrDto getGesuchsjahr(UUID gesuchsjahrId) {
        gesuchsjahrAuthorizer.canGet();
        return gesuchsjahrService.getGesuchsjahr(gesuchsjahrId);
    }

    @Override
    @RolesAllowed(STAMMDATEN_READ)
    public List<GesuchsjahrDto> getGesuchsjahre() {
        gesuchsjahrAuthorizer.canGet();
        return gesuchsjahrService.getGesuchsjahre();
    }

    @Override
    @RolesAllowed(STAMMDATEN_CREATE)
    public GesuchsjahrDto createGesuchsjahr(GesuchsjahrCreateDto gesuchsjahrCreateDto) {
        gesuchsjahrAuthorizer.canCreate();
        return gesuchsjahrService.createGesuchsjahr(gesuchsjahrCreateDto);
    }

    @Override
    @RolesAllowed(STAMMDATEN_UPDATE)
    public GesuchsjahrDto updateGesuchsjahr(UUID gesuchsjahrId, GesuchsjahrUpdateDto gesuchsjahrUpdateDto) {
        gesuchsjahrAuthorizer.canUpdate(gesuchsjahrId);
        return gesuchsjahrService.updateGesuchsjahr(gesuchsjahrId, gesuchsjahrUpdateDto);
    }

    @Override
    @RolesAllowed(STAMMDATEN_UPDATE)
    public GesuchsjahrDto publishGesuchsjahr(UUID gesuchsjahrId) {
        gesuchsjahrAuthorizer.canPublish();
        return gesuchsjahrService.publishGesuchsjahr(gesuchsjahrId);
    }

    @Override
    @RolesAllowed(STAMMDATEN_DELETE)
    public void deleteGesuchsjahr(UUID gesuchsjahrId) {
        gesuchsjahrAuthorizer.canDelete(gesuchsjahrId);
        gesuchsjahrService.deleteGesuchsjahr(gesuchsjahrId);
    }
}
