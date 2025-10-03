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

import ch.dvbern.stip.api.tenancy.service.DataTenantResolver;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import io.quarkus.arc.Arc;
import io.quarkus.narayana.jta.QuarkusTransaction;
import lombok.experimental.UtilityClass;

@UtilityClass
public class QuarkusTransactionUtil {
    public void runForTenantInNewTransaction(final String tenantId, final Runnable runnable) {
        QuarkusTransaction.requiringNew().run(() -> {
            try (
                // ignored because it's reset in the finalizer of the returned ExplicitTenantIdScope as such unused
                final var ignored1 = DataTenantResolver.setTenantId(tenantId);
                final var ignored2 = TenantService.setTenantId(tenantId);
            ) {
                Arc.container().requestContext().activate();
                runnable.run();
                Arc.container().requestContext().deactivate();
            }
        });
    }
}
