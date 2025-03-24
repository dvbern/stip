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

import ch.dvbern.stip.api.common.type.MandantIdentifier;
import ch.dvbern.stip.generated.dto.TenantAuthConfigDto;
import ch.dvbern.stip.generated.dto.TenantInfoDto;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
public class MockTenantService extends TenantService {
    public MockTenantService() {
        super(null, null, null);
    }

    @Override
    public TenantInfoDto getCurrentTenant() {
        return new TenantInfoDto()
            .identifier("bern")
            .clientAuth(
                new TenantAuthConfigDto()
                    .authServerUrl(keycloakFrontendUrl)
                    .realm("bern")
            );
    }

    @Override
    public String getCurrentTenantIdentifier() {
        return MandantIdentifier.BERN.getIdentifier();
    }

    @Override
    public MandantIdentifier resolveTenant(String subdomain) {
        return MandantIdentifier.BERN;
    }
}
