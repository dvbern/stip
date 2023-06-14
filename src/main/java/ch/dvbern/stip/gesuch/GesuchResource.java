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

package ch.dvbern.stip.gesuch;

import ch.dvbern.stip.annotations.ApiResource;
import ch.dvbern.stip.gesuch.model.Gesuch;
import ch.dvbern.stip.gesuch.dto.GesuchDTO;
import ch.dvbern.stip.gesuch.service.GesuchService;
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

import java.util.UUID;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("gesuch")
@Tag(name ="Gesuch")
@ApiResource
@DenyAll
public class GesuchResource {

    @Inject
    GesuchService gesuchService;

    @POST
    @APIResponse(responseCode = "200",
            content = @Content(schema = @Schema(implementation = String.class)))
    @APIResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    @APIResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    @APIResponse(responseCode = "500", ref = "#/components/responses/ServerError")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(
            GesuchDTO gesuch
    ){
        Gesuch changed = gesuchService.saveGesuch(gesuch);
        return Response.ok(changed.getId()).build();
    }

    @PUT
    @APIResponse(responseCode = "200",
            content = @Content(schema = @Schema(implementation = GesuchDTO.class)))
    @APIResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    @APIResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    @APIResponse(responseCode = "500", ref = "#/components/responses/ServerError")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveGesuch(
            GesuchDTO gesuch
    ){
        Gesuch changed = gesuchService.saveGesuch(gesuch);
        return Response.ok(GesuchDTO.from(changed)).build();
    }

    @GET
    @Path("/{gesuchId}")
    @Operation(
            summary = "Returniert das Gesuch mit der gegebene Id.")
    @APIResponse(responseCode = "200",
            content = @Content(schema = @Schema(implementation = GesuchDTO.class)))
    @APIResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    @APIResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    @APIResponse(responseCode = "500", ref = "#/components/responses/ServerError")
    @APIResponse(responseCode = "404", ref = "#/components/responses/NotFound")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response getGesuchDTO(
            @PathParam("gesuchId") UUID gesuchId
    ) {
        return gesuchService.findGesuchDTO(gesuchId).map(Response::ok)
                .orElseGet(()-> Response.status(Response.Status.NOT_FOUND)).build();
    }

    @GET
    @Operation(
            summary = "Returniert alle Gesuch.")
    @APIResponse(responseCode = "200")
    @APIResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    @APIResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    @APIResponse(responseCode = "500", ref = "#/components/responses/ServerError")
    @APIResponse(responseCode = "404", ref = "#/components/responses/NotFound")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response getGesuch() {
        return gesuchService.findAll().map(Response::ok)
                .orElseGet(()-> Response.status(Response.Status.NOT_FOUND)).build();
    }
}
