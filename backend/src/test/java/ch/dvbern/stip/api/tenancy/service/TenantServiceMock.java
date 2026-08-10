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

import ch.dvbern.stip.api.common.type.TenantIdentifier;
import ch.dvbern.stip.api.config.type.StipConfig;
import ch.dvbern.stip.api.config.type.TenantConfig;
import ch.dvbern.stip.generated.dto.TenantAuthConfigDto;
import ch.dvbern.stip.generated.dto.TenantFeatureDto;
import ch.dvbern.stip.generated.dto.TenantFeaturesDto;
import ch.dvbern.stip.generated.dto.TenantInfoDto;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Mock
@ApplicationScoped
public class TenantServiceMock extends TenantService {

    @Inject
    StipConfig config;

    @Inject
    TenantContext tenantContext;

    public TenantServiceMock() {
        super(null, null, null);
    }

    @Override
    public String getCurrentStringIdentifier() {
        return TenantIdentifier.BERN.getIdentifier();
    }

    @Override
    public TenantConfig getConfigForCurrentTenant() {
        return config.tenant().get(TenantIdentifier.BERN);
    }

    @Override
    public TenantIdentifier resolveTenant(String subdomain) {
        return TenantIdentifier.BERN;
    }

    @Override
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
}
