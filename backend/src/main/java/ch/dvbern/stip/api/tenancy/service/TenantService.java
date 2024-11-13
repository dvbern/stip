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

package ch.dvbern.stip.api.tenancy.service;

import ch.dvbern.stip.generated.dto.TenantAuthConfigDto;
import ch.dvbern.stip.generated.dto.TenantInfoDto;
import io.quarkus.arc.profile.UnlessBuildProfile;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import static ch.dvbern.stip.api.tenancy.service.OidcTenantResolver.TENANT_IDENTIFIER_CONTEXT_NAME;

@ApplicationScoped
@RequiredArgsConstructor
@UnlessBuildProfile("test")
public class TenantService {

    private final RoutingContext context;

    @ConfigProperty(name = "keycloak.frontend-url")
    String keycloakFrontendUrl;

    public TenantInfoDto getCurrentTenant() {
        final String tenantId = context.get(TENANT_IDENTIFIER_CONTEXT_NAME);

        final TenantAuthConfigDto tenantAuthConfig = new TenantAuthConfigDto();
        tenantAuthConfig.setAuthServerUrl(keycloakFrontendUrl);
        tenantAuthConfig.setRealm(tenantId);

        return new TenantInfoDto()
            .identifier(tenantId)
            .clientAuth(tenantAuthConfig);
    }
}
