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

import java.util.concurrent.atomic.AtomicReference;

import ch.dvbern.stip.api.tenancy.service.DataTenantResolver;
import io.quarkus.narayana.jta.QuarkusTransaction;
import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunForTenant
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_AFTER + 10)
public class RunForTenantInterceptor {
    @AroundInvoke
    public Object aroundInvoke(final InvocationContext invocationContext) throws Exception {
        final var annotation = invocationContext.getMethod().getAnnotation(RunForTenant.class);

        AtomicReference<Object> proceed = new AtomicReference<>();

        // ignored because it's reset in the finalizer of the returned ExplicitTenantIdScope as such unused
        QuarkusTransaction.requiringNew().run(() -> {
            try (final var ignored = DataTenantResolver.setTenantId(annotation.value().getIdentifier())) {
                proceed.set(invocationContext.proceed());
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
        );
        return proceed;
    }
}
