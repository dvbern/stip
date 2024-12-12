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

package ch.dvbern.stip.api.plz.service;

import java.io.IOException;

import ch.dvbern.stip.api.tenancy.service.DataTenantResolver;
import com.opencsv.exceptions.CsvException;
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
    public void run() {
        try {
            LOG.info("Fetching PLZ data from scheduled task");
            DataTenantResolver.setTenantId("none");
            try {
                plzDataFetchService.fetchData();
            } catch (IOException | CsvException e) {
                LOG.error(e.toString(), e);
            } finally {
                DataTenantResolver.setTenantId(null);
                LOG.info("Done Fetching PLZ data");
            }
        } catch (Exception e) {
            LOG.error(e.toString(), e);
        }
    }

    @Startup
    public void runAtStartup() {
        run();
    }
}
