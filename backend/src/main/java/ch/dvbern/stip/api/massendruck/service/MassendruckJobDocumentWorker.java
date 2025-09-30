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

package ch.dvbern.stip.api.massendruck.service;

import java.util.UUID;

import ch.dvbern.stip.api.common.util.WorkerExecutorUtil;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Startup
@Slf4j
public class MassendruckJobDocumentWorker {
    private final WorkerExecutor executor;
    private final MassendruckJobPdfService massendruckJobPdfService;

    @Inject
    public MassendruckJobDocumentWorker(
    final Vertx vertx,
    final MassendruckJobPdfService massendruckJobPdfService
    ) {
        executor = vertx.createSharedWorkerExecutor("MassendruckJobDocumentWorker");
        this.massendruckJobPdfService = massendruckJobPdfService;
    }

    void tearDown(@Observes ShutdownEvent shutdown) {
        LOG.info("MassendruckJobDocumentWorker shut down {}", shutdown.isStandardShutdown());
        executor.close();
    }

    public void combineDocuments(final UUID massendruckJobId, final String tenantId) {
        WorkerExecutorUtil.executeBlockingWithTransaction(
            executor,
            tenantId,
            () -> massendruckJobPdfService.downloadCombineAndSaveForJob(massendruckJobId),
            () -> massendruckJobPdfService.setFailedStatusOnJob(massendruckJobId),
            null,
            LOG
        );
    }
}
