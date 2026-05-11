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

import java.util.regex.Pattern;

import ch.dvbern.stip.api.common.type.TenantIdentifier;
import ch.dvbern.stip.api.config.type.StipConfig;
import ch.dvbern.stip.api.config.type.TenantConfig;
import ch.dvbern.stip.generated.dto.TenantAuthConfigDto;
import ch.dvbern.stip.generated.dto.TenantFeatureDto;
import ch.dvbern.stip.generated.dto.TenantFeaturesDto;
import ch.dvbern.stip.generated.dto.TenantInfoDto;
import io.quarkus.arc.profile.UnlessBuildProfile;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.tenancy.service.OidcTenantResolver.TENANT_IDENTIFIER_CONTEXT_NAME;

@ApplicationScoped
@RequiredArgsConstructor
@UnlessBuildProfile("test")
public class TenantService {
    private static final ThreadLocal<String> EXPLICIT_TENANT_ID = new ThreadLocal<>();

    private final RoutingContext context;
    private final StipConfig config;

    public static ExplicitTenantIdScope setTenantId(final String tenantId) {
        return new ExplicitTenantIdScope(EXPLICIT_TENANT_ID, tenantId);
    }

    public TenantInfoDto getCurrentTenant() {
        final var tenantId = getCurrentStringIdentifier();

        final var tenantConfig = getConfigForCurrentTenant();

        final var tenantAuthConfig = new TenantAuthConfigDto();
        tenantAuthConfig.setAuthServerUrl(config.oidc().frontendUrl());
        tenantAuthConfig.setRealm(tenantId);

        final var steuerdatenFeature = new TenantFeatureDto();
        steuerdatenFeature.enabled(tenantConfig.port().steuerdaten().enabled());
        steuerdatenFeature.setAdapterType(tenantConfig.port().steuerdaten().adapterType().orElse(null));

        final var tenantFeatures = new TenantFeaturesDto();
        tenantFeatures.setSteuerdaten(steuerdatenFeature);

        return new TenantInfoDto()
            .features(tenantFeatures)
            .identifier(tenantId)
            .clientAuth(tenantAuthConfig);
    }

    public String getCurrentStringIdentifier() {
        if (EXPLICIT_TENANT_ID.get() != null) {
            return EXPLICIT_TENANT_ID.get();
        }

        return context.get(TENANT_IDENTIFIER_CONTEXT_NAME);
    }

    public TenantIdentifier getCurrentTenantIdentifier() {
        return TenantIdentifier.of(getCurrentStringIdentifier());
    }

    public TenantConfig getConfigForCurrentTenant() {
        return config.tenant().get(getCurrentTenantIdentifier());
    }

    public TenantIdentifier resolveTenant(final String subdomain) {
        for (final var tenant : TenantIdentifier.values()) {
            final var subdomainPatterns = config.tenant().get(tenant).subdomains();
            final var matches = subdomainPatterns.stream().anyMatch(subdomainPattern -> {
                final var pattern = Pattern.compile(subdomainPattern, Pattern.CASE_INSENSITIVE);
                return pattern.matcher(subdomain).matches();
            });
            if (matches) {
                return tenant;
            }
        }

        return TenantIdentifier.of(config.defaultTenant());
    }
}
