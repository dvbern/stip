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
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class FallResourceImpl implements FallResource {

    private final FallService fallService;

    @Override
    public Response createFallForGs() {
        return Response.ok(fallService.createFallForGs()).build();
    }

    @Override
    public Response getFaelleForSb() {
        return Response.ok(fallService.findFaelleForSb()).build();
    }

    @Override
    public Response getFallForGs() {
        return Response.ok(fallService.findFallForGs()).build();
    }
}
