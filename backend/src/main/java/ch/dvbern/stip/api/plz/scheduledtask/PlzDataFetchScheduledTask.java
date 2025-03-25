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

package ch.dvbern.stip.api.plz.scheduledtask;

import ch.dvbern.stip.api.common.scheduledtask.RunForTenant;
import ch.dvbern.stip.api.common.type.MandantIdentifier;
import ch.dvbern.stip.api.plz.service.PlzDataFetchService;
import io.quarkus.runtime.Startup;
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Singleton
@Slf4j
public class PlzDataFetchScheduledTask {
    private final PlzDataFetchService plzDataFetchService;

    @Transactional
    @Scheduled(cron = "{kstip.plzdata.cron}")
    @RunForTenant(MandantIdentifier.BERN)
    public void run() {
        try {
            LOG.info("Fetching PLZ data from scheduled task");
            plzDataFetchService.fetchData();
            LOG.info("Done Fetching PLZ data");
        } catch (Throwable e) {
            LOG.error(e.toString(), e);
        }
    }

    @Startup
    public void runAtStartup() {
        run();
    }
}
