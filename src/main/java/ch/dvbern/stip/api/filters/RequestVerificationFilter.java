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

package ch.dvbern.stip.api.filters;

import ch.dvbern.stip.config.service.ConfigService;
import ch.dvbern.stip.config.model.DeploymentConfigDTO;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@PreMatching
@RequestScoped
public class RequestVerificationFilter implements ContainerRequestFilter {

    @Inject
    ConfigService configService;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        if (excludeResource(containerRequestContext)) {
            return;
        }
        if (isEnvAndVersionMatching(containerRequestContext)) {
            return;
        }
        throw new RuntimeException("headers not available");
    }

    private boolean excludeResource(ContainerRequestContext req) {
        UriInfo info = req.getUriInfo();
        if (info.getPath().contains("/config/deployment")) {
            return true;
        }
        return false;
    }

    private boolean isEnvAndVersionMatching(ContainerRequestContext req) {
        String environment = req.getHeaderString("environment"); // Todo Constant header shared ?
        String version = req.getHeaderString("version");
        DeploymentConfigDTO backendConfig = configService.getDeploymentConfiguration();
        // Local not used
        if (backendConfig.getEnvironment().equals("local")) return true;
        if (backendConfig.getEnvironment().equals(environment) && backendConfig.getVersion().equals(version)) {
            return true;
        }
        return false;
    }
}
