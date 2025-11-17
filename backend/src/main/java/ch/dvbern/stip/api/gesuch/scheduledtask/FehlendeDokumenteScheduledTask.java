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

package ch.dvbern.stip.api.gesuch.scheduledtask;

import ch.dvbern.stip.api.common.scheduledtask.RunForTenant;
import ch.dvbern.stip.api.common.type.MandantIdentifier;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class FehlendeDokumenteScheduledTask {
    private final GesuchService gesuchService;

    @Transactional
    // @Scheduled(cron = "{kstip.fehlendedokumente.cron}", concurrentExecution = ConcurrentExecution.SKIP)
    @RunForTenant(MandantIdentifier.BERN)
    @Startup
    public void run() {
        try {
            LOG.info("Processing gesuchs in FEHLENDE_DOKUMENTE");
            gesuchService.checkForFehlendeDokumenteOnAllGesuche();
            gesuchService.checkForFehlendeDokumenteOnAllAenderungen();
            LOG.info("Done processing gesuchs in FEHLENDE_DOKUMENTE");
        } catch (Throwable e) {
            LOG.error(e.toString(), e);
        }
    }
}
