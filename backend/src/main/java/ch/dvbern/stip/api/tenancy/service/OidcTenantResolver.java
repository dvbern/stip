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
import io.quarkus.oidc.TenantResolver;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OidcTenantResolver implements TenantResolver {

    public static final String DEFAULT_TENANT_IDENTIFIER = MandantIdentifier.BERN.name().toLowerCase();
    public static final String TENANT_IDENTIFIER_CONTEXT_NAME = "tenantId";

    @Override
    @SuppressWarnings("java:S1135")
    public String resolve(RoutingContext context) {

        final var tenant = DEFAULT_TENANT_IDENTIFIER;

        // TODO KSTIP-781: resolve tenant based on request and implement a test for it and remove SupressWarning
        context.put(TENANT_IDENTIFIER_CONTEXT_NAME, tenant);

        return tenant;
    }
}
