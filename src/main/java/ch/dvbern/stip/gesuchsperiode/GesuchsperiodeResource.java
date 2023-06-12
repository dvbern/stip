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

package ch.dvbern.stip.gesuchsperiode;

import ch.dvbern.stip.annotations.ApiResource;
import ch.dvbern.stip.gesuchsperiode.dto.GesuchsperiodeDTO;
import ch.dvbern.stip.gesuchsperiode.model.Gesuchsperiode;
import ch.dvbern.stip.gesuchsperiode.service.GesuchsperiodeService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.annotation.security.DenyAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("gesuchsperiode")
@Tag(name ="Gesuchsperiode")
@ApiResource
@DenyAll
public class GesuchsperiodeResource {

    @Inject
    GesuchsperiodeService gesuchsperiodeService;

    @PUT
    @APIResponse(responseCode = "200",
            content = @Content(schema = @Schema(implementation = GesuchsperiodeDTO.class)))
    @APIResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    @APIResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    @APIResponse(responseCode = "500", ref = "#/components/responses/ServerError")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveGesuchsperiode(
            GesuchsperiodeDTO gesuchsperiode
    ){
        Gesuchsperiode changed = gesuchsperiodeService.saveGesuchsperiode(gesuchsperiode);
        return Response.ok(GesuchsperiodeDTO.from(changed)).build();
    }

    @GET
    @Path("/{gesuchsperiodeId}")
    @Operation(
            summary = "Returniert der Gesuchsperiode mit der gegebene Id.")
    @APIResponse(responseCode = "200",
            content = @Content(schema = @Schema(implementation = GesuchsperiodeDTO.class)))
    @APIResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    @APIResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    @APIResponse(responseCode = "500", ref = "#/components/responses/ServerError")
    @APIResponse(responseCode = "404", ref = "#/components/responses/NotFound")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response getGesuchsperiodeDTO(
            @PathParam("gesuchsperiodeId") UUID gesuchsperiodeId
    ) {
        return gesuchsperiodeService.findGesuchsperiode(gesuchsperiodeId).map(gesuchsperiode -> Response.ok(GesuchsperiodeDTO.from(gesuchsperiode)))
                .orElseGet(()-> Response.status(Response.Status.NOT_FOUND)).build();
    }

    @GET
    @Path("/all")
    @Operation(
        summary = "Returniert der Gesuchsperiode mit der gegebene Id.")
    @APIResponse(responseCode = "200")
    @APIResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    @APIResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    @APIResponse(responseCode = "500", ref = "#/components/responses/ServerError")
    @APIResponse(responseCode = "404", ref = "#/components/responses/NotFound")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response getAlleGesuchsperiode(
    ) {
        return gesuchsperiodeService.findAlleGesuchsperiodeDTO().map(Response::ok)
            .orElseGet(()-> Response.status(Response.Status.NOT_FOUND)).build();
    }

    @GET
    @Path("/aktive")
    @Operation(
            summary = "Returniert der Gesuchsperiode mit der gegebene Id.")
    @APIResponse(responseCode = "200")
    @APIResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    @APIResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    @APIResponse(responseCode = "500", ref = "#/components/responses/ServerError")
    @APIResponse(responseCode = "404", ref = "#/components/responses/NotFound")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response getAktiveGesuchsperiode(
    ) {
        return gesuchsperiodeService.findAlleAktiveGesuchsperiodeDTO().map(Response::ok)
                .orElseGet(()-> Response.status(Response.Status.NOT_FOUND)).build();
    }

}
