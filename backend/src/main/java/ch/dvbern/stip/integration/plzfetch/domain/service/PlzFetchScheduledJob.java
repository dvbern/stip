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

import java.time.LocalDate;

import ch.dvbern.stip.api.common.type.TenantIdentifier;
import ch.dvbern.stip.api.common.util.QuarkusTransactionUtil;
import ch.dvbern.stip.api.config.type.StipConfig;
import ch.dvbern.stip.api.plz.service.PlzService;
import ch.dvbern.stip.integration.plzfetch.domain.port.PlzFetchPortFactory;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class PlzFetchScheduledJob implements Job {
    private final PlzFetchPortFactory plzFetchPortFactory;
    private final PlzService plzService;
    private final StipConfig config;
    private final Scheduler scheduler;

    private final String PLZ_SCHEDULER_CONFIG_KEY = "plz-data";
    private final String PLZ_FETCH_JOB_PREFIX = "PlzFetchScheduledJob";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        QuarkusTransactionUtil.runForTenantInNewTransaction(
            TenantIdentifier.BERN.getIdentifier(),
            () -> {
                final var plzFetchPort = plzFetchPortFactory.getPlzFetchAdapter();

                try {
                    LOG.info("Fetching PLZ data");
                    final var plzFetchData = plzFetchPort.fetchData();
                    plzFetchData.ifPresent(fetchData -> {
                        final var plzList = PlzFetchDataMapper.toPlzList(fetchData);
                        plzService.overwriteAll(plzList);
                    });
                    LOG.info("PLZ data fetched and checked/saved successfully");
                } catch (Throwable e) {
                    LOG.error("Error fetching PLZ data", e);
                }
            }
        );
    }

    void onStart(@Observes StartupEvent startupEvent) {
        final var jobDetail = JobBuilder.newJob(PlzFetchScheduledJob.class)
            .withIdentity(PLZ_FETCH_JOB_PREFIX + LocalDate.now())
            .build();

        final var schedule = CronScheduleBuilder.cronSchedule(config.scheduler().get(PLZ_SCHEDULER_CONFIG_KEY).cron());

        final var trigger = TriggerBuilder.newTrigger()
            .withIdentity(PLZ_FETCH_JOB_PREFIX + "trigger-" + LocalDate.now())
            .startNow()
            .withSchedule(schedule)
            .build();

        try {
            execute(null);
        } catch (JobExecutionException e) {
            LOG.error("Error executing PlzFetchScheduledJob on startup", e);
        }

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            LOG.error("Error scheduling PlzFetchScheduledJob", e);
        }
    }
}
