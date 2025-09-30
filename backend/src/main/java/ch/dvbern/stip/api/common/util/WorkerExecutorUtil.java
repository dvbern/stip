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
import io.vertx.core.WorkerExecutor;
import jakarta.annotation.Nullable;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;

@UtilityClass
public class WorkerExecutorUtil {
    public void executeBlockingWithTransaction(
        final WorkerExecutor executor,
        final String tenantId,
        final Runnable body,
        final @Nullable Runnable tail,
        final @Nullable Logger log
    ) {
        executor.executeBlocking(() -> {
            try {
                QuarkusTransaction.requiringNew().run(() -> {
                    try (
                        final var ignored1 = DataTenantResolver.setTenantId(tenantId);
                        final var ignored2 = TenantService.setTenantId(tenantId);
                    ) {
                        Arc.container().requestContext().activate();
                        body.run();
                        Arc.container().requestContext().deactivate();
                    }
                });
            } catch (Exception ex) {
                if (log != null) {
                    log.error(ex.toString(), ex);
                }
            } finally {
                if (tail != null) {
                    tail.run();
                }
            }

            return null;
        });
    }
}
