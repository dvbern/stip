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

package ch.dvbern.stip.api.notiz.resource;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.common.authorization.GesuchNotizAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.notiz.service.GesuchNotizService;
import ch.dvbern.stip.generated.api.GesuchNotizResource;
import ch.dvbern.stip.generated.dto.GesuchNotizCreateDto;
import ch.dvbern.stip.generated.dto.GesuchNotizDto;
import ch.dvbern.stip.generated.dto.GesuchNotizUpdateDto;
import ch.dvbern.stip.generated.dto.JuristischeAbklaerungNotizAntwortDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class GesuchNotizResourceImpl implements GesuchNotizResource {
    private final GesuchNotizService service;
    private final GesuchNotizAuthorizer authorizer;

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_JURIST })
    @Override
    public GesuchNotizDto answerJuristischeAbklaerungNotiz(
        UUID notizId,
        JuristischeAbklaerungNotizAntwortDto juristischeAbklaerungNotizAntwortDto
    ) {
        authorizer.canSetAnswer(notizId);
        return service.answerJuristischeNotiz(juristischeAbklaerungNotizAntwortDto, notizId);
    }

    @AllowAll
    @RolesAllowed(OidcConstants.ROLE_SACHBEARBEITER)
    @Override
    public GesuchNotizDto createNotiz(GesuchNotizCreateDto gesuchNotizCreateDto) {
        return service.create(gesuchNotizCreateDto);
    }

    @AllowAll
    @RolesAllowed(OidcConstants.ROLE_SACHBEARBEITER)
    @Override
    public void deleteNotiz(UUID notizId) {
        authorizer.canDelete(notizId);
        service.delete(notizId);
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_JURIST, OidcConstants.ROLE_SACHBEARBEITER })
    @Override
    public GesuchNotizDto getNotiz(UUID notizId) {
        return service.getById(notizId);
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_JURIST, OidcConstants.ROLE_SACHBEARBEITER })
    @Override
    public List<GesuchNotizDto> getNotizen(UUID gesuchId) {
        return service.getAllByGesuchId(gesuchId);
    }

    @AllowAll
    @RolesAllowed(OidcConstants.ROLE_SACHBEARBEITER)
    @Override
    public GesuchNotizDto updateNotiz(GesuchNotizUpdateDto gesuchNotizUpdateDto) {
        authorizer.canUpdate(gesuchNotizUpdateDto.getId());
        return service.update(gesuchNotizUpdateDto);
    }
}
