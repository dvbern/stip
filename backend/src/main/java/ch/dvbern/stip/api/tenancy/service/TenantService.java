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

import java.util.List;

import ch.dvbern.stip.api.common.type.MandantIdentifier;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.config.service.TenantSubdomainsProducer.PerTenantSubdomains;
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
    private static final ThreadLocal<String> EXPLICIT_TENANT_ID = new ThreadLocal<>();

    private final RoutingContext context;
    private final ConfigService configService;
    private final List<PerTenantSubdomains> perTenantSubdomains;

    @ConfigProperty(name = "keycloak.frontend-url")
    String keycloakFrontendUrl;

    public static ExplicitTenantIdScope setTenantId(final String tenantId) {
        return new ExplicitTenantIdScope(EXPLICIT_TENANT_ID, tenantId);
    }

    public TenantInfoDto getCurrentTenant() {
        final String tenantId = context.get(TENANT_IDENTIFIER_CONTEXT_NAME);

        final TenantAuthConfigDto tenantAuthConfig = new TenantAuthConfigDto();
        tenantAuthConfig.setAuthServerUrl(keycloakFrontendUrl);
        tenantAuthConfig.setRealm(tenantId);

        return new TenantInfoDto()
            .identifier(tenantId)
            .clientAuth(tenantAuthConfig);
    }

    public String getCurrentTenantIdentifier() {
        if (EXPLICIT_TENANT_ID.get() != null) {
            return EXPLICIT_TENANT_ID.get();
        }

        return context.get(TENANT_IDENTIFIER_CONTEXT_NAME);
    }

    public MandantIdentifier resolveTenant(final String subdomain) {
        for (final var perTenantSubdomain : perTenantSubdomains) {
            if (perTenantSubdomain.subdomains().contains(subdomain)) {
                return MandantIdentifier.of(perTenantSubdomain.tenant());
            }
        }

        return MandantIdentifier.of(configService.getDefaultTenant());
    }
}
