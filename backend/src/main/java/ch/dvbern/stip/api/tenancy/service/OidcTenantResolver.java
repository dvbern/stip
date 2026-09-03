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
import io.quarkus.oidc.TenantResolver;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class OidcTenantResolver implements TenantResolver {
    private final TenantService tenantService;

    public static final String DEFAULT_TENANT_IDENTIFIER = TenantIdentifier.BERN.getIdentifier();
    public static final String TENANT_IDENTIFIER_CONTEXT_NAME = "tenantId";

    @Override
    public String resolve(RoutingContext context) {
        final var authority = context.request().authority().host();
        final var parts = authority.split("\\.");
        LOG.info("Headers [{}]", context.request().headers().entries().toString());
        LOG.info("Absolute AUTHORITY [{}]", context.request().authority().host());
        LOG.info("Checking parts [{}]", parts[0]);
        final var tenant = tenantService.resolveTenant(parts[0]);
        LOG.info("Got Tenant [{}]", parts[0]);

        context.put(TENANT_IDENTIFIER_CONTEXT_NAME, tenant.getIdentifier());

        return tenant.getIdentifier();
    }
}
