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
        final @Nullable Runnable onExceptionInTransaction,
        final @Nullable Runnable tailNoTransaction,
        final @Nullable Logger log
    ) {
        executor.executeBlocking(() -> {
            try {
                QuarkusTransactionUtil.runForTenantInNewTransaction(tenantId, body);
            } catch (Exception ex) {
                if (log != null) {
                    log.error(ex.toString(), ex);
                }

                if (onExceptionInTransaction != null) {
                    QuarkusTransactionUtil.runForTenantInNewTransaction(tenantId, onExceptionInTransaction);
                }
            } finally {
                if (tailNoTransaction != null) {
                    tailNoTransaction.run();
                }
            }

            return null;
        });
    }
}
