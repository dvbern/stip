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
import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.common.authorization.AusbildungAuthorizer;
import ch.dvbern.stip.generated.api.AusbildungResource;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_UPDATE;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungResourceImpl implements AusbildungResource {
    private final AusbildungService ausbildungService;
    private final AusbildungAuthorizer ausbildungAuthorizer;

    @Override
    @RolesAllowed(GESUCH_UPDATE)
    @AllowAll
    public Response createAusbildung(AusbildungUpdateDto ausbildungUpdateDto) {
        return Response.ok(ausbildungService.createAusbildung(ausbildungUpdateDto)).build();
    }

    @Override
    @RolesAllowed(GESUCH_READ)
    public Response getAusbildung(UUID ausbildungId) {
        ausbildungAuthorizer.canRead(ausbildungId);
        return Response.ok(ausbildungService.getAusbildungById(ausbildungId)).build();
    }

    @Override
    @RolesAllowed(GESUCH_UPDATE)
    public Response updateAusbildung(UUID ausbildungId, AusbildungUpdateDto ausbildungUpdateDto) {
        ausbildungAuthorizer.canUpdate(ausbildungId);
        return Response.ok(ausbildungService.patchAusbildung(ausbildungId, ausbildungUpdateDto)).build();
    }
}
