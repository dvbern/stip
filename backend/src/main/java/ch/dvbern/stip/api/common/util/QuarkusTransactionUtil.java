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
import ch.dvbern.stip.api.tenancy.service.TenantContext;
import io.quarkus.arc.Arc;
import io.quarkus.narayana.jta.QuarkusTransaction;
import lombok.experimental.UtilityClass;

@UtilityClass
public class QuarkusTransactionUtil {
    public void runForTenantInNewTransaction(final TenantIdentifier tenantIdentifier, final Runnable runnable) {
        QuarkusTransaction.requiringNew().run(() -> {
            Arc.container().requestContext().activate();
            Arc.container().instance(TenantContext.class).get().setTenantIdentifier(tenantIdentifier);
            try {
                runnable.run();
            } finally {
                Arc.container().requestContext().deactivate();
            }
        });
    }

    public void runForTenantsInNewTransaction(final List<TenantIdentifier> tenantIdentifiers, final Runnable runnable) {
        tenantIdentifiers.forEach(
            tenantIdentifier -> runForTenantInNewTransaction(tenantIdentifier, runnable)
        );
    }

    public void runForTenantsInNewTransaction(final TenantIdentifier[] tenantIdentifiers, final Runnable runnable) {
        runForTenantsInNewTransaction(Arrays.stream(tenantIdentifiers).toList(), runnable);
    }
}
