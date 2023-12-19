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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.fall.resource;

import ch.dvbern.stip.api.fall.service.FallService;
import ch.dvbern.stip.generated.api.FallResource;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_GESUCHSTELLER;
import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_SACHBEARBEITER;

@RequestScoped
@RequiredArgsConstructor
public class FallResourceImpl implements FallResource {

	private final FallService fallService;
	private final UriInfo uriInfo;

    @RolesAllowed({ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER})
	@Override
	public Response createFall(UUID benutzerId) {
		var fall = fallService.createFall(benutzerId);
		return Response.created(uriInfo.getAbsolutePathBuilder().path(fall.getId().toString()).build()).build();
	}

    @RolesAllowed({ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER})
	@Override
	public Response getFall(UUID fallId) {
		var fall = fallService
				.getFall(fallId)
				.orElseThrow(NotFoundException::new);
		return Response.ok(fall).build();
	}

    @RolesAllowed({ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER})
	@Override
	public Response getFallForBenutzer(UUID benutzerId) {
		return Response.ok(fallService.findFaelleForBenutzer(benutzerId)).build();
	}
}
