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

package ch.dvbern.stip.api.common.scheduledtask;

import ch.dvbern.stip.api.tenancy.service.DataTenantResolver;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@RunForTenant
public class RunForTenantInterceptor {
    @AroundInvoke
    Object aroundInvoke(final InvocationContext invocationContext) throws Exception {
        final var annotation = invocationContext.getMethod().getAnnotation(RunForTenant.class);

        // ignored because it's reset in the finalizer of the returned ExplicitTenantIdScope as such unused
        try (final var ignored = DataTenantResolver.setTenantId(annotation.value().getIdentifier())) {
            return invocationContext.proceed();
        }
    }
}
