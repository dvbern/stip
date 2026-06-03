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

package ch.dvbern.stip.integration.plzfetch.domain.service;

import ch.dvbern.stip.api.common.scheduledtask.RunForTenantsScheduledTask;
import ch.dvbern.stip.api.common.type.TenantIdentifier;
import ch.dvbern.stip.api.common.util.QuarkusTransactionUtil;
import ch.dvbern.stip.api.plz.service.PlzService;
import ch.dvbern.stip.integration.plzfetch.domain.port.PlzFetchPortFactory;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class PlzFetchScheduledTask extends RunForTenantsScheduledTask {
    private static final String NAME = "PlzFetch";
    private static final String SCHEDULER_CRON_CONFIG_KEY = "plz-data";
    private static final TenantIdentifier TENANT_IDENTIFIER = TenantIdentifier.BERN;

    @Inject
    PlzFetchPortFactory plzFetchPortFactory;

    @Inject
    PlzService plzService;

    @Inject
    PlzFetchDataMapper plzFetchDataMapper;

    public PlzFetchScheduledTask() {
        super(NAME, SCHEDULER_CRON_CONFIG_KEY, TENANT_IDENTIFIER);
    }

    @Override
    @Transactional
    public void run() {
        final var plzFetchPort = plzFetchPortFactory.getPlzFetchAdapter();

        try {
            LOG.info("Fetching PLZ data");
            final var plzFetchData = plzFetchPort.fetchData();
            plzFetchData.ifPresent(fetchData -> {
                final var plzList = plzFetchDataMapper.toPlzList(fetchData);
                plzService.overwriteAll(plzList);
            });
            LOG.info("PLZ data fetched and checked/saved successfully");
        } catch (Throwable e) {
            LOG.error("Error fetching PLZ data", e);
        }
    }

    void onStart(@Observes StartupEvent startupEvent) {
        QuarkusTransactionUtil.runForTenantInNewTransaction(
            TENANT_IDENTIFIER,
            this::run
        );
    }
}
