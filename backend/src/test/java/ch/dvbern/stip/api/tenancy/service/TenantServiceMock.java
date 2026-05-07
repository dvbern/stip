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
import ch.dvbern.stip.api.config.StipConfig;
import ch.dvbern.stip.api.config.TenantConfig;
import ch.dvbern.stip.generated.dto.TenantAuthConfigDto;
import ch.dvbern.stip.generated.dto.TenantFeatureDto;
import ch.dvbern.stip.generated.dto.TenantInfoDto;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Mock
@ApplicationScoped
public class TenantServiceMock extends TenantService {

    @Inject
    StipConfig config;

    public TenantServiceMock() {
        super(null, null);
    }

    @Override
    public TenantInfoDto getCurrentTenant() {
        return new TenantInfoDto()
            .identifier("bern")
            .features(new TenantFeatureDto().nesko(false))
            .clientAuth(
                new TenantAuthConfigDto()
                    .authServerUrl(config.oidc().frontendUrl())
                    .realm("bern")
            );
    }

    @Override
    public TenantConfig getConfigForCurrentTenant() {
        return config.tenant().get(TenantIdentifier.BERN);
    }

    @Override
    public String getCurrentStringIdentifier() {
        return TenantIdentifier.BERN.getIdentifier();
    }

    @Override
    public TenantIdentifier getCurrentTenantIdentifier() {
        return TenantIdentifier.BERN;
    }

    @Override
    public TenantIdentifier resolveTenant(String subdomain) {
        return TenantIdentifier.BERN;
    }
}
