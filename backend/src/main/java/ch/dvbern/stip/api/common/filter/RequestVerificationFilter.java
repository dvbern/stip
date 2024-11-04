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

package ch.dvbern.stip.api.common.filter;

import java.io.IOException;

import ch.dvbern.stip.api.common.exception.AppFailureMessage;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.generated.dto.DeploymentConfigDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;

@Provider
@PreMatching
@RequestScoped
@RequiredArgsConstructor
public class RequestVerificationFilter implements ContainerRequestFilter {
    private final ConfigService configService;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        if (excludeResource(containerRequestContext)) {
            return;
        }
        if (isEnvAndVersionMatching(containerRequestContext)) {
            return;
        }

        throw AppFailureMessage.missingRequestHeader("environment,version").create();
    }

    private boolean excludeResource(ContainerRequestContext req) {
        UriInfo info = req.getUriInfo();
        return info.getPath().contains("/config/deployment");
    }

    private boolean isEnvAndVersionMatching(ContainerRequestContext req) {
        String environment = req.getHeaderString("environment");
        String version = req.getHeaderString("version");
        DeploymentConfigDto backendConfig = configService.getDeploymentConfiguration();
        // Local not used
        return backendConfig.getEnvironment().equals("local") || (backendConfig.getEnvironment().equals(environment)
        && backendConfig.getVersion().equals(version));
    }
}
