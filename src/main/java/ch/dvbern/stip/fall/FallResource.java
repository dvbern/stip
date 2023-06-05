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

package ch.dvbern.stip.fall;

import jakarta.annotation.security.DenyAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import ch.dvbern.stip.annotations.ApiResource;
import ch.dvbern.stip.fall.model.Fall;
import ch.dvbern.stip.fall.dto.FallDTO;
import ch.dvbern.stip.fall.service.FallService;
import ch.dvbern.stip.gesuchsperiode.dto.GesuchsperiodeDTO;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.UUID;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("fall")
@Tag(name ="Fall")
@ApiResource
@DenyAll
public class FallResource {

	@Inject
	FallService fallService;

	@PUT
	@APIResponse(responseCode = "200",
			content = @Content(schema = @Schema(implementation = FallDTO.class)))
	@APIResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
	@APIResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
	@APIResponse(responseCode = "500", ref = "#/components/responses/ServerError")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveFall(
			FallDTO fall
	){
		Fall changed = fallService.saveFall(fall);
		return Response.ok(FallDTO.from(changed)).build();
	}

	@GET
	@Path("/id/{fallId}")
	@Operation(
			summary = "Returniert der Fall mit der gegebene Id.")
	@APIResponse(responseCode = "200",
			content = @Content(schema = @Schema(implementation = FallDTO.class)))
	@APIResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
	@APIResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
	@APIResponse(responseCode = "500", ref = "#/components/responses/ServerError")
	@APIResponse(responseCode = "404", ref = "#/components/responses/NotFound")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response getFallDTO(
			@PathParam("fallId") UUID fallId
	) {
		return fallService.findFall(fallId).map(fall -> Response.ok(FallDTO.from(fall)))
				.orElseGet(()-> Response.status(Response.Status.NOT_FOUND)).build();
	}

}
