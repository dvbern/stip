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

import ch.dvbern.stip.api.common.authorization.GesuchNotizAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.notiz.service.GesuchNotizService;
import ch.dvbern.stip.generated.api.GesuchNotizResource;
import ch.dvbern.stip.generated.dto.GesuchNotizCreateDto;
import ch.dvbern.stip.generated.dto.GesuchNotizDto;
import ch.dvbern.stip.generated.dto.GesuchNotizUpdateDto;
import ch.dvbern.stip.generated.dto.JuristischeAbklaerungNotizAntwortDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.NOTIZ_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.NOTIZ_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.NOTIZ_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.NOTIZ_UPDATE;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class GesuchNotizResourceImpl implements GesuchNotizResource {
    private final GesuchNotizService service;
    private final GesuchNotizAuthorizer authorizer;
    private final GesuchService gesuchService;

    @Override
    @RolesAllowed(NOTIZ_CREATE)
    public GesuchNotizDto answerJuristischeAbklaerungNotiz(
        UUID notizId,
        JuristischeAbklaerungNotizAntwortDto juristischeAbklaerungNotizAntwortDto
    ) {
        authorizer.canSetAnswer(notizId);
        final var gesuch = service.getGesuchOfNotiz(notizId);
        final var answeredNotiz = service.answerJuristischeNotiz(juristischeAbklaerungNotizAntwortDto, notizId);
        gesuchService.gesuchStatusToBereitFuerBearbeitung(gesuch.getId());
        return answeredNotiz;
    }

    @Override
    @RolesAllowed(NOTIZ_CREATE)
    public GesuchNotizDto createNotiz(GesuchNotizCreateDto gesuchNotizCreateDto) {
        authorizer.canCreate(gesuchNotizCreateDto.getGesuchId(), gesuchNotizCreateDto.getNotizTyp());
        return gesuchService.createGesuchNotiz(gesuchNotizCreateDto);
    }

    @Override
    @RolesAllowed(NOTIZ_DELETE)
    public void deleteNotiz(UUID notizId) {
        authorizer.canDelete(notizId);
        service.delete(notizId);
    }

    @Override
    @RolesAllowed(NOTIZ_READ)
    public GesuchNotizDto getNotiz(UUID notizId) {
        authorizer.canGet();
        return service.getById(notizId);
    }

    @Override
    @RolesAllowed(NOTIZ_CREATE)
    public List<GesuchNotizDto> getNotizen(UUID gesuchId) {
        authorizer.canGet();
        return service.getAllByGesuchId(gesuchId);
    }

    @Override
    @RolesAllowed(NOTIZ_UPDATE)
    public GesuchNotizDto updateNotiz(GesuchNotizUpdateDto gesuchNotizUpdateDto) {
        authorizer.canUpdate(gesuchNotizUpdateDto.getId());
        return service.update(gesuchNotizUpdateDto);
    }
}
