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

package ch.dvbern.stip.config;

import ch.dvbern.stip.annotations.ApiResource;
import ch.dvbern.stip.config.service.ConfigService;
import ch.dvbern.stip.config.model.DeploymentConfigDTO;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
@Path("config")
@Tag(name ="Configuration")
@ApiResource
public class ConfigResource {

    @Inject
    ConfigService configService;

    @GET
    @Path("/deployment")
    @Operation(
            summary = "Returniert der Deployment Configuration.",
            description =
                    "Gibt zuerueck die Version und Stage aus das Backend")
    @APIResponse(responseCode = "200",
            content = @Content(schema = @Schema(implementation = DeploymentConfigDTO.class)))
    @APIResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    @APIResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    @APIResponse(responseCode = "500", ref = "#/components/responses/ServerError")
    @Produces(APPLICATION_JSON)
    public Response getDeploymentConfig(
    ) {
        return Response.ok(configService.getDeploymentConfiguration()).build();
    }

}
