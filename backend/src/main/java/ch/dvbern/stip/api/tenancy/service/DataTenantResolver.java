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

import java.util.Objects;

import ch.dvbern.stip.api.common.type.TenantIdentifier;
import io.quarkus.arc.profile.UnlessBuildProfile;
import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.tenancy.service.OidcTenantResolver.DEFAULT_TENANT_IDENTIFIER;
import static ch.dvbern.stip.api.tenancy.service.OidcTenantResolver.TENANT_IDENTIFIER_CONTEXT_NAME;

@PersistenceUnitExtension
@ApplicationScoped
@UnlessBuildProfile("test")
@RequiredArgsConstructor
public class DataTenantResolver implements TenantResolver {
    private final Instance<RoutingContext> context;
    private final TenantContext tenantContext;

    @Override
    public String getDefaultTenantId() {
        throw new RuntimeException("No Default Tenant");
    }

    @Override
    public String resolveTenantId() {
        if (Objects.isNull(tenantContext.getTenantIdentifier())) {
            String tenantId = Objects
                .requireNonNullElse(context.get().get(TENANT_IDENTIFIER_CONTEXT_NAME), DEFAULT_TENANT_IDENTIFIER);
            tenantContext.setTenantIdentifier(TenantIdentifier.of(tenantId));
        }

        return tenantContext.getTenantIdentifier().getIdentifier();
    }
}
