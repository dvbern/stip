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

import ch.dvbern.stip.api.common.scheduledtask.RunForTenant;
import ch.dvbern.stip.api.common.scheduledtask.RunForTenantInterceptor;

/**
 * Represent a code block that runs for an explicitly defined tenant.
 * Must be used inside a try-with-resource statement to properly work.
 * Primarily designed for the {@link RunForTenant} interceptor/ {@link RunForTenantInterceptor}.
 */
public class ExplicitTenantIdScope implements AutoCloseable {
    private final ThreadLocal<String> explicitTenantId;

    public ExplicitTenantIdScope(final ThreadLocal<String> toSet, final String value) {
        explicitTenantId = toSet;
        explicitTenantId.set(value);
    }

    @Override
    public void close() {
        explicitTenantId.remove();
    }
}
