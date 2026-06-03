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

package ch.dvbern.stip.api.common.util;

import java.util.Arrays;
import java.util.List;

import ch.dvbern.stip.api.common.type.TenantIdentifier;
import ch.dvbern.stip.api.tenancy.service.DataTenantResolver;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import io.quarkus.arc.Arc;
import io.quarkus.narayana.jta.QuarkusTransaction;
import lombok.experimental.UtilityClass;

@UtilityClass
public class QuarkusTransactionUtil {
    public void runForTenantInNewTransaction(final TenantIdentifier tenantIdentifier, final Runnable runnable) {
        QuarkusTransaction.requiringNew().run(() -> {
            try (
                // ignored because it's reset in the finalizer of the returned ExplicitTenantIdScope as such unused
                final var ignored1 = DataTenantResolver.setTenantId(tenantIdentifier);
                final var ignored2 = TenantService.setTenantId(tenantIdentifier);
            ) {
                Arc.container().requestContext().activate();
                runnable.run();
                Arc.container().requestContext().deactivate();
            }
        });
    }

    public void runForTenantsInNewTransaction(final List<TenantIdentifier> tenantIdentifiers, final Runnable runnable) {
        for (final TenantIdentifier tenantIdentifier : tenantIdentifiers) {
            runForTenantInNewTransaction(tenantIdentifier, runnable);
        }
    }

    public void runForTenantsInNewTransaction(final TenantIdentifier[] tenantIdentifiers, final Runnable runnable) {
        runForTenantsInNewTransaction(Arrays.stream(tenantIdentifiers).toList(), runnable);
    }
}
