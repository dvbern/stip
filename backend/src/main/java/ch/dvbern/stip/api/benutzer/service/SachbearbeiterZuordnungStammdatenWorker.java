package ch.dvbern.stip.api.benutzer.service;

import java.util.concurrent.atomic.AtomicBoolean;

import ch.dvbern.stip.api.tenancy.service.DataTenantResolver;
import ch.dvbern.stip.api.zuordnung.service.ZuordnungService;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
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
            return; // TODO Error/ Exception?
        }

        executor.executeBlocking(() -> {
            try {
                QuarkusTransaction.requiringNew().run(() -> {
                    DataTenantResolver.setTenantId(tenantId);
                    zuordnungService.updateZuordnungOnFall();
                });
            } catch (Exception e) {
                LOG.error(e.toString());
            } finally {
                running.set(false);
            }
            return null;
        });
    }
}
