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

import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.common.authorization.GesuchNotizAuthorizer;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.notiz.service.GesuchNotizService;
import ch.dvbern.stip.generated.api.GesuchNotizResource;
import ch.dvbern.stip.generated.dto.GesuchNotizCreateDto;
import ch.dvbern.stip.generated.dto.GesuchNotizUpdateDto;
import ch.dvbern.stip.generated.dto.JuristischeAbklaerungNotizAntwortDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchNotizResourceImpl implements GesuchNotizResource {
    private final GesuchNotizService service;
    private final GesuchNotizAuthorizer authorizer;

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_JURIST })
    @Override
    public Response answerJuristischeAbklaerungNotiz(
        UUID notizId,
        JuristischeAbklaerungNotizAntwortDto juristischeAbklaerungNotizAntwortDto
    ) {
        return null;
    }

    @AllowAll
    @RolesAllowed(OidcConstants.ROLE_SACHBEARBEITER)
    @Override
    public Response createNotiz(GesuchNotizCreateDto gesuchNotizCreateDto) {
        final var notiz = service.create(gesuchNotizCreateDto);
        return Response.ok(notiz).build();
    }

    @AllowAll
    @RolesAllowed(OidcConstants.ROLE_SACHBEARBEITER)
    @Override
    public Response deleteNotiz(UUID notizId) {
        service.delete(notizId);
        return Response.noContent().build();
    }

    @AllowAll
    @RolesAllowed({ OidcConstants.ROLE_JURIST, OidcConstants.ROLE_SACHBEARBEITER })
    @Override
    public Response getJuristischeAbklaerungNotiz(UUID notizId) {
        return null;
    }

    @RolesAllowed({ OidcConstants.ROLE_JURIST, OidcConstants.ROLE_SACHBEARBEITER })
    @AllowAll
    @Override
    public Response getJuristischeAbklaerungNotizen(UUID gesuchId) {
        return null;
    }

    @AllowAll
    @RolesAllowed(OidcConstants.ROLE_SACHBEARBEITER)
    @Override
    public Response getNotiz(UUID notizId) {
        return Response.ok(service.getById(notizId)).build();
    }

    @AllowAll
    @RolesAllowed(OidcConstants.ROLE_SACHBEARBEITER)
    @Override
    public Response getNotizen(UUID gesuchId) {
        final var notizen = service.getAllByGesuchId(gesuchId);
        return Response.ok(notizen).build();
    }

    @AllowAll
    @RolesAllowed(OidcConstants.ROLE_SACHBEARBEITER)
    @Override
    public Response updateNotiz(GesuchNotizUpdateDto gesuchNotizUpdateDto) {
        final var notiz = service.update(gesuchNotizUpdateDto);
        return Response.ok(notiz).build();
    }
}
