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

import ch.dvbern.stip.api.common.scheduledtask.RunForTenantsScheduledTask;
import ch.dvbern.stip.api.common.type.TenantIdentifier;

/**
 * Represent a code block that runs for an explicitly defined tenant.
 * Must be used inside a try-with-resource statement to properly work.
 * Primarily designed for the {RunForTenants} interceptor/ {RunForTenantsInterceptor}.
 * FJ: Now used in the {@link RunForTenantsScheduledTask}
 */
public class ExplicitTenantIdScope implements AutoCloseable {
    private final ThreadLocal<TenantIdentifier> explicitTenantId;

    public ExplicitTenantIdScope(final ThreadLocal<TenantIdentifier> toSet, final TenantIdentifier value) {
        explicitTenantId = toSet;
        explicitTenantId.set(value);
    }

    @Override
    public void close() {
        explicitTenantId.remove();
    }
}
