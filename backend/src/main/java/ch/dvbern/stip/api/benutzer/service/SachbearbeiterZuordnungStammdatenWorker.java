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

package ch.dvbern.stip.api.benutzer.service;

import java.util.concurrent.atomic.AtomicBoolean;

import ch.dvbern.stip.api.tenancy.service.DataTenantResolver;
import ch.dvbern.stip.api.zuordnung.service.ZuordnungService;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import jakarta.annotation.Nullable;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Startup
@Slf4j
public class SachbearbeiterZuordnungStammdatenWorker {
    private final WorkerExecutor executor;

    private final ZuordnungService zuordnungService;

    private final AtomicBoolean running = new AtomicBoolean(false);

    @Inject
    public SachbearbeiterZuordnungStammdatenWorker(
    final Vertx vertx,
    final ZuordnungService zuordnungService
    ) {
        executor = vertx.createSharedWorkerExecutor("SachbearbeiterZuordnungStammdatenWorker");
        this.zuordnungService = zuordnungService;
    }

    void tearDown(@Observes ShutdownEvent event) {
        LOG.info(
            "SachbearbeiterZuordnungStammdatenWorker shut down {}",
            event.isStandardShutdown() ? "normally" : "abnormally"
        );
        executor.close();
    }

    public void updateZuordnung(final String tenantId) {
        if (!running.compareAndSet(false, true)) {
            throw new IllegalStateException("A previous assignment run is still in progress");
        }

        run(
            tenantId,
            zuordnungService::updateZuordnungOnAllFaelle,
            () -> running.set(false)
        );
    }

    private void run(final String tenantId, final Runnable body, final @Nullable Runnable tail) {
        executor.executeBlocking(() -> {
            try {
                QuarkusTransaction.requiringNew().run(() -> {
                    try (final var ignored = DataTenantResolver.setTenantId(tenantId)) {
                        body.run();
                    }
                });
            } catch (Exception e) {
                LOG.error(e.toString(), e);
            } finally {
                if (tail != null) {
                    tail.run();
                }
            }

            return null;
        });
    }
}
